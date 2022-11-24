package cn.bixin.sona.gateway.channel.support;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 连接接入 黑名单过滤
 *
 * @author qinwei
 */
@Slf4j
public class AccessFilter {

    public static final AccessFilter INSTANCE = new AccessFilter();

    private static final Splitter FIELD_SPLITTER = Splitter.on('/').trimResults().omitEmptyStrings();

    private volatile IpFilterRule[] rules;

    public void updateRules(String str) {
        List<String> ruleStrs = JSON.parseArray(str, String.class);
        if (ruleStrs == null) {
            log.error("AccessFilter updateRules() error!", new IllegalArgumentException());
            return;
        }
        IpFilterRule[] newRules = new IpFilterRule[ruleStrs.size()];
        for (int i = 0; i < ruleStrs.size(); i++) {
            try {
                String ruleStr = ruleStrs.get(i);
                List<String> fields = FIELD_SPLITTER.splitToList(ruleStr);
                if (!fields.isEmpty()) {
                    String ip = fields.get(0);
                    int maskLen = 32;
                    if (fields.size() >= 2) {
                        maskLen = Integer.parseInt(fields.get(1));
                    }
                    newRules[i] = new IpSubnetFilterRule(ip, maskLen, IpFilterRuleType.REJECT);
                }
            } catch (Exception e) {
                log.error("AccessFilter updateRules() error!", e);
            }
        }
        log.info("AccessFilter updateRules() success, newRules={}", str);
        rules = newRules;
    }

    public boolean accept(InetSocketAddress remoteAddress) {
        if (rules == null) {
            return true;
        }
        try {
            for (IpFilterRule rule : rules) {
                if (rule == null) {
                    continue;
                }
                if (rule.matches(remoteAddress)) {
                    return rule.ruleType() == IpFilterRuleType.ACCEPT;
                }
            }
        } catch (Exception e) {
            log.error("AccessFilter accept() error!", e);
        }
        return true;
    }

}
