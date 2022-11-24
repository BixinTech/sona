package cn.bixin.sona.common.exception;

import cn.bixin.sona.common.dto.Code;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author qinwei
 */
public class YppRunTimeException extends RuntimeException {

    private static final long serialVersionUID = 1826674764451920773L;

    private Code code;
    private Map<String, Object> ext;

    public YppRunTimeException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    public YppRunTimeException(Code code, Map<String, Object> ext) {
        super(code.getMessage());
        this.code = code;
        this.ext = ext;
    }

    public Code getCode() {
        return this.code;
    }

    public Map<String, Object> getExt() {
        return this.ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return MessageFormat.format("业务异常{0}:{1}", this.code.getCode(), this.code.getMessage());
    }
}