package cn.bixin.sona.gateway.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinwei
 */
public class RoundRobinLoadLoadBalance<T> implements LoadBalance<T> {

    private final AtomicInteger id = new AtomicInteger();

    private final List<T> list;

    private final boolean optimize;

    public RoundRobinLoadLoadBalance(List<T> list) {
        this.list = list;
        optimize = isPowerOfTwo(list.size());
    }

    @Override
    public T selectNode(String key) {
        if (optimize) {
            return list.get(id.getAndIncrement() & list.size() - 1);
        }
        return list.get(id.getAndIncrement() % list.size());
    }

    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }
}
