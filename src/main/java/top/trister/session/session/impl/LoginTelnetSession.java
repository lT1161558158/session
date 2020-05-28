package top.trister.session.session.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import top.trister.session.channel.TransparentChannel;
import top.trister.session.session.LoginSession;

import static top.trister.session.channel.PredicateHelper.withMark;

public class LoginTelnetSession extends TelnetSession implements LoginSession {
    /**
     * 等待输入用户名标记
     */
    @Getter
    @Setter
    String waitUser = "evicename";
    /**
     * 等待输入密码标记
     */
    @Getter
    @Setter
    String waitPassword = "assword";
    /**
     * 登陆完成标记
     */
    @Getter
    @Setter
    String loginFinish = "Congratulation";
    boolean logged = false;

    @SneakyThrows
    @Override
    public void login(String user, String password) {
        TransparentChannel channel = new TransparentChannel(this,false);
        //允许超时
        channel.setAllowedTimeout(true);
        channel.readUntil(withMark(waitUser));
        channel.write(user);
        channel.readUntil(withMark(waitPassword));
        channel.write(password);
        channel.readUntil(withMark(loginFinish));
        logged = true;
    }

    @Override
    public boolean isLogged() {
        return logged;
    }
}
