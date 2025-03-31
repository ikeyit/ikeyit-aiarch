package com.ikeyit.foo.domain.model;


import com.ikeyit.common.data.EnumWithInt;

/**
 * Foo status enum
 * NOTE:
 * Use this boilerplate code to define enumeration type used by a domain model
 * Implement EnumWithInt.
 * Store the integer value as SMALLINT type in your database table
 */
public enum FooStatus implements EnumWithInt {
    DRAFT(1),
    ACTIVE(2),
    INACTIVE(3),
    ;

    private final int value;

    FooStatus(int value) {
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
