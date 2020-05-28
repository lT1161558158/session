package top.trister.session.session.impl;

import org.junit.jupiter.api.Test;
import top.trister.session.channel.TransparentChannel;
import top.trister.session.shell.Shell;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SshSessionTest {

    @Test
    void test() throws Exception {
        int count=(int)1;
        for (int i = 0; i < count; i++) {
            try (SshSession session = new SshSession()) {
                session.connect("192.168.3.126", 22);
                session.charset(StandardCharsets.UTF_8);
                session.login("root", "555");
                TransparentChannel channel = new TransparentChannel(session, false);
                Shell shell = new Shell(channel);
                shell.setTimeout(1);
                shell.exec("\n",1, TimeUnit.SECONDS);
                String result = shell.exec("id");
//                System.out.println(result);
                assertTrue(result.contains("root"));
//                shell.su("patrol");
                System.out.println(shell.exec("id"));
                shell.cd("/opt");
                System.out.println(shell.exec("pwd"));
                System.out.println(shell.exec("ls"));
                shell.createTextFile("111.sc","4567'\n\"");
                System.out.println(shell.exec("ls"));
                System.out.println(shell.exec("ls"));

                System.out.println(i);
            }
        }
//        try (SshSession session = new SshSession()) {
//            session.connect("192.168.3.126", 22);
//            session.login("root", "ponshine");
//            TransparentChannel channel = new TransparentChannel(session, false);
//            channel.waitUntil(readable.or(timeout(3, TimeUnit.SECONDS))).readUntil(withLine);//登陆后的回显,可能没有回显
//            channel.write("id");
//            channel.waitUntil(readable).readUntil(withLine);
//            assertTrue(channel.current().getRead().toString().contains("root"));
//            System.out.println(channel.current().getRead());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}