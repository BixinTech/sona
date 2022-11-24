package cn.bixin.sona.server.im.manager;

import cn.bixin.sona.api.im.dto.MessageInfoDTO;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.server.im.config.HbaseConfig;
import cn.bixin.sona.server.im.dto.RoomMessageDTO;
import cn.bixin.sona.server.im.service.MessageRecorderService;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MessageQueryManager {

    private static final Logger log = LoggerFactory.getLogger(MessageQueryManager.class);

    @Resource
    IdGenerator idGenerator;
    @Resource
    HbaseConfig hbaseConfig;
    @Resource
    MessageRecorderService messageRecorderService;

    /**
     * 查询房间信息
     * @param uid
     * @param roomId
     * @param reserved
     * @param anchor
     * @param limit
     * @return
     */
    public PageResult<MessageInfoDTO> queryMessageInfoPage(Long uid, long roomId, boolean reserved, String anchor, int limit) {
        String minMsgId;
        String maxMsgId;
        if(reserved){
            minMsgId = idGenerator.getSpecialSequenceId(hbaseConfig.getHbaseLimitTime());
            maxMsgId = NumberUtils.isDigits(anchor) ? anchor : idGenerator.strId();
        } else {
            minMsgId = NumberUtils.isDigits(anchor) ?  anchor : idGenerator.getSpecialSequenceId(hbaseConfig.getHbaseLimitTime());
            maxMsgId = idGenerator.strId();
        }
        try {
            List<RoomMessageDTO> queryList = messageRecorderService.queryRoomMessage(uid, roomId, minMsgId, maxMsgId, limit, reserved);
            boolean isEnd = queryList.size() < limit;
            List<MessageInfoDTO> resList = queryList.stream().filter(Objects::nonNull).map(item -> {
                MessageInfoDTO result = new MessageInfoDTO();
                BeanUtils.copyProperties(item , result);
                return result;
            }).collect(Collectors.toList());
            return PageResult.newPageResult(resList, isEnd);
        } catch (Exception e) {
            log.error("queryMessageInfoPage exception, uid:{}, roomId:{},anchor:{}", uid, roomId, anchor, e);
            throw new YppRunTimeException(Code.business("8011", e.getMessage()));
        }
    }

}
