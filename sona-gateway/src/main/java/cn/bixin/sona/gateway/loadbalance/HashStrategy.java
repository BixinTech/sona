package cn.bixin.sona.gateway.loadbalance;

/**
 * @author qinwei
 */
public interface HashStrategy {

    int hash(String origin);
}
