package cn.bixin.sona.server;

import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.api.room.SonaRoomRemoteService;
import cn.bixin.sona.api.socket.RoomMessageRemoteService;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.RoomDetailInfoDTO;
import cn.bixin.sona.enums.UserTypeEnum;
import cn.bixin.sona.request.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SonaRoomTests {

    @Resource
    private SonaRoomRemoteService sonaRoomRemoteService;

    @Resource
    private RoomMessageRemoteService roomMessageRemoteService;

    @Resource
    private SonaRoomQueryRemoteService sonaRoomQueryRemoteService;

    @Test
    public void testCreateRoom() {
        //{"password":"","productCode":"CHATROOM","roomTitle":"房间标题","uid":"201721042504930029"}
        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("重阳");
        request.setProductCode("CHATROOM");
        request.setUid(201721042504930029L);
        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.createRoom(request);
        System.out.println(response.getCode());
    }

    @Test
    public void testOpenRoom() {
        OpenCloseRoomRequest request = new OpenCloseRoomRequest();
        request.setRoomId(4067934556221106432L);
        request.setUid(111);
        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.openRoom(request);
        System.out.println(response.getCode());
    }

    @Test
    public void testCloseRoom() {
        OpenCloseRoomRequest request = new OpenCloseRoomRequest();
        request.setRoomId(4098354394336812032L);
        request.setUid(192100665532790051L);
        Response<Boolean> response = sonaRoomRemoteService.closeRoom(request);
        System.out.println(response.getCode());
    }

    @Test
    public void testEnterRoom() {
        //{"uid":"201721042504930029","password":"","roomId":"4160815379634528256"}
        EnterRoomRequest request = new EnterRoomRequest();
        request.setRoomId(4160815379634528256L);
        request.setUid(201721042504930029L);
        request.setUserTypeEnum(UserTypeEnum.NORMAL);
        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.enterRoom(request);
        System.out.println(response.getCode());
    }

    @Test
    public void testLeaveRoom() {
        LeaveRoomRequest request = new LeaveRoomRequest();
        request.setRoomId(4067837473082337536L);
        request.setUid(123);
        Response<Boolean> response = sonaRoomRemoteService.leaveRoom(request);
        System.out.println(response.getCode());
    }

    @Test
    public void testRoomManage() {
        CreateRoomRequest createReq = new CreateRoomRequest();
        createReq.setName("测试111");
        createReq.setProductCode("TEST_GROUP");
        createReq.setUid(111);
        Response<RoomDetailInfoDTO> createResp = sonaRoomRemoteService.createRoom(createReq);
        Assertions.assertEquals(createResp.getCode(), String.valueOf(8000));

        EnterRoomRequest enterReq = new EnterRoomRequest();
        enterReq.setRoomId(createResp.getResult().getRoomId());
        enterReq.setUid(123);
        enterReq.setUserTypeEnum(UserTypeEnum.VIP);
        Response<RoomDetailInfoDTO> enterResp = sonaRoomRemoteService.enterRoom(enterReq);
        Assertions.assertEquals(enterResp.getCode(), String.valueOf(8000));

        LeaveRoomRequest leaveReq = new LeaveRoomRequest();
        leaveReq.setRoomId(createResp.getResult().getRoomId());
        leaveReq.setUid(123);
        Response<Boolean> leaveResp = sonaRoomRemoteService.leaveRoom(leaveReq);
        Assertions.assertEquals(leaveResp.getCode(), String.valueOf(8000));

        OpenCloseRoomRequest closeReq = new OpenCloseRoomRequest();
        closeReq.setRoomId(createResp.getResult().getRoomId());
        closeReq.setUid(111);
        Response<Boolean> closeResp = sonaRoomRemoteService.closeRoom(closeReq);
        Assertions.assertEquals(closeResp.getCode(), String.valueOf(8000));

        OpenCloseRoomRequest openReq = new OpenCloseRoomRequest();
        openReq.setRoomId(createResp.getResult().getRoomId());
        openReq.setUid(111);
        Response<RoomDetailInfoDTO> openResp = sonaRoomRemoteService.openRoom(openReq);
        Assertions.assertEquals(openResp.getCode(), String.valueOf(8000));
    }

    @Test
    public void testUpdatePassword() {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setRoomId(4069035967902148864L);
        request.setOperatorUid(111);
        request.setNewPassword("123");
        sonaRoomRemoteService.updatePassword(request);
    }

    @Test
    public void testMute() {
        OperateRequest req = new OperateRequest();
        req.setRoomId(4069035967902148864L);
        req.setMinutes(60);
        req.setOperatorUid(111);
        req.setTargetUid(123);
        sonaRoomRemoteService.muteUser(req);
        sonaRoomRemoteService.cancelMuteUser(req);
    }

    @Test
    public void testBlock() {
        OperateRequest req = new OperateRequest();
        req.setRoomId(4069035967902148864L);
        req.setMinutes(60);
        req.setOperatorUid(111);
        req.setTargetUid(123);
        sonaRoomRemoteService.blockUser(req);
        sonaRoomRemoteService.cancelBlockUser(req);
    }

    @Test
    public void testKick() {
        OperateRequest req = new OperateRequest();
        req.setRoomId(4069035967902148864L);
        req.setMinutes(60);
        req.setOperatorUid(111);
        req.setTargetUid(123);
        sonaRoomRemoteService.kickUser(req);
    }

    @Test
    public void testAdmin() {
        OperateRequest req = new OperateRequest();
        req.setRoomId(4069035967902148864L);
        req.setMinutes(60);
        req.setOperatorUid(111);
        req.setTargetUid(123);
        sonaRoomRemoteService.setAdmin(req);
        sonaRoomRemoteService.removeAdmin(req);
    }

    @Test
    public void testUpdateScore() {
//        sonaRoomRemoteService.updateChatroomUserScore(4069035967902148864L, 123L, 20);
//        sonaRoomRemoteService.logReport(new LogReportRequest());
        ChatroomMsgRequest request = new ChatroomMsgRequest();
        request.setRoomId("4099839498699627520");
        List<Long> list = new ArrayList<>();
        list.add(1234567L);
        request.setMessageId("123123123123");
        roomMessageRemoteService.batchKickOutChatroom(request, list);
    }

    @Test
    public void testOnlineRoom() {

        sonaRoomQueryRemoteService.getOnlineRoomList("0", 10);

    }
}
