package com.ikeyit.common.exception;

/**
 * Enumeration of common error codes used throughout the application.
 */
public enum CommonErrorCode implements ErrorCode {

    /** Invalid request format or structure */
    BAD_REQUEST,

    /** Invalid argument or parameter value */
    INVALID_ARGUMENT,

    /** Authorization is required*/
    AUTHORIZATION_REQUIRED,

    /** Authentication is required*/
    AUTHENTICATION_REQUIRED,

    /** Requested resource was not found */
    NOT_FOUND,

    /** Request conflicts with current state of the resource */
    CONFLICT,

    /** Specific resource was not found */
    RESOURCE_NOT_FOUND,

    /** Resource update operation resulted in a conflict */
    RESOURCE_UPDATE_CONFLICT,

    /** Internal server error occurred */
    INTERNAL_SERVER_ERROR,

    /** Error occurred while communicating with third-party service */
    THIRD_PARTY_ERROR,

    /** Resource is currently occupied or locked */
    OCCUPIED,

    /** Error occurred during data persistence operation */
    PERSISTENCE_ERROR,

    /** Optimistic locking conflict occurred during update */
    OPTIMISTIC_LOCK_CONFLICT;
}
