package com.ikeyit.common.data;

import java.util.*;
import java.util.function.Supplier;

/**
 * Give an enum one extra value. e.g. integer. The enum will be serialized to / deserialized from the value. It's useful
 * when you want store enum as int type in DB to improve performance.
 * Note: the interface should be implemented only by Enum.
 */
public interface EnumWithInt {

    int value();

    static <A extends EnumWithInt> void assertNoDuplicate(A[] values) {
        Set<Integer> valueSet = new HashSet<>(values.length);
        for (A value : values) {
            if (!valueSet.add(value.value())) {
                throw new IllegalStateException("Duplicate values!");
            }
        }
    }

    static <A extends EnumWithInt> Optional<A> optionalOf(A[] values, int value) {
        return Arrays.stream(values)
                .filter(e -> e.value() == value)
                .findFirst();
    }

    static <A extends EnumWithInt> A of(A[] values, int value, A defaultValue) {
        return optionalOf(values, value)
                .orElse(defaultValue);
    }

    static <A extends EnumWithInt> A of(A[] values, int value) {
        return optionalOf(values, value)
                .orElseThrow(() -> new IllegalArgumentException("Value is not found!"));
    }

    static <A extends EnumWithInt> A of(A[] values, int value, Supplier<? extends RuntimeException> exceptionSupplier) {
        return optionalOf(values, value)
                .orElseThrow(exceptionSupplier);
    }
}
