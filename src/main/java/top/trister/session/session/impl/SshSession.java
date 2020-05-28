package top.trister.session.session.impl;

import com.jcraft.jsch.*;
import lombok.SneakyThrows;
import top.trister.session.session.ConnectSession;
import top.trister.session.session.LoginSession;

import java.util.Properties;

/**
 * @author 11615
 */
public class SshSession extends AbstractSession implements ConnectSession, LoginSession {
    private final JSch jsch = new JSch();
    private Session session;
    String host;
    int port;


    @Override
    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    @SneakyThrows
    @Override
    public void login(String user, String password) {
        session = jsch.getSession(user, host, port);
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        properties.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.setConfig(properties);
        UserInfo ui = new MyUserInfo(password);
        session.setUserInfo(ui);
        session.setTimeout((int) unit.toMillis(timeout));
        session.connect(session.getTimeout());
        Channel shell = session.openChannel("shell");
        //不要分配一个伪终端
        ((ChannelShell) shell).setPty(false);
        inputStream = shell.getInputStream();
        outputStream = shell.getOutputStream();
        shell.connect(session.getTimeout());
    }

    @Override
    public boolean isLogged() {
        return session != null;
    }

    @Override
    public boolean isAlive() {
        return session != null && session.isConnected();
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.disconnect();
        }
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
        private final String password;

        public MyUserInfo(String password) {
            this.password = password;
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public boolean promptYesNo(String str) {
            return true;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return true;
        }

        @Override
        public boolean promptPassword(String message) {
            return true;
        }

        @Override
        public void showMessage(String message) {
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            return new String[]{this.password};
        }
    }
}
