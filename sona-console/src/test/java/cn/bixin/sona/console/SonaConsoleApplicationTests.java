package cn.bixin.sona.console;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.domain.req.MercuryEventLogQuery;
import cn.bixin.sona.console.domain.req.StreamQueryRequest;
import cn.bixin.sona.console.facade.StreamServiceFacade;
import cn.bixin.sona.console.manager.StreamManager;
import cn.bixin.sona.console.mapper.ChatroomMapper;
import cn.bixin.sona.console.repository.MercuryEventLogRepository;
import cn.bixin.sona.console.repository.MercuryReportLogRepository;
import cn.bixin.sona.console.repository.RoomImMsgLogRepository;
import cn.bixin.sona.console.service.MercuryEventLogService;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class SonaConsoleApplicationTests {
	@Resource
	private ChatroomMapper chatroomMapper;
	@Resource
	private MercuryEventLogService mercuryEventLogService;
	@Resource
	private MercuryEventLogRepository mercuryEventLogRepository;
	@Resource
	private MercuryReportLogRepository mercuryReportLogRepository;
	@Resource
	private RoomImMsgLogRepository roomImMsgLogRepository;

	@Resource
	private StreamManager streamManager;
	@Resource
	private StreamServiceFacade facade;

	@Test
	void contextLoads() {
/*		mercuryEventLogRepository.deleteAll();
		mercuryReportLogRepository.deleteAll();
		roomImMsgLogRepository.deleteAll();*/
		facade.switchAudioSupplierRoom(4069035967902148864L, 5);
	}

	@Test
	void selectByPrimaryKey() {
		chatroomMapper.selectByPrimaryKey(2L);
	}

	@Test
	void test00(){

		MercuryEventLog mercuryEventLog = new MercuryEventLog();
		mercuryEventLog.setUid("2013123");
		mercuryEventLog.setServer("");
		mercuryEventLog.setAddr("");
		mercuryEventLog.setType("");
		mercuryEventLog.setDevice("");
		mercuryEventLog.setEvent("");
		mercuryEventLog.setContent("你好 hello world");
		mercuryEventLog.setCmd("1");
		mercuryEventLog.setHeader("");
		mercuryEventLog.setSendTime(System.currentTimeMillis());
		mercuryEventLogService.save(mercuryEventLog);

		MercuryReportLog mercuryReportLog = new MercuryReportLog();
		mercuryReportLog.setSendTime(System.currentTimeMillis());
		mercuryReportLog.setCommon("");
		mercuryReportLog.setDesc("");
		mercuryReportLog.setDetails("");
		mercuryReportLog.setIp("");
		mercuryReportLog.setModel("");
		mercuryReportLog.setNetwork("");
		mercuryReportLog.setOsVer("");
		mercuryReportLog.setPlatform("");
		mercuryReportLog.setType("");
		mercuryReportLog.setUid("");
		mercuryReportLogRepository.save(mercuryReportLog);

		RoomImMsgLog roomImMsgLog = new RoomImMsgLog();
		roomImMsgLog.setMessageId("");
		roomImMsgLog.setRoomId("");
		roomImMsgLog.setMsgType("");
		roomImMsgLog.setProductCode("");
		roomImMsgLog.setPriority("");
		roomImMsgLog.setUid("");
		roomImMsgLog.setContent("");
		roomImMsgLog.setToUid("");
		roomImMsgLog.setSendTime(System.currentTimeMillis());
		roomImMsgLogRepository.save(roomImMsgLog);

	}

	@Test
	void test0(){
		mercuryEventLogRepository.deleteAll();
		MercuryEventLog mercuryEventLog = new MercuryEventLog();
		mercuryEventLog.setUid("2013123");
		mercuryEventLog.setServer("");
		mercuryEventLog.setAddr("");
		mercuryEventLog.setType("");
		mercuryEventLog.setDevice("");
		mercuryEventLog.setEvent("");
		mercuryEventLog.setContent("你好 hello world");
		mercuryEventLog.setCmd("1");
		mercuryEventLog.setHeader("");
		mercuryEventLog.setSendTime(System.currentTimeMillis());
		mercuryEventLogService.save(mercuryEventLog);

		MercuryEventLogQuery query = new MercuryEventLogQuery();
		query.setFromTime(System.currentTimeMillis() - 10000);
		query.setToTime(System.currentTimeMillis() + 10000);
		query.setContent("hell");
		query.setUid("2013123");
		Page<MercuryEventLog> mercuryEventLogs = mercuryEventLogService.pageQuery(query);
		System.out.println(JSON.toJSONString(mercuryEventLogs));
	}

	@Test
	void test1() {
		StreamQueryRequest request = new StreamQueryRequest();
		request.setRoomId("4069090844816730368");
		request.setUid("123");
		request.setStartTime("2022-09-20 18:31:27");
		request.setEndTime("2022-09-20 20:31:27");
		request.setLimit(20);

		System.out.println(streamManager.queryChatroomStreamRecord(request));
	}

	@Test
	void test2() {
		StreamQueryRequest request = new StreamQueryRequest();
		request.setRoomId("4069090844816730368");
		request.setStartTime("2022-09-20 18:31:27");
		request.setEndTime("2022-09-20 20:31:27");
		request.setLimit(20);

		System.out.println(streamManager.queryChatroomMixStream(request));
	}

	@Test
	void test3() {
		StreamQueryRequest request = new StreamQueryRequest();
		request.setStreamId("STGZ4069090844816730368Z123Z4086981608179537920");
		request.setStartTime("2022-09-20 18:31:27");
		request.setEndTime("2022-09-20 20:31:27");
		request.setLimit(20);

		System.out.println(streamManager.queryChatroomStream(request));
	}
}
