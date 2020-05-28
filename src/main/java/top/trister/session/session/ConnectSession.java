package top.trister.session.session;

/**
 * 需要连接的会话
 */
public interface ConnectSession extends Session {
    /**
     *
     * @param host host
     * @param port port
     */
    void connect(String host, int port);

    /**
     *
     * @return true if connected
     */
    boolean isConnected();
}
