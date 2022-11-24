package cn.bixin.sona.common.dto;

/**
 * @author qinwei
 */
public interface Code {

    String getCode();

    String getMessage();

    Code SUCCESS = new Code() {
        @Override
        public String getCode() {
            return "8000";
        }

        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };

    Code ERROR_PARAM = new Code() {
        @Override
        public String getCode() {
            return "8010";
        }

        @Override
        public String getMessage() {
            return "参数不正确";
        }
    };

    /**
     * 自定义新的code
     */
    static Code business(String code, String msg) {
        return new Code() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return msg;
            }
        };
    }

}
