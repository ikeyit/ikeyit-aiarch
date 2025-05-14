package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.EnumWithInt;

public enum RoleType implements EnumWithInt {
    SYSTEM(1),
    CUSTOM(2);

    private final int value;

    RoleType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    public static RoleType of(int v) {
        return EnumWithInt.of(values(), v);
    }
}
