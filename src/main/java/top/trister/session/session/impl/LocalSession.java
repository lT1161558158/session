package top.trister.session.session.impl;

import lombok.SneakyThrows;

public class LocalSession extends AbstractSession {
    Process process;

    @Override
    public void close() throws Exception {
        if (process != null) {
            process.destroy();
        }
    }

    @SneakyThrows
    @Override
    public void active() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        boolean windows = System.getProperty("os.name").contains("dows");
        processBuilder.command(windows ? "cmd" : "bash");
        process = processBuilder.start();
        inputStream = process.getInputStream();
        outputStream = process.getOutputStream();
    }

    @Override
    public boolean isAlive() {
        return process != null && process.isAlive();
    }
}
