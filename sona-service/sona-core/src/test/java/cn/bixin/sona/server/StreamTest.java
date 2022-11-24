package cn.bixin.sona.server;

import cn.bixin.sona.api.room.StreamRemoteService;
import cn.bixin.sona.request.ChangeStreamRequest;
import cn.bixin.sona.request.InitStreamRequest;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class StreamTest {

    @Resource
    private StreamRemoteService streamRemoteService;

    @Test
    public void testCreateStream() {
        InitStreamRequest request = new InitStreamRequest();
        request.setRoomId(4069035967902148864L);
        request.setUid(123);
        System.out.println(streamRemoteService.initStream(request).getResult());

        InitStreamRequest request1 = new InitStreamRequest();
        request1.setRoomId(4069090844816730368L);
        request1.setUid(123);
        System.out.println(streamRemoteService.initStream(request1).getResult());
    }

    @Test
    public void testGetStreamUrl() {
        System.out.println(streamRemoteService.getRoomStreamUrl(4069035967902148864L));
        System.out.println(streamRemoteService.getRoomStreamUrlBatch(Lists.newArrayList(4069035967902148864L, 4069090844816730368L)));
    }

    @Test
    public void testMuteStream() {
        System.out.println(streamRemoteService.muteStream(4069035967902148864L, Lists.newArrayList(123L)));
        System.out.println(streamRemoteService.cancelMuteStream(4069035967902148864L, Lists.newArrayList(123L)));
        System.out.println(streamRemoteService.muteRoomStream(4069035967902148864L));
    }

    @Test
    public void testChangeStream() {
        ChangeStreamRequest request = new ChangeStreamRequest();
        request.setRoomId(4069035967902148864L);
        request.setUid(1234L);
        request.setStreamId("T_T_1_3_10_4069035967902148864_1234_1234315");
        System.out.println(streamRemoteService.addStream(request));
        request.setRoomId(4069035967902148864L);
        request.setUid(1234L);
        request.setStreamId("T_T_1_3_10_4069035967902148864_1234_1234315");
        System.out.println(streamRemoteService.closeStream(request));
    }

    @Test
    public void testGetSupplierInfo() {
        //roomId=4098354394336812032&uid=201721042504930029
        long roomId = 4098354394336812032L;
        streamRemoteService.syncRoomConfigByRoomId(roomId);
    }

    @Test
    public void testGetAppInfo() {
        long roomId = 4098354394336812032L;
        long uid = 201721042504930029L;

        streamRemoteService.genUserSig(roomId, uid);

    }

}
