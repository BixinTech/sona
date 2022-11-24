package cn.bixin.sona.console.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.bixin.sona.console.domain.db.Chatroom;
import cn.bixin.sona.console.mapper.ChatroomMapper;
import cn.bixin.sona.console.service.ChatroomService;
@Service
public class ChatroomServiceImpl implements ChatroomService{

    @Resource
    private ChatroomMapper chatroomMapper;

    @Override
    public int deleteByPrimaryKey(Long pid) {
        return chatroomMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int insert(Chatroom record) {
        return chatroomMapper.insert(record);
    }

    @Override
    public int insertSelective(Chatroom record) {
        return chatroomMapper.insertSelective(record);
    }

    @Override
    public Chatroom selectByPrimaryKey(Long pid) {
        return chatroomMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int updateByPrimaryKeySelective(Chatroom record) {
        return chatroomMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Chatroom record) {
        return chatroomMapper.updateByPrimaryKey(record);
    }

}
