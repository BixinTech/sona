package cn.bixin.sona.web.response;


import java.io.Serializable;

public class ProductConfigInfoVO implements Serializable {
    private static final long serialVersionUID = 4618024577060376503L;

    private String productCode;
    private String productCodeAlias = "C";
    private ImConfigInfoVO imConfig;
    private StreamConfigInfoVO streamConfig;

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

    public ImConfigInfoVO getImConfig() {
        return imConfig;
    }

    public void setImConfig(ImConfigInfoVO imConfig) {
        this.imConfig = imConfig;
    }

    public StreamConfigInfoVO getStreamConfig() {
        return streamConfig;
    }

    public void setStreamConfig(StreamConfigInfoVO streamConfig) {
        this.streamConfig = streamConfig;
    }

}
