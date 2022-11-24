package cn.bixin.sona.console.domain.converter;

import cn.bixin.sona.console.domain.db.Stream;
import cn.bixin.sona.console.domain.dto.StreamRecordDTO;

import java.util.Objects;

/**
 * Created by zhichao.guo on 10/18/21 4:34 PM
 */
public class StreamRecordConverter {

    public static StreamRecordDTO convert2DTO(Stream stream) {
        StreamRecordDTO result = new StreamRecordDTO();
        if (Objects.isNull(stream)) {
            return result;
        }
        result.setUid(stream.getUid());
        result.setStatus(stream.getStatus());
        result.setReplayUrl(stream.getReplayUrl());
        result.setBeginTime(stream.getBeginTime());
        result.setEndTime(stream.getCloseTime());
        result.setSource(stream.getSource());
        result.setRoomId(stream.getRoomId());
        result.setId(stream.getId());
        return result;
    }

}