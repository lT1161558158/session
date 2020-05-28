package top.trister.session.factory;

import lombok.SneakyThrows;
import top.trister.session.enums.SessionEnums;
import top.trister.session.session.ConnectSession;
import top.trister.session.session.LoginSession;
import top.trister.session.session.Session;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author 11615
 */
public class SessionFactory {
    public static final String USER = "user";
    public static final String PASSWORD = "password";


    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String TIMEOUT = "timeout";
    public static final String CHARSET = "charset";


    @SneakyThrows
    public Session newInstance(SessionEnums type, Map<String, Object> args, boolean initiate) {
        Session session = type.getClazz().newInstance();

        Object timeout = args.get(TIMEOUT);
        if (timeout != null) {
            session.timeout((Integer) timeout, TimeUnit.SECONDS);
        }
        String charset = Objects.toString(args.get(CHARSET), null);
        if (charset != null) {
            session.charset(Charset.forName(charset));
        }
        if (!initiate) {
            return session;
        }
        if (session instanceof ConnectSession) {
            String host = get(args, HOST, String.class);
            Integer port = get(args, PORT, Integer.class);
            ((ConnectSession) session).connect(host, port);
        }
        if (session instanceof LoginSession) {
            String user = get(args, USER, String.class);
            String password = get(args, PASSWORD, String.class);
            ((LoginSession) session).login(user, password);
        }
        session.active();
        return session;
    }

    public static <T> T get(Map<String, Object> args, String key, Class<T> clazz) {
        Optional<Object> optional = Optional.ofNullable(args.get(key));
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("must provide " + key);
        }
        return clazz.cast(optional.get());
    }

}
