package com.ikeyit.common.data;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

public interface IncludedFields {
    boolean has(@Nonnull String field);
    default <T> T includeThenReturn(@Nonnull String field, @Nonnull Callable<T> callable) {
        if (has(field)) {
            try {
                return callable.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to include field", e);
            }
        }
        return null;
    }

    default IncludedFields include(@Nonnull String field, @Nonnull Runnable runnable) {
        if (has(field)) {
            runnable.run();
        }
        return this;
    }
}
