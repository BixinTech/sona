package cn.bixin.sona.server.im.service;

import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import cn.bixin.sona.server.mq.KafkaSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qinwei
 * <p>
 */
@Service
public class SaveMessageService {

    @Resource
    private KafkaSender kafkaSender;

    /**
     * 保存房间消息
     */
    public void saveRoomMessage(RoomMessageRequest request) {
        if (request.isNeedToSave()) {
            kafkaSender.send("TOPIC-ROOM-MESSAGE-RECORDER", ConvertUtils.convertChatSaveMessage(request));
        }
    }

    /**
     * 保存群组消息
     */
    public void saveGroupMessage(GroupMessageRequest request) {
        kafkaSender.send("TOPIC-ROOM-MESSAGE-RECORDER", ConvertUtils.convertGroupSaveMessage(request));
    }

}
