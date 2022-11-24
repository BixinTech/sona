package cn.bixin.sona.gateway.loadbalance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author qinwei
 */
public class ConsistentHashLoadBalance<T> implements LoadBalance<T> {

    private static final int REPLICA_NUM = 8;

    private static final String VIRTUAL_SEPARATOR = "|";

    private final TreeMap<Integer, T> virtualNodes = new TreeMap<>();

    private final HashStrategy strategy = new FnvHashStrategy();

    public ConsistentHashLoadBalance(List<T> list) {
        this(list, REPLICA_NUM);
    }

    public ConsistentHashLoadBalance(List<T> list, int replicaNum) {
        for (T t : list) {
            String key = t.toString();
            for (int i = 0; i < replicaNum; i++) {
                virtualNodes.put(strategy.hash(key + VIRTUAL_SEPARATOR + Integer.toHexString(i)), t);
            }
        }
    }

    @Override
    public T selectNode(String key) {
        return locate(strategy.hash(key));
    }

    private T locate(int hash) {
        Map.Entry<Integer, T> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

}
