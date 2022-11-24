package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.SwitchBatch;

import java.util.List;

public interface SwitchBatchService{


    int deleteByPrimaryKey(Long id);

    int insert(SwitchBatch record);

    int insertSelective(SwitchBatch record);

    SwitchBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SwitchBatch record);

    int updateByPrimaryKey(SwitchBatch record);

    List<SwitchBatch> queryByParam(int start, int pageSize);

    Integer queryTotalCount();
}
