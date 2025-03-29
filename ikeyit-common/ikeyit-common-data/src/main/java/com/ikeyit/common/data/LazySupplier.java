package com.ikeyit.common.data;

import java.util.function.Supplier;

/**
 * A thread-unsafe implementation of a lazy supplier that caches its value after first access.
 * The value is computed only once when first requested and then cached for subsequent accesses.
 *
 * @param <T> the type of value being supplied
 */
public class LazySupplier<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean isInitialized = false;

    /**
     * Creates a new LazySupplier with the given supplier function.
     *
     * @param supplier the function that computes the value when needed
     * @throws NullPointerException if supplier is null
     */
    public LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the value, computing it if not already initialized.
     * After the first call, the computed value is cached and returned for all subsequent calls.
     *
     * @return the computed value
     */
    public T get() {
        if (!isInitialized) {
            value = supplier.get();
            isInitialized = true;
        }
        return value;
    }
}
