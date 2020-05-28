package top.trister.session.enums;

import lombok.Getter;
import top.trister.session.session.Session;
import top.trister.session.session.impl.*;

/**
 * @author 11615
 */

@Getter
public enum SessionEnums {
    //ssh
    SSH(SshSession.class),
    //local
    LOCAL(LocalSession.class),
    //telnet
    TELNET(TelnetSession.class),
    //login_telnet
    LOGIN_TELNET(LoginTelnetSession.class),
    //redirect
    REDIRECT(RedirectSession.class),
    ;
    final Class<? extends Session> clazz;
    final String name;

    SessionEnums(Class<? extends Session> clazz) {
        this.clazz = clazz;
        name = clazz.getSimpleName();
    }
}
