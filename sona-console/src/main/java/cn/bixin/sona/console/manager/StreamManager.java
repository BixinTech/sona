package cn.bixin.sona.console.manager;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.console.domain.converter.StreamConverter;
import cn.bixin.sona.console.domain.dto.ChatroomStreamDTO;
import cn.bixin.sona.console.domain.dto.StreamRecordDTO;
import cn.bixin.sona.console.domain.req.StreamQueryRequest;
import cn.bixin.sona.console.exception.ExceptionCode;
import cn.bixin.sona.console.service.StreamService;
import cn.bixin.sona.console.utils.DateUtils;
import com.google.common.collect.Sets;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class StreamManager {

    @Resource
    private StreamService streamService;

    public PageResult<ChatroomStreamDTO> queryChatroomStreamRecord(StreamQueryRequest request) {
        if (request == null || (StringUtils.isBlank(request.getUid()) && StringUtils.isBlank(request.getRoomId()) && StringUtils.isBlank(request.getStreamId()))) {
            throw new YppRunTimeException(ExceptionCode.NULL_PARAM);
        }
        return getStreamsPageResult(request);
    }

    public PageResult<ChatroomStreamDTO> queryChatroomMixStream(StreamQueryRequest request) {
        if (request == null || (StringUtils.isBlank(request.getRoomId()) && StringUtils.isBlank(request.getStartTime())) && StringUtils.isBlank(request.getEndTime())) {
            throw new YppRunTimeException(ExceptionCode.NULL_PARAM);
        }

        return getStreamsPageResult(request);
    }

    public PageResult<ChatroomStreamDTO> queryChatroomStream(StreamQueryRequest request) {
        if (request == null || StringUtils.isBlank(request.getStreamId())) {
            throw new YppRunTimeException(ExceptionCode.NULL_PARAM);
        }
        return getStreamsPageResult(request);
    }

    private PageResult<ChatroomStreamDTO> getStreamsPageResult(StreamQueryRequest request) {
        StreamRecordDTO param = new StreamRecordDTO();
        if (StringUtils.isNotEmpty(request.getUid())) {
            param.setUid(Long.parseLong(request.getUid()));
        }
        if (StringUtils.isNotEmpty(request.getRoomId())) {
            param.setRoomId(Long.parseLong(request.getRoomId()));
        }
        if (StringUtils.isNotEmpty(request.getAnchor())) {
            param.setAnchor(Long.parseLong(request.getAnchor()));
        }
        param.setStreamId(request.getStreamId());
        param.setLimit(request.getLimit());
        param.setBeginTime(DateUtils.formatToDate(request.getStartTime()));
        param.setEndTime(DateUtils.formatToDate(request.getEndTime()));
        List<StreamRecordDTO> streamRecordDTOList = streamService.queryStreamRecordInfo(param);
        List<StreamRecordDTO> recordDTOList = streamService.selectStreamCount(param);
        return fillChatroomStream(streamRecordDTOList, recordDTOList);
    }

    private PageResult<ChatroomStreamDTO> fillChatroomStream(List<StreamRecordDTO> streams, List<StreamRecordDTO> sums) {
        if (CollectionUtils.isEmpty(streams)) {
            return PageResult.newPageResult(Collections.emptyList(), true);
        }

        List<ChatroomStreamDTO> chatroomStreamDTOS = StreamConverter.convertChatroomStreamList(streams);

        PageResult<ChatroomStreamDTO> pageResult = new PageResult<>();
        pageResult.setCount((long) sums.size());
        pageResult.setEnd(false);
        pageResult.setList(chatroomStreamDTOS);
        pageResult.setAnchor(CollectionUtils.isEmpty(streams) ? "" : String.valueOf(streams.get(streams.size() - 1).getId()));
        return pageResult;
    }
}
