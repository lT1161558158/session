package top.trister.session.channel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class PredicateHelper {
    /**
     * true 如果condition.str中包含了换行符
     */
    public static Predicate<TransparentChannel.Conditional> withLine = conditional -> conditional.str.contains("\n");

    /**
     * @param mark mark
     * @return true 如果condition.str中包含了mark
     */
    public static Predicate<TransparentChannel.Conditional> withMark(String mark) {
        return conditional -> conditional.str.contains(mark);
    }

    /**
     * @param loop 循环次数
     * @return true 如果空转次数超过了loop
     */
    public static Predicate<TransparentChannel.Conditional> unReadableLoop(int loop) {
        AtomicInteger counter = new AtomicInteger();
        return conditional -> {
            if (readable.test(conditional)) {
                counter.set(0);
            } else {
                counter.getAndIncrement();
            }
            return counter.get() > loop;
        };
    }

    /**
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true 如果已经超时了
     */
    public static Predicate<TransparentChannel.Conditional> timeout(int timeout, TimeUnit unit) {
        long start = System.currentTimeMillis();
        return conditional -> System.currentTimeMillis() - start > unit.toMillis(timeout);
    }


    /**
     * 可读
     */
    public static Predicate<TransparentChannel.Conditional> readable = conditional -> {
        try {
            return conditional.session.reader().ready();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static Predicate<TransparentChannel.Conditional> unreadable = readable.negate();
    /**
     * 会话失活
     */
    public static Predicate<TransparentChannel.Conditional> died = conditional -> !conditional.session.isAlive();


}
