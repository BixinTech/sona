package cn.bixin.sona.server.im.ack;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.mq.KafkaSender;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author qinwei
 */
@Service
public class MessageArrivalService {

    private static final Logger log = LoggerFactory.getLogger(MessageArrivalService.class);

    private static final String HIGH_SCORE_USER = "sona:c:u:zs";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private KafkaSender kafkaSender;

    public Set<Long> getNeedAckUids(RoomMessageRequest request) {
        Set<Long> results;
        if (CollectionUtils.isEmpty(request.getAckUids())) {
            results = Sets.newHashSet();
        } else {
            results = new HashSet<>(request.getAckUids());
        }
        try {
            results.addAll(getHighScoreUids(request.getRoomId()));
        } catch (Exception e) {
            log.error("Unable to get ack uids ", e);
        }
        return results;
    }

    private Set<Long> getHighScoreUids(long roomId) {
        Set<String> zset = redisTemplate.opsForZSet().reverseRange(Joiner.on(":").join(HIGH_SCORE_USER, roomId), 0, -1);
        if (CollectionUtils.isEmpty(zset)) {
            return Sets.newHashSet();
        } else {
            return zset.stream().map(Long::parseLong).collect(Collectors.toSet());
        }
    }

    public void sendChatRoomAckMessage(RoomMessageRequest request) {
        kafkaSender.send("TOPIC_CHATROOM_MESSAGE_ACK", request);
    }

}
