package cn.bixin.sona.web;

import javax.annotation.Resource;

import cn.bixin.sona.request.OpenCloseRoomRequest;
import cn.bixin.sona.web.controller.SonaController;
import cn.bixin.sona.web.controller.StreamController;
import cn.bixin.sona.web.request.BlockUserRequest;
import cn.bixin.sona.web.request.CloseRoomRequest;
import cn.bixin.sona.web.request.CreateRoomRequest;
import cn.bixin.sona.web.request.EnterRequest;
import cn.bixin.sona.web.request.LeaveRoomRequest;
import cn.bixin.sona.web.request.MuteUserRequest;
import cn.bixin.sona.web.request.OpenRoomRequest;
import cn.bixin.sona.web.request.UpdatePasswordRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SonaRoomTest {

	@Resource
	private SonaController sonaController;

	@Resource
	private StreamController streamController;

	@Test
	public void testCreateRoom() {

		CreateRoomRequest request = new CreateRoomRequest();
		request.setRoomTitle("test-room1");
		request.setProductCode("TEST");
		request.setPassword("");
		request.setUid("201721042504930029");
		request.setExtMap(null);
		sonaController.createRoom(request);
	}


	@Test
	public void testEnterRoom() {
		//{"uid":"201721042504930029","password":"","roomId":"4160815379634528256"}
		EnterRequest request = new EnterRequest();
		String roomId = "4160815379634528256";

		request.setRoomId(roomId);
		request.setUid("201721042504930029");
		//request.setPassword("123456");

		sonaController.enterRoom(request);

		sonaController.getRoomMemberCount(roomId);
		sonaController.getRoomMemberList(roomId, "", 10);
	}

	@Test
	public void testLeaveRoom() {

		LeaveRoomRequest request = new LeaveRoomRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("192100665532790051");

		sonaController.leaveRoom(request);
	}

	@Test
	public void testCloseRoom() {
		CloseRoomRequest request = new CloseRoomRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");
		//admin: 201721042504930029
		sonaController.closeRoom(request);
	}

	@Test
	public void testOpenRoom() {

		OpenRoomRequest request = new OpenRoomRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");

		sonaController.openRoom(request);
	}

	@Test
	public void testMute() {

		MuteUserRequest request = new MuteUserRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");
		request.setTargetUid("201721042504930000");
		request.setMinute(100);


		sonaController.muteUser(request);
		sonaController.cancelMuteUser(request);

	}

	@Test
	public void testBlock() {
		BlockUserRequest request = new BlockUserRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");
		request.setTargetUid("201721042504930000");
		request.setReason("aaa");

		sonaController.blockUser(request);
		sonaController.cancelBlockUser(request);

	}

	@Test
	public void testAdmin() {
		BlockUserRequest request = new BlockUserRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");
		request.setTargetUid("201721042504930000");
		request.setReason("aaa");

		sonaController.setAdmin(request);
		sonaController.cancelSetAdmin(request);
	}


	@Test
	public void testPwd() {
		UpdatePasswordRequest request = new UpdatePasswordRequest();
		request.setRoomId("4098354394336812032");
		request.setUid("201721042504930029");
		request.setOldPassword("");
		request.setNewPassword("123456");


		sonaController.updatePassword(request);
	}


	@Test
	public void testSupplierInfo() {
		//roomId=4098354394336812032&uid=201721042504930029
		String roomId = "4098354394336812032";
		String uid = "201721042504930029";

		streamController.syncSupplierConfig(roomId, uid);
	}

	@Test
	public void testAppInfo() {
		String roomId = "4098354394336812032";
		String uid = "201721042504930029";
		streamController.genUserSig(roomId, uid);
	}

}
