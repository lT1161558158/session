package top.trister.session.session.impl;

import org.junit.jupiter.api.Test;
import top.trister.session.channel.TransparentChannel;
import top.trister.session.session.ConnectSession;

import static java.util.concurrent.TimeUnit.SECONDS;
import static top.trister.session.channel.PredicateHelper.*;

class TelnetSessionTest {
    @Test
    void test() {
        /**
         * export PS1="" 可以去掉命令显示的前缀
         * stty -echo 不显示回显
         * stty echo 显示回显
         */
        ConnectSession session = new TelnetSession();
//        session.charset(Charset.forName("gbk"));
        session.active();
        session.connect("127.0.0.1", 8888);
        TransparentChannel channel = new TransparentChannel(session, false);
        channel.waitUntil(readable.or(timeout(1, SECONDS)));
        channel.readUntil(unReadableLoop(100));
        String str = channel.current()
                .getRead()
                .toString();
        System.out.println(str);
//        System.out.println(channel.readUntil(withLine).current().getRead());
    }
}