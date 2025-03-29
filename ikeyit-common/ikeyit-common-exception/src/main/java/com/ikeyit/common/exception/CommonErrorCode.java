package com.ikeyit.common.exception;

/**
 * Enumeration of common error codes used throughout the application.
 * Each error code has a unique numeric value.
 * The class ensures no duplicate values exist among the error codes.
 */
public enum CommonErrorCode implements ErrorCode {

    /** Invalid request format or structure */
    BAD_REQUEST(1),

    /** Invalid argument or parameter value */
    INVALID_ARGUMENT(2),

    /** Access to the resource is forbidden */
    FORBIDDEN(3),

    /** Authentication is required to access the resource */
    UNAUTHORIZED(4),

    /** Requested resource was not found */
    NOT_FOUND(5),

    /** Request conflicts with current state of the resource */
    CONFLICT(6),

    /** Specific resource was not found */
    RESOURCE_NOT_FOUND(7),

    /** Resource update operation resulted in a conflict */
    RESOURCE_UPDATE_CONFLICT(8),

    /** Internal server error occurred */
    INTERNAL_SERVER_ERROR(9),

    /** Error occurred while communicating with third-party service */
    THIRD_PARTY_ERROR(10),

    /** Resource is currently occupied or locked */
    OCCUPIED(11),

    /** Error occurred during data persistence operation */
    PERSISTENCE_ERROR(12),

    /** Optimistic locking conflict occurred during update */
    OPTIMISTIC_LOCK_CONFLICT(13);

    private final int value;

    static {
        ErrorCode.assertNoDuplicate(values());
    }

    /**
     * Creates a new CommonErrorCode with the specified value.
     * @param value The numeric value for this error code
     */
    CommonErrorCode(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of this error code.
     * @return The error code value
     */
    @Override
    public int value() {
        return value;
    }

    /**
     * Finds a CommonErrorCode by its numeric value.
     * @param codeValue The numeric value to look up
     * @return The corresponding CommonErrorCode
     * @throws IllegalArgumentException if no matching error code is found
     */
    public static CommonErrorCode valueOf(int codeValue) {
        for (CommonErrorCode v : values()) {
            if (v.value() == codeValue) {
                return v;
            }
        }

        throw new IllegalArgumentException("ErrorCode is not found! " + codeValue);
    }
}
