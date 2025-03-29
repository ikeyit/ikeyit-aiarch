package com.ikeyit.common.exception;

import java.util.HashSet;
import java.util.Set;

public interface ErrorCode {
    int value();

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
