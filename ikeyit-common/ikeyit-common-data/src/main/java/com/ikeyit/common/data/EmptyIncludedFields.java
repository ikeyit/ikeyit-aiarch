package com.ikeyit.common.data;

import javax.annotation.Nonnull;

/**
 * Implementation of IncludedFields that excludes all fields.
 * This is a singleton class that always returns false for any field check,
 * indicating that no fields should be included in the response.
 */
public class EmptyIncludedFields implements IncludedFields  {

    private static final IncludedFields INSTANCE = new EmptyIncludedFields();

    /**
     * Private constructor to prevent instantiation outside of this class.
     */
    private EmptyIncludedFields() {
        
    }
    
    /**
     * Gets the singleton instance of EmptyIncludedFields.
     * 
     * @return The singleton instance
     */
    public static IncludedFields get() {
        return INSTANCE;
    }

    /**
     * Checks if a field should be included.
     * This implementation always returns false, indicating no fields should be included.
     *
     * @param field The name of the field to check
     * @return Always returns false
     */
    @Override
    public boolean has(@Nonnull String field) {
        return false;
    }
}
