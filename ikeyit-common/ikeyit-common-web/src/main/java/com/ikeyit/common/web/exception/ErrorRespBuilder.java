package com.ikeyit.common.web.exception;

import com.ikeyit.common.exception.ErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import com.ikeyit.common.exception.MessageKey;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * A builder class for creating ErrorResp objects with internationalized error messages.
 * This class implements the Builder pattern to provide a fluent API for constructing
 * error responses with proper error codes and localized messages.
 *
 * The builder supports:
 * - Setting error codes
 * - Configuring default messages
 * - Handling message internationalization through Spring's MessageSource
 * - Managing message keys and prefixes
 * - Handling message arguments for parameterized messages
 */
public class ErrorRespBuilder {
    private ErrorCode errorCode;
    private String defaultMessage;
    private String messageKeyPrefix;
    private String messageKey;
    private Object[] args;
    private MessageSource messageSource;

    private ErrorRespBuilder(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode is null!");
        }
        this.errorCode = errorCode;
    }

    /**
     * Sets the default message to be used when message resolution fails.
     *
     * @param defaultMessage The fallback message to use
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    /**
     * Sets the prefix to be prepended to message keys when resolving messages.
     *
     * @param messageKeyPrefix The prefix to add to message keys
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setMessageKeyPrefix(String messageKeyPrefix) {
        this.messageKeyPrefix = messageKeyPrefix;
        return this;
    }

    /**
     * Sets the message key used to look up the localized message.
     *
     * @param messageKey The key to use for message resolution
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setMessageKey(String messageKey) {
        this.messageKey = messageKey;
        return this;
    }

    /**
     * Sets the message key from a MessageKey enum value.
     *
     * @param messageKey The MessageKey enum value
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setMessageKey(MessageKey messageKey) {
        this.messageKey = messageKey == null ? null : messageKey.value();
        return this;
    }

    /**
     * Sets the arguments to be used in message formatting.
     *
     * @param args Variable arguments to be used in message formatting
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setArgs(Object... args) {
        this.args = args;
        return this;
    }

    /**
     * Sets the MessageSource to be used for message resolution.
     *
     * @param messageSource The Spring MessageSource to use
     * @return This builder instance for method chaining
     */
    public ErrorRespBuilder setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    /**
     * Builds the ErrorResp object using the configured parameters.
     * Uses the current locale from LocaleContextHolder for message resolution.
     *
     * @return A new ErrorResp instance with the resolved message
     */
    public ErrorResp build() {
        Locale locale = LocaleContextHolder.getLocale();
        String message = defaultMessage;
        if (messageKey != null && messageSource != null) {
            String fullMessageKey = messageKeyPrefix != null ? messageKeyPrefix + messageKey : messageKey;
            message = messageSource.getMessage(fullMessageKey, args, defaultMessage, locale);
        }
        return new ErrorResp(errorCode.name(), message);
    }


    /**
     * Creates a new ErrorRespBuilder instance with the specified error code.
     *
     * @param errorCode The error code to use in the response
     * @return A new ErrorRespBuilder instance
     * @throws IllegalArgumentException if errorCode is null
     */
    public static ErrorRespBuilder of(ErrorCode errorCode) {
        return new ErrorRespBuilder(errorCode);
    }
}
