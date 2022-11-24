package cn.bixin.sona.server;

import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.common.dto.Response;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
public class SonaRoomQueryTest {

    @Resource
    private SonaRoomQueryRemoteService sonaRoomQueryRemoteService;

    @Test
    public void testQuery() {
//        System.out.println(sonaRoomQueryRemoteService.getRoom(4069035967902148864L).getResult().getName());
//        System.out.println(sonaRoomQueryRemoteService.getRoom(4069090844816730368L).getResult().getName());
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.getRoomBatch(Lists.newArrayList(4069035967902148864L, 4069090844816730368L)).getResult()));
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.getRoomConfig(4069035967902148864L).getResult()));
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.getRoomConfig(4069090844816730368L).getResult()));
    }

    @Test
    public void testBatch() {
        Response<Map<Long, Long>> resp = sonaRoomQueryRemoteService.batchGetRoomMemberCount(Lists.newArrayList( 4069090844816730368L, 4067934556221106432L));
        System.out.println(resp.getResult().size());

        resp = sonaRoomQueryRemoteService.batchGetRoomMemberCount(Lists.newArrayList(4069035967902148864L));
        System.out.println(resp.getResult().size());
    }

    @Test
    public void testMember() {
        System.out.println(sonaRoomQueryRemoteService.getRoomMemberCount(4069035967902148864L).getResult());
        System.out.println(sonaRoomQueryRemoteService.getRoomMemberCount(4069090844816730368L).getResult());
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.getRoomMemberList(4069035967902148864L, "0", 1).getResult()));
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.getRoomMemberList(4069090844816730368L, "0", 1)));
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.isUserInRoom(4069035967902148864L, Lists.newArrayList(123L)).getResult()));
        System.out.println(JSON.toJSONString(sonaRoomQueryRemoteService.isUserInRoom(4069090844816730368L, Lists.newArrayList(123L)).getResult()));
    }
}
