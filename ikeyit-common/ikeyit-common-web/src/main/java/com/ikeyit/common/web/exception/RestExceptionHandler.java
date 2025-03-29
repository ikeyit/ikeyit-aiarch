package com.ikeyit.common.web.exception;


import com.ikeyit.common.exception.BizException;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import com.ikeyit.common.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static final String MESSAGE_KEY_PREFIX = "error.";
    
    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResp> handleBizException(BizException exception){
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespBuilder
                        .of(exception.getErrorCode())
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey(exception.getMessageKey())
                        .setArgs(exception.getArgs())
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResp> handleInvalidArgumentException(Exception exception) {
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.INVALID_ARGUMENT)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("badRequest")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResp> handleServiceException(SystemException exception){
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.INTERNAL_SERVER_ERROR)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("internalServerError")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResp> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.BAD_REQUEST)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("httpRequestMethodNotSupported")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResp> mediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception) {
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.BAD_REQUEST)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("notAcceptable")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResp> requestBindingException(ServletRequestBindingException exception) {
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ErrorRespBuilder
                .of(CommonErrorCode.BAD_REQUEST)
                .setMessageSource(messageSource)
                .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                .setMessageKey("badRequest")
                .setDefaultMessage(exception.getMessage())
                .build());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResp> handleException(Throwable exception){
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorRespBuilder
                        .of(CommonErrorCode.INTERNAL_SERVER_ERROR)
                        .setMessageSource(messageSource)
                        .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                        .setMessageKey("internalServerError")
                        .setDefaultMessage(exception.getMessage())
                        .build());
    }

}