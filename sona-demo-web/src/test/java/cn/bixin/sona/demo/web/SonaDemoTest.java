package cn.bixin.sona.demo.web;

import javax.annotation.Resource;

import cn.bixin.sona.demo.web.controller.SonaDemoController;
import cn.bixin.sona.demo.web.request.MicRequest;
import cn.bixin.sona.demo.web.request.RewardRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SonaDemoTest {



    @Resource
    private SonaDemoController sonaDemoController;


    @Test
    public void testGetOnlineRoomList() {

        sonaDemoController.getOnlineRoomList();
    }

    @Test
    public void testRoomInfo() {

        sonaDemoController.getRoomInfo("4160815379634528256");
    }

    @Test
    public void testGiftList() {
        sonaDemoController.getGiftList();

    }

    @Test
    public void testReward() {
        RewardRequest rewardRequest = new RewardRequest();
        rewardRequest.setRoomId("4160815379634528256");
        rewardRequest.setFromUid("201721042504930029");
        rewardRequest.setToUid("");
        rewardRequest.setGiftId(1);

        //{"uid":"201721042504930029","giftId":1,"roomId":"4160815379634528256"}
        //2022-11-10 19:17:32.167 18530-18585 OkHttp                  cn.bixin.sona.demo                   D  --> END
        // POST (70-byte body)

        sonaDemoController.reward(rewardRequest);

    }

    @Test
    public void testMic() {
        MicRequest micRequest = new MicRequest();
        micRequest.setRoomId("4069035967902148864");
        micRequest.setIndex(0);
        micRequest.setUid("001001");
        micRequest.setOperate(1);


        sonaDemoController.doMic(micRequest);
        sonaDemoController.getRoomInfo("4069035967902148864");

    }

}
