package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.Chatroom;
public interface ChatroomService{


    int deleteByPrimaryKey(Long pid);

    int insert(Chatroom record);

    int insertSelective(Chatroom record);

    Chatroom selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(Chatroom record);

    int updateByPrimaryKey(Chatroom record);

}
