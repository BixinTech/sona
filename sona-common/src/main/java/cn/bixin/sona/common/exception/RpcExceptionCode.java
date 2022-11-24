package cn.bixin.sona.common.exception;

import cn.bixin.sona.common.dto.Code;

/**
 * @author qinwei
 */
public enum RpcExceptionCode implements Code {

    UNKNOWN_EXCEPTION("9999", "未知异常"),

    NETWORK_EXCEPTION("9998", "网络异常"),

    TIMEOUT_EXCEPTION("9997", "超时异常"),

    BIZ_EXCEPTION("9996", "业务异常"),

    FORBIDDEN_EXCEPTION("9995", "服务不可用"),

    SERIALIZATION_EXCEPTION("9994", "序列化异常");

    private String code;
    private String message;

    RpcExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
