package cn.bixin.sona.server.room.domain.stream;


import cn.bixin.sona.server.room.domain.enums.PlatformEnum;
import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import org.apache.commons.lang3.StringUtils;

public class StreamContext {

    private String streamId;
    private String productCode;
    private StreamSupplierEnum streamSupplier;
    private String sonaSdkVersion;
    private PlatformEnum platform;
    private Long roomId;
    private Long uid;
    private String randomNumber;
    /**
     * 短产品标识
     */
    private String shortProductCode;

    public static StreamContext convert(String streamId) {
        if (StringUtils.isBlank(streamId)) {
            return null;
        }


        String[] streamArray = streamId.split("_");
        if (streamArray.length != 7) {
            return null;
        }

        StreamContext context = new StreamContext();
        context.setStreamId(streamId);
        context.setProductCode(ProductEnum.getByCode(streamArray[0]).name());
        context.setStreamSupplier(StreamSupplierEnum.getByDesc(streamArray[1]));
        context.setSonaSdkVersion(streamArray[2]);
        context.setPlatform(PlatformEnum.getPlatformByCode(Integer.valueOf(streamArray[3])));
        context.setRoomId(Long.valueOf(streamArray[4]));
        context.setUid(Long.parseLong(streamArray[5]));
        context.setRandomNumber(streamArray[6]);
        context.setShortProductCode(streamArray[0]);
        return context;
    }

    public static String convertStringUid(String streamId) {
        if (StringUtils.isBlank(streamId)) {
            return null;
        }
        String[] streamArray = streamId.split("_");
        if (streamArray.length != 7) {
            return null;
        }
        return streamArray[5];
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public StreamSupplierEnum getStreamSupplier() {
        return streamSupplier;
    }

    public void setStreamSupplier(StreamSupplierEnum streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    public String getSonaSdkVersion() {
        return sonaSdkVersion;
    }

    public void setSonaSdkVersion(String sonaSdkVersion) {
        this.sonaSdkVersion = sonaSdkVersion;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getShortProductCode() {
        return shortProductCode;
    }

    public void setShortProductCode(String shortProductCode) {
        this.shortProductCode = shortProductCode;
    }
}
