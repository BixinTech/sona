package cn.bixin.sona.common.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinwei
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -4676464238372114010L;

    @Description("自定义的业务code码，8000表示业务请求成功，非8000表示业务异常")
    private String code;
    @Description("业务异常的提示信息,成功的请求此字段为空")
    private String msg;
    @Description("请求是成功还是异常 true成功，false异常")
    private boolean success;
    @Description("请求成功后返回的信息")
    private T result;
    @Description("traceId")
    private String tid;
    @Description("扩展信息")
    private Map<String, Object> ext;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Response<T> putExt(String key, Object value) {
        if (this.ext == null) {
            this.ext = new HashMap<>(1);
        }
        this.ext.put(key, value);
        return this;
    }

    public static <T> Response<T> success(T data) {
        return generateResponse(true, Code.SUCCESS, data, null);
    }

    public static <T> Response<T> fail(String code, String msg) {
        return fail(Code.business(code, msg));
    }

    public static <T> Response<T> fail(String code, String msg, Map<String, Object> ext) {
        return fail(Code.business(code, msg), ext);
    }

    public static <T> Response<T> fail(Code code) {
        return generateResponse(false, code, null, null);
    }

    public static <T> Response<T> fail(Code code, Map<String, Object> ext) {
        return generateResponse(false, code, null, ext);
    }

    private static <T> Response<T> generateResponse(boolean success, Code code, T data, Map<String, Object> ext) {
        Response<T> response = new Response<>();
        response.code = code.getCode();
        response.msg = code.getMessage();
        response.success = success;
        response.result = data;
        response.ext = ext;
        return response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", result=" + result +
                ", tid='" + tid + '\'' +
                ", ext=" + ext +
                '}';
    }
}
