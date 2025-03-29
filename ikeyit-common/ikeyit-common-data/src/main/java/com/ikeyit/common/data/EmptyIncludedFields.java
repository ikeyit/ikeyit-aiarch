package com.ikeyit.common.data;

import javax.annotation.Nonnull;

public class EmptyIncludedFields implements IncludedFields  {

    private static final IncludedFields INSTANCE = new EmptyIncludedFields();

    private EmptyIncludedFields() {
        
    }
    
    public static IncludedFields get() {
        return INSTANCE;
    }

    @Override
    public boolean has(@Nonnull String field) {
        return false;
    }
}
