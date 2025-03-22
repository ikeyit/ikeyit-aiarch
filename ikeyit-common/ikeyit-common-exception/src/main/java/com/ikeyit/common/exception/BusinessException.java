package com.ikeyit.common.exception;

/**
 * 业务异常类
 * 用于表示业务逻辑错误，通常不需要记录堆栈信息
 */
public class BusinessException extends BaseException {
    
    /**
     * 创建业务异常
     * @param errorCode 错误代码
     * @param message 错误消息
     */
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 创建业务异常
     * @param errorCode 错误代码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}