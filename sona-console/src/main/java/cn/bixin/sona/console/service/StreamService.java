package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.dto.StreamRecordDTO;

import java.util.List;

public interface StreamService {

    List<StreamRecordDTO> queryStreamRecordInfo(StreamRecordDTO request);

    List<StreamRecordDTO> selectStreamCount(StreamRecordDTO request);

}
