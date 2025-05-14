package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.EnumWithInt;

public enum MemberStatus implements EnumWithInt {
    UNAPPROVED(1),
    ACTIVE(2),
    REJECTED(3),
    DISABLED(4),
    ;

    private final int value;

    MemberStatus(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    public static MemberStatus of(int v) {
        return EnumWithInt.of(values(), v);
    }
}
