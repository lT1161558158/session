package top.trister.session.shell;

import lombok.Setter;
import top.trister.session.channel.TransparentChannel;
import top.trister.session.session.Session;

import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static top.trister.session.channel.PredicateHelper.*;

/**
 * @author 11615
 * @date 2020/5/28 14:01
 */
public class Shell {
    public static final char ESC = 27;
    final TransparentChannel transparentChannel;
    @Setter
    int timeout = 3;
    @Setter
    TimeUnit unit = TimeUnit.SECONDS;

    public Shell(Session session) {
        this(new TransparentChannel(session));
    }

    public Shell(TransparentChannel channel) {
        this.transparentChannel = channel;
    }

    /**
     * @param cmd cmd
     * @return 命令的回显
     */
    public String exec(String cmd) {
        return exec(cmd, timeout, unit);
    }

    public String exec(String cmd, int timeout, TimeUnit unit) {
        Predicate<TransparentChannel.Conditional> waiting = readable.or(timeout(timeout, unit));
        transparentChannel.write(cmd)
                .waitUntil(waiting)
                .readUntil(withLine.or(timeout(timeout, unit)));
        return transparentChannel.current().getRead().toString();
    }

    public void exec(String cmd, Predicate<String> test) {
        String result = exec(cmd);
        if (!test.test(result)) {
            throw new IllegalStateException("cmd=" + cmd + " result=" + result);
        }
    }

    /**
     * @param fileName 文件名字
     * @param content  文件内容
     */
    public Shell createFile(String fileName, String content) {
        String encode = new String(Base64.getEncoder().encode(content.getBytes()));
        String cmd = "echo '" + encode + "' | base64 -d  > " + fileName;
        exec(cmd);
        return this;
    }

    /**
     * 创建文本文件
     *
     * @param fileName fileName
     * @param content  content
     */
    public Shell createTextFile(String fileName, String content) {
        final String eof="EOF1234567890QWERTYUIOP";
        String template="echo ''> %s && cat > %s << %s \n%s\n%s\n";
        String cmd = String.format(template,fileName,fileName,eof,content,eof);
        exec(cmd);
        return this;
    }

    /**
     * 切换用户
     *
     * @param who 用户
     * @return this
     */
    public Shell su(String who) {
        String cmd = "su " + who;
        exec(cmd,1,TimeUnit.SECONDS);
        return this;
    }

    public Shell cd(String path) {
        exec("cd " + path);
        return this;
    }
}
