package com.ikeyit.common.data;

import java.util.*;
import java.util.function.Supplier;

/**
 * An interface for enums that have an associated integer value.
 * This interface enables enums to be serialized to and deserialized from integer values,
 * which is useful for storing enum values in databases for better performance.
 * 
 * <p>This interface should only be implemented by enum types. The implementing enum
 * should ensure that each enum constant has a unique integer value.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * public enum Status implements EnumWithInt {
 *     ACTIVE(1),
 *     INACTIVE(2);
 *     
 *     private final int value;
 *     
 *     Status(int value) {
 *         this.value = value;
 *     }
 *     
 *     public int value() {
 *         return value;
 *     }
 * }
 * </pre>
 */
public interface EnumWithInt {

    /**
     * Returns the integer value associated with this enum constant.
     *
     * @return the integer value
     */
    int value();

    /**
     * Verifies that there are no duplicate integer values among the enum constants.
     * This method should typically be called in the enum's static initializer.
     *
     * @param values array of enum constants to check
     * @param <A> the enum type
     * @throws IllegalStateException if duplicate values are found
     */
    static <A extends EnumWithInt> void assertNoDuplicate(A[] values) {
        Set<Integer> valueSet = new HashSet<>(values.length);
        for (A value : values) {
            if (!valueSet.add(value.value())) {
                throw new IllegalStateException("Duplicate values!");
            }
        }
    }

    /**
     * Finds an enum constant by its integer value and returns it wrapped in an Optional.
     *
     * @param values array of enum constants to search
     * @param value the integer value to look for
     * @param <A> the enum type
     * @return an Optional containing the matching enum constant, or empty if not found
     */
    static <A extends EnumWithInt> Optional<A> optionalOf(A[] values, int value) {
        return Arrays.stream(values)
                .filter(e -> e.value() == value)
                .findFirst();
    }

    /**
     * Finds an enum constant by its integer value, returning a default value if not found.
     *
     * @param values array of enum constants to search
     * @param value the integer value to look for
     * @param defaultValue the default value to return if no match is found
     * @param <A> the enum type
     * @return the matching enum constant or the default value
     */
    static <A extends EnumWithInt> A of(A[] values, int value, A defaultValue) {
        return optionalOf(values, value)
                .orElse(defaultValue);
    }

    /**
     * Finds an enum constant by its integer value, throwing an exception if not found.
     *
     * @param values array of enum constants to search
     * @param value the integer value to look for
     * @param <A> the enum type
     * @return the matching enum constant
     * @throws IllegalArgumentException if no matching enum constant is found
     */
    static <A extends EnumWithInt> A of(A[] values, int value) {
        return optionalOf(values, value)
                .orElseThrow(() -> new IllegalArgumentException("Value is not found!"));
    }

    /**
     * Finds an enum constant by its integer value, throwing a custom exception if not found.
     *
     * @param values array of enum constants to search
     * @param value the integer value to look for
     * @param exceptionSupplier supplier of the exception to throw if no match is found
     * @param <A> the enum type
     * @return the matching enum constant
     * @throws RuntimeException from the exceptionSupplier if no matching enum constant is found
     */
    static <A extends EnumWithInt> A of(A[] values, int value, Supplier<? extends RuntimeException> exceptionSupplier) {
        return optionalOf(values, value)
                .orElseThrow(exceptionSupplier);
    }
}
