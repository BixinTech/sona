package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.Stream;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface StreamMapper {

    void initStream(@Param("streamId") String streamId, @Param("roomId") long roomId,
                    @Param("uid") long uid, @Param("productCode") String productCode,
                    @Param("source") int source);

    Stream selectByStreamId(@Param("streamId") String streamId);

    void addStream(Stream stream);

    List<Stream> selectLivingStreamByRoomIdAndUids(@Param("roomId") long roomId, @Param("uids") List<Long> uids);

    List<Stream> getRoomLivingSteamList(@Param("roomId") long roomId);

    void updateCreateStream(@Param("streamId") String streamId, @Param("rtmpUrls") String rtmpUrls,
                            @Param("hlsUrls") String hlsUrls, @Param("hdlUrls") String hdlUrls, @Param("picUrls") String picUrls);

    void openStream(@Param("streamId") String streamId);

    void closeStream(@Param("streamId") String streamId, @Param("closeType") int closeType,
                     @Param("errMsg") String errMsg);

    void closeStreams(@Param("streamIds") List<String> streamIds);

    void handleReplay(@Param("streamId") String streamId, @Param("replayUrl") String replayUrl,
                      @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    void insert(Stream stream);

}
