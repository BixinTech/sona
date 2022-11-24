package cn.bixin.sona.console.mapper;

import cn.bixin.sona.console.domain.db.Chatroom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatroomMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(Chatroom record);

    int insertSelective(Chatroom record);

    Chatroom selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(Chatroom record);

    int updateByPrimaryKey(Chatroom record);
}