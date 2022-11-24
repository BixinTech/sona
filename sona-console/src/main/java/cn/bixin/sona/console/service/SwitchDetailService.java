package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.SwitchDetail;

import java.util.List;

public interface SwitchDetailService {


    int deleteByPrimaryKey(Long id);

    int insert(SwitchDetail record);

    int insertSelective(SwitchDetail record);

    SwitchDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SwitchDetail record);

    int updateByPrimaryKey(SwitchDetail record);

    List<SwitchDetail> queryDetailsByIds(List<Long> batchIds);
}

