package cn.bixin.sona.gateway.exception;

/**
 * @author qinwei
 */
public class AccessMessageDecodeException extends RuntimeException {

    private static final long serialVersionUID = 7297511371816648440L;

    public AccessMessageDecodeException() {
        super();
    }

    public AccessMessageDecodeException(String message) {
        super(message);
    }

    public AccessMessageDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessMessageDecodeException(Throwable cause) {
        super(cause);
    }

}
