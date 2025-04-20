package com.ikeyit.classroom.domain.model;

import com.ikeyit.common.data.EnumWithInt;

/**
 * Enrollment status enumeration
 */
public enum EnrollmentStatus implements EnumWithInt {
    PENDING(0),    // Pending review
    APPROVED(1),   // Approved
    REJECTED(2),   // Rejected
    CANCELLED(3);  // Cancelled

    private final int value;

    EnrollmentStatus(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}