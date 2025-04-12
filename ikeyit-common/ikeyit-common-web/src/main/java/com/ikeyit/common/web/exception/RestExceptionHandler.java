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

/**
 * Global exception handler for REST endpoints that provides centralized error handling
 * and consistent error response formatting across the application.
 *
 * This handler processes various types of exceptions and converts them into
 * standardized error responses with appropriate HTTP status codes and
 * internationalized error messages.
 *
 * Handles the following types of exceptions:
 * - Business logic exceptions (BizException)
 * - Validation and argument errors
 * - System-level exceptions
 * - HTTP request-related exceptions
 */
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    protected static final String MESSAGE_KEY_PREFIX = "error.";
    
    protected final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BizException.class)
    /**
     * Handles business logic exceptions and converts them to error responses.
     *
     * @param exception The business exception that was thrown
     * @return A ResponseEntity with BAD_REQUEST status and error details
     */
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
    /**
     * Handles various types of invalid argument exceptions.
     * This includes missing parameters, invalid types, and validation failures.
     *
     * @param exception The exception that was thrown
     * @return A ResponseEntity with BAD_REQUEST status and error details
     */
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
    /**
     * Handles system-level exceptions that indicate internal server errors.
     *
     * @param exception The system exception that was thrown
     * @return A ResponseEntity with INTERNAL_SERVER_ERROR status and error details
     */
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
    /**
     * Handles requests with unsupported HTTP methods.
     *
     * @param exception The HttpRequestMethodNotSupportedException that was thrown
     * @return A ResponseEntity with BAD_REQUEST status and error details
     */
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
    /**
     * Handles requests with unsupported media types.
     *
     * @param exception The HttpMediaTypeNotAcceptableException that was thrown
     * @return A ResponseEntity with NOT_ACCEPTABLE status and error details
     */
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
    /**
     * Handles servlet request binding exceptions.
     *
     * @param exception The ServletRequestBindingException that was thrown
     * @return A ResponseEntity with NOT_ACCEPTABLE status and error details
     */
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
    /**
     * Fallback handler for all unhandled exceptions.
     *
     * @param exception The unhandled exception that was thrown
     * @return A ResponseEntity with INTERNAL_SERVER_ERROR status and error details
     */
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