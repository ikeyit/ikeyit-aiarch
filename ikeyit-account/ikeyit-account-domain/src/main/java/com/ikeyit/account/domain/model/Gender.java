package com.ikeyit.account.domain.model;

import com.ikeyit.common.data.EnumWithInt;

/**
 * Gender enum
 */
public enum Gender implements EnumWithInt {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int value;

    Gender(int value) {
        this.value = value;
    }

    static {
        // Verify there are no duplicate integer values
        EnumWithInt.assertNoDuplicate(values());
    }

    @Override
    public int value() {
        return this.value;
    }
} 