package com.ikeyit.common.exception;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * A runtime exception class for business logic errors.
 * This exception carries an error code, optional message key for i18n, and message arguments.
 */
public class BizException extends RuntimeException {

    private ErrorCode errorCode;

    private Object[] args;

    private MessageKey messageKey;

    /**
     * Creates a new BizException with only an error code.
     * @param errorCode The error code for this exception
     */
    public BizException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    /**
     * Creates a new BizException with an error code and formatted message.
     * @param errorCode The error code for this exception
     * @param message The message format string
     * @param args The message format arguments
     */
    public BizException(ErrorCode errorCode, String message, Object... args) {
        this(errorCode, null, null, message, args);
    }

    /**
     * Creates a new BizException with an error code, message key, and formatted message.
     * @param errorCode The error code for this exception
     * @param messageKey The message key for i18n
     * @param message The message format string
     * @param args The message format arguments
     */
    public BizException(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        this(errorCode, null, messageKey, message, args);
    }

    /**
     * Creates a new BizException with an error code, cause, and formatted message.
     * @param errorCode The error code for this exception
     * @param cause The cause of this exception
     * @param message The message format string
     * @param args The message format arguments
     */
    public BizException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
        this(errorCode, cause, null, message, args);
    }

    /**
     * Creates a new BizException with all parameters.
     * @param errorCode The error code for this exception
     * @param cause The cause of this exception
     * @param messageKey The message key for i18n
     * @param message The message format string
     * @param args The message format arguments
     */
    public BizException(ErrorCode errorCode, Throwable cause, MessageKey messageKey, String message, Object... args) {
        super(detailMessage(messageKey, message, args), cause);
        this.messageKey = messageKey;
        this.errorCode = errorCode;
        this.args = args;
    }

    private static String detailMessage(MessageKey messageKey, String message, Object... args) {
        if (message != null) {
            return MessageFormat.format(message, args);
        }
        return messageKey != null ? "Key: " + messageKey + ", Args: " + Arrays.toString(args) : null;
    }

    /**
     * Gets the error code associated with this exception.
     * @return The error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the numeric value of the error code.
     * @return The error code value
     */
    public int getErrorCodeValue() {
        return errorCode.value();
    }

    /**
     * Gets the message key for i18n if present.
     * @return The message key, or null if not set
     */
    @Nullable
    public MessageKey getMessageKey() {
        return messageKey;
    }

    /**
     * Gets the message format arguments if present.
     * @return The message arguments, or null if not set
     */
    @Nullable
    public Object[] getArgs() {
        return args;
    }
}
