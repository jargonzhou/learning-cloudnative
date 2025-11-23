package com.spike.springcloud.commons.exception;

/**
 * 业务异常.
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误编码, 可以与属性消息对应.
     */
    private String code = "000000";

    public static BusinessException newException(String code, String message) {
        BusinessException result = new BusinessException(message);
        result.setCode(code);
        return result;
    }

    public static BusinessException newException(String message) {
        return new BusinessException(message);
    }

    public static BusinessException newException(String message, Throwable cause) {
        return new BusinessException(message, cause);
    }

    public static BusinessException newException(Throwable cause) {
        return new BusinessException(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
