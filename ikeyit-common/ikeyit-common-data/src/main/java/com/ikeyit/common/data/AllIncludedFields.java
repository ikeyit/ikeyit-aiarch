package com.ikeyit.common.data;

import javax.annotation.Nonnull;

public class AllIncludedFields implements IncludedFields  {

    private static final IncludedFields INSTANCE = new AllIncludedFields();

    private AllIncludedFields() {
        
    }
    
    public static IncludedFields get() {
        return INSTANCE;
    }

    @Override
    public boolean has(@Nonnull String field) {
        return true;
    }
}
