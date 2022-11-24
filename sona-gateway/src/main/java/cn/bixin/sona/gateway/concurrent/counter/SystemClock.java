package cn.bixin.sona.gateway.concurrent.counter;

/**
 * @author qinwei
 * <p>
 * SystemClock 每秒有1000次的系统调用，也就是说 qps 大于1000的时候收益比较大，在目前的场景中还是值得的
 */
public final class SystemClock {

    private static volatile long currentTimeMillis;

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread daemon = new Thread(() -> {
            while (true) {
                currentTimeMillis = System.currentTimeMillis();
                try {
                    Thread.sleep(1);
                } catch (Throwable ignored) {
                }
            }
        });
        daemon.setDaemon(true);
        daemon.setName("system-clock");
        daemon.start();
    }

    private SystemClock() {
    }

    public static long currentTimeMillis() {
        return currentTimeMillis;
    }

}
