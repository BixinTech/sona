package cn.bixin.sona.console.domain.converter;

import cn.bixin.sona.console.domain.dto.ChatroomStreamDTO;
import cn.bixin.sona.console.domain.dto.StreamRecordDTO;
import cn.bixin.sona.console.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.console.utils.DateUtils;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhichao.guo on 10/18/21 11:07 AM
 */
public class StreamConverter {

    public static List<ChatroomStreamDTO> convertChatroomStreamList(List<StreamRecordDTO> streams) {

        if (CollectionUtils.isEmpty(streams)) {
            return Collections.emptyList();
        }

        List<ChatroomStreamDTO> result = Lists.newArrayList();
        streams.forEach(stream -> {
            ChatroomStreamDTO streamDTO = new ChatroomStreamDTO();
            streamDTO.setRoomId(String.valueOf(stream.getRoomId()));
            streamDTO.setUid(String.valueOf(stream.getUid()));
            streamDTO.setStatus(stream.getStatus());
            streamDTO.setReplayUrl(stream.getReplayUrl());

            streamDTO.setBeginTime(DateUtils.getDateFormatDateTimeStr(stream.getBeginTime()));
            streamDTO.setEndTime(DateUtils.getDateFormatDateTimeStr(stream.getEndTime()));

            String source = null;
            StreamSupplierEnum supplierEnum = StreamSupplierEnum.getByCode(stream.getSource());
            if (supplierEnum != null) {
                source = supplierEnum.name();
            }
            streamDTO.setSource(source);

            result.add(streamDTO);
        });

        return result;
    }

}