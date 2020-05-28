package top.trister.session.session;

/**
 * 需要登陆的会话
 *
 */
public interface LoginSession extends Session {
    /**
     *
     * @param user 用户名
     * @param password 密码
     */
    void login(String user,String password);

    /**
     *
     * @return true if logged
     */
    boolean isLogged();
}
