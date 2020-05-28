package top.trister.session.channel;

import lombok.*;
import top.trister.session.session.Session;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static top.trister.session.channel.PredicateHelper.*;

public class TransparentChannel {

    @Data
    @ToString(exclude = "read")
    public static class WriteAndRead {
        final String write;
        final StringBuilder read = new StringBuilder();
        final TimeUnit unit = TimeUnit.MILLISECONDS;
        LocalDateTime start = LocalDateTime.now();
        long usingTime = 0;
        LocalDateTime end;

        public WriteAndRead(String write) {
            this.write = write;
        }

        void end() {
            end = LocalDateTime.now();
        }

        void use(long time) {
            usingTime += time;
        }

    }

    @Data
    public class Conditional {
        /**
         * 当前读到的字符数量
         */
        int read;
        /**
         * 当前命令读到的所有内容
         */
        String str;
        /**
         * session
         */
        Session session = TransparentChannel.this.session;
        /**
         * channel
         */
        TransparentChannel channel = TransparentChannel.this;

        public Conditional(int read, String str) {
            this.read = read;
            this.str = str;

        }

        public Conditional() {
        }
    }

    @Getter
    final Session session;
    WriteAndRead writeAndRead = new WriteAndRead(null);
    final List<WriteAndRead> history = new ArrayList<>();
    @Getter
    @Setter
    int writeBeforeReadLoop = 5;
    @Getter
    @Setter
    String lineSplit = "\n";
    @Getter
    @Setter
    boolean allowedTimeout = true;

    /**
     * 每次循环会执行的任务
     */
    Consumer<String> eachLoop;

    public TransparentChannel(Session session) {
        this(session, true);
    }

    @SneakyThrows
    private void sleep(String str) {
        TimeUnit.MILLISECONDS.sleep(1);
    }

    public TransparentChannel(Session session, boolean busy) {
        eachLoop = busy ? str -> {
        } : this::sleep;
        this.session = session;
    }


    @SneakyThrows
    public TransparentChannel write(String cmd) {
        //避免上个命令未读完的数据写到了下一个回显中
        readUntil(unReadableLoop(5));
        Writer writer = session.writer();
        roll(cmd);
        writer.write(cmd);
        if (!cmd.endsWith(lineSplit)) {
            writer.write(lineSplit);
        }
        writer.flush();
        return this;
    }

    /**
     * 滚动当前记录
     *
     * @param write 写入的
     */
    void roll(String write) {
        history.add(writeAndRead);
        writeAndRead.end();
        writeAndRead = new WriteAndRead(write);
    }

    private Predicate<Conditional> wrapPredicate(Predicate<Conditional> test) {
        //会话失活条件
        test = died.or(test);
        if (allowedTimeout) {
            test = test.or(timeout((int) session.timeout(), session.unit()));
        }
        return test;
    }

    /**
     * 等待直到条件达成
     *
     * @param test test
     * @return this
     */
    public TransparentChannel waitUntil(Predicate<Conditional> test) {
        test = wrapPredicate(test);
        while (!test.test(new Conditional())) {
            eachLoop.accept("");
        }
        return this;
    }


    @SneakyThrows
    public TransparentChannel watchUntil(Predicate<Conditional> test, Consumer<String> eachLoop) {
        InnerRead innerRead = new InnerRead();
        Conditional conditional = new Conditional();
        long start = System.currentTimeMillis();
        test = wrapPredicate(test);
        eachLoop = eachLoop.andThen(this.eachLoop);
        do {
            String str = innerRead.innerRead();
            if (str == null) {
                conditional.read = 0;
            } else {
                conditional.read = str.length();
                writeAndRead.read.append(str);
            }
            conditional.str = writeAndRead.read.toString();
            eachLoop.accept(str);
        } while (!test.test(conditional));
        writeAndRead.use(System.currentTimeMillis() - start);
        return this;
    }

    @SneakyThrows
    public TransparentChannel readUntil(Predicate<Conditional> test) {
        return watchUntil(test, str -> {
        });
    }

    public String currentStr() {
        return writeAndRead.read.toString();
    }


    /**
     * 读数据的辅助类
     */
    class InnerRead {
        final char[] bytes = new char[1024];

        /**
         * @return 读入的字符数
         */
        @SneakyThrows
        String innerRead() {
            Reader reader = session.reader();
            if (!reader.ready()) {
                return null;
            }
            int read = reader.read(bytes);
            if (read > 0) {
                return new String(bytes, 0, read);
            } else if (read == 0) {
                return null;
            } else {
                throw new IOException("connect is break!");
            }
        }
    }


    public List<WriteAndRead> history() {
        return new ArrayList<>(history);
    }

    public WriteAndRead current() {
        return writeAndRead;
    }

    public void clearHistory() {
        history.clear();
    }
}
