package com.ikeyit.common.data;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

/**
 * Interface for managing field inclusion in data objects.
 * Provides functionality to check if a field should be included and conditionally execute
 * code based on field inclusion status.
 */
public interface IncludedFields {
    /**
     * Checks if a field should be included.
     *
     * @param field the name of the field to check
     * @return true if the field should be included, false otherwise
     * @throws NullPointerException if field is null
     */
    boolean has(@Nonnull String field);

    /**
     * Executes a callable if a field should be included and returns its result.
     * If the field is not included, returns null.
     *
     * @param field the name of the field to check
     * @param callable the code to execute if the field is included
     * @param <T> the type of the result
     * @return the result of the callable if the field is included, null otherwise
     * @throws NullPointerException if field or callable is null
     * @throws IllegalStateException if the callable throws a checked exception
     */
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

    /**
     * Executes a runnable if a field should be included.
     * Returns this instance for method chaining.
     *
     * @param field the name of the field to check
     * @param runnable the code to execute if the field is included
     * @return this instance
     * @throws NullPointerException if field or runnable is null
     */
    default IncludedFields include(@Nonnull String field, @Nonnull Runnable runnable) {
        if (has(field)) {
            runnable.run();
        }
        return this;
    }
}
