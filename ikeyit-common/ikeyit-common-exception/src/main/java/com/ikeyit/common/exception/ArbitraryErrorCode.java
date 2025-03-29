package com.ikeyit.common.exception;

/**
 * A class that implements ErrorCode interface to represent arbitrary error codes with custom values.
 * This allows creation of error codes that are not part of predefined enums.
 */
public class ArbitraryErrorCode implements ErrorCode{
    private final int value;

    /**
     * Creates a new ArbitraryErrorCode with the specified value.
     * @param value The numeric value for this error code
     */
    public ArbitraryErrorCode(int value) {
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
}
