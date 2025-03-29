package com.ikeyit.common.exception;

/**
 * Record class representing an error response.
 * Contains an error code and error message.
 * @param errCode The numeric error code
 * @param errMsg The error message
 */
public record ErrorResp(int errCode, String errMsg) {
}
