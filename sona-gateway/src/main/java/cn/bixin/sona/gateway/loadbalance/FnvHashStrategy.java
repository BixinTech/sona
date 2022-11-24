package cn.bixin.sona.gateway.loadbalance;

/**
 * @author qinwei
 * <p>
 * FNV 能快速 hash 大量数据并保持较小的冲突率，它的高度分散使它适用于 hash 一些非常相近的字符串，比如 URL，hostname，文件名，text，IP 地址等。
 */
public class FnvHashStrategy implements HashStrategy {

    private static final long FNV_32_INIT = 2166136261L;

    private static final int FNV_32_PRIME = 16777619;

    @Override
    public int hash(String origin) {
        final int p = FNV_32_PRIME;
        int hash = (int) FNV_32_INIT;
        for (int i = 0; i < origin.length(); i++) {
            hash = (hash ^ origin.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        hash = Math.abs(hash);
        return hash;
    }
}
