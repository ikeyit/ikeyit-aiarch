package com.ikeyit.common.exception;

/**
 * 应用程序异常基类
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * 创建异常
     * @param errorCode 错误代码
     * @param message 错误消息
     */
    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 创建异常
     * @param errorCode 错误代码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误代码
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }
}