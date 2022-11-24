package cn.bixin.sona.server.hbase;

import cn.bixin.sona.api.im.request.MessageQueryRequest;
import cn.bixin.sona.common.hbase.HBaseRepository;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.server.im.controller.MessageQueryController;
import cn.bixin.sona.server.im.dto.RoomMessageDTO;
import cn.bixin.sona.server.im.service.MessageRecorderService;
import cn.bixin.sona.server.im.utils.HbaseUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HbaseTest {

    @Resource
    IdGenerator idGenerator;
    @Resource
    HBaseRepository hBaseRepository;
    @Resource
    MessageRecorderService messageRecorderService;
    @Resource
    MessageQueryController messageQueryController;


    @Test
    public void localDebug(){
        while(true){
            doDebug();
        }
    }

    private void doDebug(){
        try {
//            this.deleteTable();
//            this.tableCreate();
//            this.saveRoomMessage();
//            this.queryRoomMessage();
            queryMessageInfo();
        } catch (Exception e){
            e.printStackTrace();
        } catch (Error er){
            er.printStackTrace();
        }
    }

    private void queryMessageInfo(){
        MessageQueryRequest request = new MessageQueryRequest();
        request.setUid(100000000L);
        request.setRoomId(100000000L);
        Object obj = messageQueryController.queryMessageInfo(request);
        System.out.println(JSON.toJSONString(obj));
    }

    private void queryRoomMessage() throws IOException {
        List<RoomMessageDTO> list = messageRecorderService.queryRoomMessage(null, 100000000L, "0", String.valueOf(Long.MAX_VALUE), 100, true);
        System.out.println(JSON.toJSONString(list));
    }

    /**
     * 消息保存
     * @throws IOException
     */
    private void saveRoomMessage() throws IOException {
        long uid0 = 100000000L;
        long roomId0 = 100000000L;
        for (int i = 0; i < 100; i++) {
            long uid = uid0 + RandomUtils.nextInt(10);
            long roomId = roomId0 + RandomUtils.nextInt(3);
            RoomMessageDTO msg = this.createRoomMessageDTO(uid, roomId, "msg:" + uid + ":" + roomId);
            messageRecorderService.saveRoomMessage(msg);
        }
    }

    /** 表创建 */
    private void tableCreate() throws IOException {
        String tbName = HbaseUtil.roomMessageTable();
        Connection connection = hBaseRepository.getConnection();
        Admin admin = connection.getAdmin();
        boolean isExists = admin.tableExists(TableName.valueOf(tbName));
        if (isExists) {
            System.out.println(String.format("%s exists, create table ignore", tbName));
            return;
        }
        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tbName));
        tableDesc.addFamily(new HColumnDescriptor(HbaseUtil.getRoomMessageFamily()));
        admin.createTable(tableDesc);
        System.out.println(String.format("create finished, tableName:%s", tbName));
    }

    private void deleteTable() throws IOException {
        String tbName = HbaseUtil.roomMessageTable();
        Connection connection = hBaseRepository.getConnection();
        Admin admin = connection.getAdmin();
        admin.disableTable(TableName.valueOf(tbName));
        admin.deleteTable(TableName.valueOf(tbName));
        System.out.println(String.format("delete table finished, tableName:%s", tbName));
    }

    private RoomMessageDTO createRoomMessageDTO(long uid, long roomId, String content){
        RoomMessageDTO msg = new RoomMessageDTO();
        msg.setUid(uid);
        msg.setRoomId(roomId);
        msg.setSendTime(System.currentTimeMillis());
        msg.setContent(content);
        msg.setProductCode("CHATROOM");
        msg.setMessageId(idGenerator.strId());
        return msg;
    }
}
