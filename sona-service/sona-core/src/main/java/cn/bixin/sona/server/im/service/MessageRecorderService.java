package cn.bixin.sona.server.im.service;


import cn.bixin.sona.common.hbase.HBaseRepository;
import cn.bixin.sona.server.im.dto.RoomMessageDTO;
import cn.bixin.sona.server.im.utils.HbaseUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageRecorderService {

    @Resource
    HBaseRepository hBaseRepository;

    /**
     * 消息存储
     * @param messageDTO
     */
    public void saveRoomMessage(@NotNull RoomMessageDTO messageDTO) throws IOException {
        Put put = createRoomMessagePut(messageDTO);
        hBaseRepository.put(HbaseUtil.roomMessageTable(), put);
    }

    /**
     * 查询
     * @param roomId
     * @param minMessageId
     * @param maxMessageId
     * @param limit
     * @param reversed
     * @return
     * @throws IOException
     */
    public List<RoomMessageDTO> queryRoomMessage(Long uid, long roomId, String minMessageId, String maxMessageId, int limit, boolean reversed) throws IOException {
        byte[] startRowKey = HbaseUtil.genRoomMessageKey(roomId, reversed ? maxMessageId : minMessageId);
        byte[] endRowKey = HbaseUtil.genRoomMessageKey(roomId, reversed ? minMessageId : maxMessageId);
        Scan scan = new Scan()
                .withStartRow(startRowKey).withStopRow(endRowKey)
                .setReversed(reversed)
                .setLimit(limit);
        if(uid != null && uid > 0){
            scan.setFilter(new SingleColumnValueFilter(HbaseUtil.getRoomMessageFamily(), Bytes.toBytes("uid"), CompareOperator.EQUAL, Bytes.toBytes(uid)));
        }
        List<RoomMessageDTO> resList = new ArrayList<>();
        hBaseRepository.scan(HbaseUtil.roomMessageTable(), scan, result -> {
            resList.add(createRoomMessageDTO(result));
        });
        return resList;
    }

    private RoomMessageDTO createRoomMessageDTO(Result result) {
        RoomMessageDTO message = new RoomMessageDTO();
        byte[] family = HbaseUtil.getRoomMessageFamily();
        message.setUid(Bytes.toLong(result.getValue(family, Bytes.toBytes("uid"))));
        message.setRoomId(Bytes.toLong(result.getValue(family, Bytes.toBytes("roomId"))));
        message.setSendTime(Bytes.toLong(result.getValue(family, Bytes.toBytes("sendTime"))));
        message.setContent(Bytes.toString(result.getValue(family, Bytes.toBytes("content"))));
        message.setProductCode(Bytes.toString(result.getValue(family, Bytes.toBytes("productCode"))));
        message.setMessageId(Bytes.toString(result.getValue(family, Bytes.toBytes("messageId"))));
        return message;
    }

    private Put createRoomMessagePut(RoomMessageDTO messageDTO) throws IOException {
        byte[] family = HbaseUtil.getRoomMessageFamily();
        byte[] rowKey = HbaseUtil.genRoomMessageKey(messageDTO.getRoomId(), messageDTO.getMessageId());
        Put put = new Put(rowKey);
        put.addColumn(family, Bytes.toBytes("uid"), Bytes.toBytes(messageDTO.getUid()));
        put.addColumn(family, Bytes.toBytes("roomId"), Bytes.toBytes(messageDTO.getRoomId()));
        put.addColumn(family, Bytes.toBytes("sendTime"), Bytes.toBytes(messageDTO.getSendTime()));
        put.addColumn(family, Bytes.toBytes("content"), Bytes.toBytes(messageDTO.getContent()));
        put.addColumn(family, Bytes.toBytes("productCode"), Bytes.toBytes(messageDTO.getProductCode()));
        put.addColumn(family, Bytes.toBytes("messageId"), Bytes.toBytes(messageDTO.getMessageId()));
        return put;
    }

}
