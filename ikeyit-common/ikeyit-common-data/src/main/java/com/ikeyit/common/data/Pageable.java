package com.ikeyit.common.data;

/**
 * Represents a pageable request with pagination parameters.
 */
public interface Pageable {
    /**
     * Retrieves the page parameters for the current request.
     * 
     * @return The page parameters
     */
    PageParam pageParam();
}
