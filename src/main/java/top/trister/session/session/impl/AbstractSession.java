package top.trister.session.session.impl;

import lombok.Getter;
import top.trister.session.session.Session;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author 11615
 */
public abstract class AbstractSession implements Session {

    protected InputStream inputStream;
    protected OutputStream outputStream;
    @Getter
    protected long timeout = 30;
    @Getter
    protected TimeUnit unit = TimeUnit.SECONDS;
    @Getter
    protected Charset charset = StandardCharsets.UTF_8;
    protected Reader reader;
    protected Writer writer;

    @Override
    public Reader reader() {
        if (reader == null) {
            reader = new InputStreamReader(inputStream, charset);
        }
        return reader;
    }

    @Override
    public Writer writer() {
        if (writer == null) {
            writer = new OutputStreamWriter(outputStream, charset);
        }
        return writer;
    }

    @Override
    public void timeout(long time, TimeUnit unit) {
        timeout = time;
        this.unit = unit;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    @Override
    public TimeUnit unit() {
        return unit;
    }

    @Override
    public void charset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void active() {
    }

    @Override
    public InputStream input() {
        return inputStream;
    }

    @Override
    public OutputStream output() {
        return outputStream;
    }
}
