package com.ikeyit.common.exception;


public enum CommonErrorCode implements ErrorCode {

    BAD_REQUEST(1),

    INVALID_ARGUMENT(2),

    FORBIDDEN(3),

    UNAUTHORIZED(4),

    NOT_FOUND(5),

    CONFLICT(6),

    RESOURCE_NOT_FOUND(7),

    RESOURCE_UPDATE_CONFLICT(8),

    INTERNAL_SERVER_ERROR(9),

    THIRD_PARTY_ERROR(10),

    OCCUPIED(11),

    PERSISTENCE_ERROR(12),

    OPTIMISTIC_LOCK_CONFLICT(13),
    ;

    private final int value;

    static {
        ErrorCode.assertNoDuplicate(values());
    }

    CommonErrorCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    public static CommonErrorCode valueOf(int codeValue) {
        for (CommonErrorCode v : values()) {
            if (v.value() == codeValue) {
                return v;
            }
        }

        throw new IllegalArgumentException("ErrorCode is not found! " + codeValue);
    }
}
