package top.trister.session.session.impl;

import org.junit.jupiter.api.Test;
import top.trister.session.channel.TransparentChannel;
import top.trister.session.session.Session;

import java.nio.charset.Charset;

import static java.util.concurrent.TimeUnit.SECONDS;
import static top.trister.session.channel.PredicateHelper.*;

class LocalSessionTest {
    @Test
    void test() {
        Session session = new LocalSession();
        session.charset(Charset.forName("gbk"));
        session.active();
        TransparentChannel channel = new TransparentChannel(session,false);
        System.out.println(channel.write("ipconfig").waitUntil(readable).readUntil(unReadableLoop(10).and(timeout(1, SECONDS))).current().getRead().toString());
        System.out.println(channel.write("ipconfig2").waitUntil(readable).readUntil(unReadableLoop(10).and(timeout(1, SECONDS))).current().getRead().toString());
        for (TransparentChannel.WriteAndRead writeAndRead : channel.history()) {
            System.out.println(writeAndRead.getRead() + " "+writeAndRead);
        }
    }

}