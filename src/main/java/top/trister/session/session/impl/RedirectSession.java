package top.trister.session.session.impl;

import top.trister.session.session.Session;

import java.io.InputStream;
import java.io.OutputStream;

public class RedirectSession extends AbstractSession implements Session {

    @Override
    public boolean isAlive() {
        return inputStream != null && outputStream != null;
    }

    @Override
    public void close() throws Exception {
    }

    public void redirectInput(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void redirectOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
