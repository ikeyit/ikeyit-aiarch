package com.ikeyit.foo.domain.model;


import com.ikeyit.common.data.EnumWithInt;

/**
 * <pre>
 * === AI-NOTE ===
 * - Enumeration type used by a domain model should implement the interface "EnumWithInt"
 * - Store the integer value as SMALLINT type in your database table
 * === AI-NOTE-END ===
 * </pre>
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

    // # AI REMINDER
    // Generate the static code to ensure there is no duplication
    static {
        EnumWithInt.assertNoDuplicate(values());
    }

    @Override
    public int value() {
        return this.value;
    }
}
