package top.trister.session.session;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 会话
 * @author 11615
 */
public interface Session extends AutoCloseable {
    /**
     * 判断是否存活
     * @return true if alive
     */
    boolean isAlive();

    /**
     * 返回一个给定的charset的reader
     * @return reader
     */
    Reader reader();

    /**
     * 返回原始流
     * @return input
     */
    InputStream input();

    /**
     * 返回原始流
     * @return output
     */
    OutputStream output();

    /**
     * 返回一个给定的charset的writer
     * @return writer
     */
    Writer writer();

    /**
     * 设置 session 的超时
     * @param time time
     * @param unit unit
     */
    void timeout(long time, TimeUnit unit);

    /**
     * 获得超时值
     * @return time
     */
    long timeout();

    /**
     * 超时的时间单位
     * @return unit
     */
    TimeUnit unit();

    /**
     * 指定编码集
     * @param charset charset
     */
    void charset(Charset charset);

    /**
     * 激活 session
     */
    void active();

}
