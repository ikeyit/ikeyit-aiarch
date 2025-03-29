package com.ikeyit.common.data;

import javax.annotation.Nonnull;

/**
 * Implementation of IncludedFields that includes all fields.
 * This is a singleton class that always returns true for any field check,
 * indicating that all fields should be included in the response.
 */
public class AllIncludedFields implements IncludedFields  {

    private static final IncludedFields INSTANCE = new AllIncludedFields();

    /**
     * Private constructor to prevent instantiation outside of this class.
     */
    private AllIncludedFields() {
        
    }
    
    /**
     * Gets the singleton instance of AllIncludedFields.
     * 
     * @return The singleton instance
     */
    public static IncludedFields get() {
        return INSTANCE;
    }

    /**
     * Checks if a field should be included.
     * This implementation always returns true, indicating all fields should be included.
     *
     * @param field The name of the field to check
     * @return Always returns true
     */
    @Override
    public boolean has(@Nonnull String field) {
        return true;
    }
}
