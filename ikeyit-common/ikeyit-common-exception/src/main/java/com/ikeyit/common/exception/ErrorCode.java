package com.ikeyit.common.exception;

import java.util.HashSet;
import java.util.Set;

/**
 * Interface for error codes in the application.
 * Implementations must provide a unique numeric value for each error code.
 */
public interface ErrorCode {
    /**
     * Returns the numeric value of this error code.
     * @return The error code value
     */
    int value();

    /**
     * Utility method to ensure no duplicate error code values exist in an array of error codes.
     * @param errorCodes Array of error codes to check for duplicates
     * @throws IllegalStateException if duplicate values are found
     */
    static void assertNoDuplicate(ErrorCode[] errorCodes) {
        if (errorCodes == null || errorCodes.length == 0) {
            return;
        }
        Set<Integer> valueSet = new HashSet<>(errorCodes.length);
        for (ErrorCode errorCode : errorCodes) {
            if (!valueSet.add(errorCode.value())) {
                throw new IllegalStateException("Duplicate ErrorCode!");
            }
        }
    }
}
