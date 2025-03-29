package com.ikeyit.common.data;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

public class DefaultIncludedFields implements IncludedFields {
    private final Set<String> fields;

    public DefaultIncludedFields(Collection<String> fields) {
        this.fields = fields == null ? Set.of() : Set.copyOf(fields);
    }

    @Override
    public boolean has(@Nonnull String field) {
        return fields.contains(field);
    }

    public static IncludedFields of(String... fields) {
        return new DefaultIncludedFields(Set.of(fields));
    }
}
