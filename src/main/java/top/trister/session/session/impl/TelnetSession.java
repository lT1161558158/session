package top.trister.session.session.impl;

import lombok.SneakyThrows;
import org.apache.commons.net.telnet.TelnetClient;
import top.trister.session.session.ConnectSession;

public class TelnetSession extends AbstractSession implements ConnectSession {
    final TelnetClient telnet = new TelnetClient();

    @Override
    public boolean isAlive() {
        return telnet.isAvailable();
    }

    @Override
    public void close() throws Exception {
        telnet.disconnect();
    }

    @SneakyThrows
    @Override
    public void connect(String host, int port) {
        telnet.setCharset(charset);
        telnet.setConnectTimeout((int) unit.toMillis(timeout));
        telnet.connect(host, port);
        telnet.setKeepAlive(true);
        telnet.setSoTimeout((int) unit.toMillis(timeout));
        inputStream = telnet.getInputStream();
        outputStream = telnet.getOutputStream();
    }

    @Override
    public boolean isConnected() {
        return telnet.isConnected();
    }
}
