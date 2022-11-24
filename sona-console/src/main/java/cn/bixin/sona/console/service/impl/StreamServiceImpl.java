package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.domain.converter.StreamRecordConverter;
import cn.bixin.sona.console.domain.db.Stream;
import cn.bixin.sona.console.domain.dto.StreamRecordDTO;
import cn.bixin.sona.console.mapper.StreamMapper;
import cn.bixin.sona.console.service.StreamService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StreamServiceImpl implements StreamService {

    @Resource
    private StreamMapper streamMapper;

    public List<StreamRecordDTO> queryStreamRecordInfo(StreamRecordDTO request){
        log.info("queryStreamRecordInfo param:{}", JSON.toJSONString(request));
        //根据用户信息查询流信息
        List<Stream> streams = streamMapper.selectStream(request.getStreamId(), request.getRoomId(), request.getUid()
                , request.getBeginTime(), request.getEndTime(), request.getAnchor(), request.getLimit());
        //过滤拉流模式
        return streams.stream().map(StreamRecordConverter::convert2DTO).collect(Collectors.toList());
    }

    public List<StreamRecordDTO> selectStreamCount(StreamRecordDTO request){
        log.info("selectStreamCount param:{}", JSON.toJSONString(request));
        List<Stream> streams = streamMapper.selectStreamInfo(request.getStreamId(), request.getRoomId(),
                request.getUid(), request.getBeginTime(), request.getEndTime());
        return streams.stream().map(StreamRecordConverter::convert2DTO).collect(Collectors.toList());
    }

}
