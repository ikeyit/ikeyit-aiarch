package com.ikeyit.classroom.domain.model;

import com.ikeyit.common.data.EnumWithInt;

/**
 * Course status enumeration
 */
public enum CourseStatus implements EnumWithInt {
    DRAFT(0),      // Draft
    PUBLISHED(1),  // Published
    CANCELLED(2);  // Cancelled

    private final int value;

    CourseStatus(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}