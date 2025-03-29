package com.ikeyit.common.data;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * Default implementation of the IncludedFields interface.
 * This class provides a set of fields that are included in the response.
 */
public class DefaultIncludedFields implements IncludedFields {
    private final Set<String> fields;

    /**
     * Constructs a DefaultIncludedFields instance with the specified fields.
     * 
     * @param fields The collection of fields to include
     */
    public DefaultIncludedFields(Collection<String> fields) {
        this.fields = fields == null ? Set.of() : Set.copyOf(fields);
    }

    /**
     * Checks if the specified field is included in the response.
     * 
     * @param field The field to check
     * @return true if the field is included, false otherwise
     */
    @Override
    public boolean has(@Nonnull String field) {
        return fields.contains(field);
    }

    /**
     * Creates a new DefaultIncludedFields instance with the specified fields.
     * 
     * @param fields The collection of fields to include
     * @return A new DefaultIncludedFields instance
     */
    public static IncludedFields of(String... fields) {
        return new DefaultIncludedFields(Set.of(fields));
    }
}
