package com.ikeyit.common.data;

import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean isInitialized = false;

    public LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (!isInitialized) {
            value = supplier.get();
            isInitialized = true;
        }
        return value;
    }
}
