package cn.bixin.sona.demo.web.controller;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.bixin.sona.api.im.RouterRoomMessageService;
import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.demo.web.message.MessgeInfo;
import cn.bixin.sona.demo.web.message.MicMessage;
import cn.bixin.sona.demo.web.message.RewardMessage;
import cn.bixin.sona.demo.web.message.RewardMessage2;
import cn.bixin.sona.demo.web.request.MicRequest;
import cn.bixin.sona.demo.web.request.RewardRequest;
import cn.bixin.sona.demo.web.response.GiftInfoVO;
import cn.bixin.sona.demo.web.response.RoomInfoVO;
import cn.bixin.sona.demo.web.response.SeatInfoVO;
import cn.bixin.sona.dto.RoomDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo
 */
@Slf4j
@RestController
@RequestMapping("/sona/demo")
public class SonaDemoController {

    @DubboReference
    private SonaRoomQueryRemoteService sonaRoomQueryRemoteService;

    @DubboReference
    private RouterRoomMessageService routerRoomMessageService;

    private Map<String, List<SeatInfoVO>> roomSeatMap = Maps.newHashMap();
    private static Map<Integer, String> giftMap = Maps.newHashMap();

    static {
        giftMap.put(1, "海豚恋人");
        giftMap.put(2, "萌兔兔");
        giftMap.put(3, "原谅帽");
        giftMap.put(4, "海上城堡");


    }


    /**
     * 获取房间列表
     */
    @GetMapping(path = {"/room/list"})
    public Response<List<RoomDTO>> getOnlineRoomList() {
        Response<PageResult<RoomDTO>> response = sonaRoomQueryRemoteService.getOnlineRoomList("0", 100);
        if (!response.isSuccess()) {
            return Response.success(Collections.emptyList());
        }

        return Response.success(response.getResult().getList());
    }


    /**
     * 获取房间列表
     */
    @GetMapping(path = {"/room/info"})
    public Response<RoomInfoVO> getRoomInfo(@RequestParam String roomId) {

        Response<RoomDTO> response = sonaRoomQueryRemoteService.getRoom(Long.valueOf(roomId));
        if (!response.isSuccess() || response.getResult() == null) {
            return Response.success(null);
        }

        RoomDTO roomDTO = response.getResult();
        RoomInfoVO vo = new RoomInfoVO();
        vo.setName(roomDTO.getName());
        vo.setRoomId(roomDTO.getRoomId() + "");
        List<SeatInfoVO> seatInfoVOS = roomSeatMap.get(roomDTO.getRoomId() + "");
        if (CollectionUtils.isEmpty(seatInfoVOS)) {
            seatInfoVOS = Lists.newArrayList();
            initSeatList(seatInfoVOS);

            roomSeatMap.put(roomId, seatInfoVOS);
        }

        vo.setSeatList(seatInfoVOS);

        return Response.success(vo);
    }

    private void initSeatList(List<SeatInfoVO> seatInfoVOS) {

        SeatInfoVO s1 = new SeatInfoVO();
        s1.setUid("");
        s1.setIndex(0);
        seatInfoVOS.add(s1);

        SeatInfoVO s2 = new SeatInfoVO();
        s2.setIndex(1);
        s2.setUid("");
        seatInfoVOS.add(s2);

        SeatInfoVO s3 = new SeatInfoVO();
        s3.setIndex(2);
        s3.setUid("");
        seatInfoVOS.add(s3);

        SeatInfoVO s4 = new SeatInfoVO();
        s4.setUid("");
        s4.setIndex(3);
        seatInfoVOS.add(s4);

        SeatInfoVO s5 = new SeatInfoVO();
        s5.setUid("");
        s5.setIndex(4);
        seatInfoVOS.add(s5);

        SeatInfoVO s6 = new SeatInfoVO();
        s6.setUid("");
        s6.setIndex(5);
        seatInfoVOS.add(s6);
    }


    /**
     * 获取礼物列表
     */
    @GetMapping(path = {"/gift/list"})
    public Response<List<GiftInfoVO>> getGiftList() {

        List<GiftInfoVO> list = Lists.newArrayList();
        GiftInfoVO gift1 = new GiftInfoVO();
        gift1.setGiftId(1);
        gift1.setGiftName("海豚恋人");
        gift1.setPrice(5200);

        list.add(gift1);

        GiftInfoVO gift2 = new GiftInfoVO();
        gift2.setGiftId(2);
        gift2.setGiftName("萌兔兔");
        gift2.setPrice(100);

        list.add(gift2);

        GiftInfoVO gift3 = new GiftInfoVO();
        gift3.setGiftId(3);
        gift3.setGiftName("原谅帽");
        gift3.setPrice(200);

        list.add(gift3);

        GiftInfoVO gift4 = new GiftInfoVO();
        gift4.setGiftId(4);
        gift4.setGiftName("海上城堡");
        gift4.setPrice(400);

        list.add(gift4);


        return Response.success(list);
    }


    /**
     * 礼物打赏
     */
    @PostMapping(path = {"/gift/reward"})
    public Response<Boolean> reward(@RequestBody RewardRequest rewardRequest) {
        RoomMessageRequest request = new RoomMessageRequest();
        request.setRoomId(Long.valueOf(rewardRequest.getRoomId()));
        request.setPriority(PriorityEnum.HIGH);
        request.setProductCode("CHATROOM");
        request.setMsgType("REWARD");

        RewardMessage rewardMessage = new RewardMessage();
        rewardMessage.setGiftId(rewardRequest.getGiftId());
        rewardMessage.setRoomId(rewardRequest.getRoomId());
        rewardMessage.setUid(rewardRequest.getToUid());

        RewardMessage2 rewardMessage2 = new RewardMessage2();
        rewardMessage2.setGiftId(rewardRequest.getGiftId());
        rewardMessage2.setFromUid(rewardRequest.getFromUid());
        rewardMessage2.setToUid(rewardRequest.getToUid());
        rewardMessage2.setRoomId(rewardRequest.getRoomId());
        rewardMessage2.setGiftName(giftMap.get(rewardRequest.getGiftId()));

        MessgeInfo messageInfo = new MessgeInfo();
        messageInfo.setData(JSON.toJSONString(rewardMessage));
        messageInfo.setMsgType(303);

        request.setContent(JSON.toJSONString(messageInfo));

        routerRoomMessageService.sendChatRoomMessage(request);

        MessgeInfo messageInfo2 = new MessgeInfo();
        messageInfo2.setData(JSON.toJSONString(rewardMessage2));
        messageInfo2.setMsgType(304);

        request.setContent(JSON.toJSONString(messageInfo2));

        routerRoomMessageService.sendChatRoomMessage(request);

        return Response.success(true);
    }

    /**
     * 上下麦
     */
    @PostMapping(path = {"/room/mic"})
    public Response<Boolean> doMic(@RequestBody MicRequest micRequest) {
        RoomMessageRequest request = new RoomMessageRequest();
        request.setRoomId(Long.valueOf(micRequest.getRoomId()));
        request.setPriority(PriorityEnum.HIGH);
        request.setProductCode("CHATROOM");
        request.setMsgType("MIC");

        MicMessage micMessage = new MicMessage();
        micMessage.setUid(micRequest.getUid());
        micMessage.setRoomId(micRequest.getRoomId());
        micMessage.setOperate(micRequest.getOperate());
        micMessage.setIndex(micRequest.getIndex());

        MessgeInfo messgeInfo = new MessgeInfo();
        messgeInfo.setData(JSON.toJSONString(micMessage));
        messgeInfo.setMsgType(203);

        request.setContent(JSON.toJSONString(messgeInfo));

        routerRoomMessageService.sendChatRoomMessage(request);

        String roomId = micRequest.getRoomId();
        if (micMessage.getOperate() == 1) {
            List<SeatInfoVO> seatList = roomSeatMap.get(roomId);
            if (CollectionUtils.isEmpty(seatList)) {
                seatList = Lists.newArrayList();
            }

            SeatInfoVO seatInfo = seatList.stream().filter(each -> each.getIndex() == micRequest.getIndex()).findFirst().orElse(null);
            if (seatInfo == null) {
                seatInfo = new SeatInfoVO();
                seatInfo.setIndex(micMessage.getIndex());
                seatInfo.setUid(micRequest.getUid());

                seatList.add(seatInfo);
            } else {
                seatInfo.setUid(micRequest.getUid());
            }

            roomSeatMap.put(roomId, seatList);
        } else {
            List<SeatInfoVO> seatList = roomSeatMap.get(roomId);
            if (!CollectionUtils.isEmpty(seatList)) {
                SeatInfoVO seatInfo = seatList.stream().filter(each -> each.getIndex() == micRequest.getIndex()).findFirst().orElse(null);
                if (seatInfo != null) {
                    seatInfo.setUid("");
                }
            }

            roomSeatMap.put(roomId, seatList);
        }

        return Response.success(true);
    }
}
