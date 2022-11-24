package cn.bixin.sona.console.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.bixin.sona.console.mapper.SwitchBatchMapper;
import cn.bixin.sona.console.domain.db.SwitchBatch;
import cn.bixin.sona.console.service.SwitchBatchService;

import java.util.List;

@Service
public class SwitchBatchServiceImpl implements SwitchBatchService{

    @Resource
    private SwitchBatchMapper switchBatchMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return switchBatchMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SwitchBatch record) {
        return switchBatchMapper.insert(record);
    }

    @Override
    public int insertSelective(SwitchBatch record) {
        return switchBatchMapper.insertSelective(record);
    }

    @Override
    public SwitchBatch selectByPrimaryKey(Long id) {
        return switchBatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SwitchBatch record) {
        return switchBatchMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SwitchBatch record) {
        return switchBatchMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SwitchBatch> queryByParam(int start, int pageSize) {
        return switchBatchMapper.queryByParam(start, pageSize);
    }

    @Override
    public Integer queryTotalCount() {
        return switchBatchMapper.queryTotalCount();
    }

}
