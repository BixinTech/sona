package cn.bixin.sona.server.room.domain.stream;

/**
 * 获得用户签名的输入参数
 */
public class UserSginInputParam {
    /**
     * 流提供商
     */
    private String streamSupplier;
    /**
     * 是否需要录播
     */
    private boolean needReplay;

    /**
     * uid
     */
    private long uid;

    /**
     *
     */
    private boolean isGuest;

    /**
     * 产品code
     */
    private String productCode;

    public String getStreamSupplier() {
        return streamSupplier;
    }

    public void setStreamSupplier(String streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    public boolean isNeedReplay() {
        return needReplay;
    }

    public void setNeedReplay(boolean needReplay) {
        this.needReplay = needReplay;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
