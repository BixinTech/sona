package cn.bixin.sona.common.util;


import org.apache.dubbo.common.utils.NetUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author qinwei
 */
@Service
public class IdGenerator {

    private static final int TOTAL_BITS = 64;
    private static final int EPOCH_BITS = 40;
    private static final int NODE_IP_BITS = 16;
    private static final int SEQUENCE_BITS = 8;

    /**
     * Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
     *
     * @param UTC
     * @return
     */
    private static final long CUSTOM_EPOCH = 1420070400000L;
    /**
     * 255.255.255.255
     * 取后16位ip地址
     */
    private static final int NODE_IP = createNodeId();
    private static final int MAX_SEQUENCE = (int) (Math.pow(2, SEQUENCE_BITS) - 1);
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public long id() {
        long currentTimestamp = timestamp();
        synchronized (this) {
            if (currentTimestamp == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                if (sequence == 0) {
                    // Sequence Exhausted, wait till next millisecond.
                    currentTimestamp = waitNextMillis(currentTimestamp);
                }
            } else {
                // reset sequence to start with zero for the next millisecond
                sequence = 0;
            }
            lastTimestamp = currentTimestamp;
        }

        long id = currentTimestamp << (TOTAL_BITS - EPOCH_BITS);
        id |= (NODE_IP << (TOTAL_BITS - EPOCH_BITS - NODE_IP_BITS));
        id |= sequence;
        return id;
    }

    public String strId() {
        return String.valueOf(id());
    }

    /**
     * 获取指定时间戳对应的id序号
     * 注意！！不用于生成消息id，仅用于查询
     *
     * @param timestamp
     * @return
     */
    public String getSpecialSequenceId(long timestamp) {
        long specialTimestamp = timestamp - CUSTOM_EPOCH;
        long id = specialTimestamp << (TOTAL_BITS - EPOCH_BITS);
        id |= (NODE_IP << (TOTAL_BITS - EPOCH_BITS - NODE_IP_BITS));
        return String.valueOf(id);
    }

    /**
     * 获取指定时间戳对应的id序号
     * 注意！！不用于生成消息id，仅用于查询
     *
     * @param timestamp
     * @return
     */
    public String getSequenceIdWithoutNodeId(long timestamp) {
        long specialTimestamp = timestamp - CUSTOM_EPOCH;
        long id = specialTimestamp << (TOTAL_BITS - EPOCH_BITS);
        return String.valueOf(id);
    }

    /**
     * 获取 sequenceId 对应 时间戳
     *
     * @param sequenceId
     * @return
     */
    public long getTimestampFromSequenceId(long sequenceId) {
        return (sequenceId >> (TOTAL_BITS - EPOCH_BITS)) + CUSTOM_EPOCH;
    }

    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }

    private static int createNodeId() {
        String[] split = NetUtils.getLocalHost().split("\\.");
        return (Integer.parseInt(split[2]) << 8) + Integer.parseInt(split[3]);
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }
}
