package cn.bixin.sona.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

public class ProductConfigInfoDTO implements Serializable {

    @Description("产品code")
    private String productCode;
    @Description("产品code别名")
    private String productCodeAlias;
    @Description("im配置信息")
    private ImConfigInfoDTO imConfig;
    @Description("流配置信息")
    private StreamConfigInfoDTO streamConfig;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCodeAlias() {
        return productCodeAlias;
    }

    public void setProductCodeAlias(String productCodeAlias) {
        this.productCodeAlias = productCodeAlias;
    }

    public ImConfigInfoDTO getImConfig() {
        return imConfig;
    }

    public void setImConfig(ImConfigInfoDTO imConfig) {
        this.imConfig = imConfig;
    }

    public StreamConfigInfoDTO getStreamConfig() {
        return streamConfig;
    }

    public void setStreamConfig(StreamConfigInfoDTO streamConfig) {
        this.streamConfig = streamConfig;
    }
}
