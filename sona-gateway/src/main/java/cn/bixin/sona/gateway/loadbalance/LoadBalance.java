package cn.bixin.sona.gateway.loadbalance;

/**
 * @author qinwei
 */
public interface LoadBalance<T> {

    T selectNode(String key);

}
