package cn.bixin.sona.console.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.bixin.sona.console.mapper.SwitchDetailMapper;
import cn.bixin.sona.console.domain.db.SwitchDetail;
import cn.bixin.sona.console.service.SwitchDetailService;

import java.util.List;

@Service
public class SwitchDetailServiceImpl implements SwitchDetailService{

    @Resource
    private SwitchDetailMapper switchDetailMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return switchDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SwitchDetail record) {
        return switchDetailMapper.insert(record);
    }

    @Override
    public int insertSelective(SwitchDetail record) {
        return switchDetailMapper.insertSelective(record);
    }

    @Override
    public SwitchDetail selectByPrimaryKey(Long id) {
        return switchDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SwitchDetail record) {
        return switchDetailMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SwitchDetail record) {
        return switchDetailMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SwitchDetail> queryDetailsByIds(List<Long> batchIds) {
        return switchDetailMapper.selectAllByBatchIdIn(batchIds);
    }

}
