package com.ikeyit.common.data;

/**
 * A class representing pagination parameters.
 * Provides configuration for page-based data retrieval with validation.
 */
public class PageParam {

    /**
     * The current page number (1-based). Defaults to 1.
     */
    protected int page = 1;

    /**
     * The number of items per page. Defaults to 10.
     */
    protected int pageSize = 10;

    /**
     * Creates a new PageParam instance with default values.
     * Page number defaults to 1 and page size defaults to 10.
     */
    public PageParam() {
    }

    /**
     * Creates a new PageParam instance with specified values.
     *
     * @param page the page number (must be greater than or equal to 1)
     * @param pageSize the page size (must be greater than or equal to 1)
     * @throws IllegalArgumentException if page or pageSize is less than 1
     */
    public PageParam(int page, int pageSize) {
        if (page < 1) {
            throw new IllegalArgumentException("Page index must be less than one!");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }

        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * Calculates and returns the offset for the current page.
     * The offset represents the number of items to skip.
     *
     * @return the offset for the current page
     */
    public long getOffset() {
        return (page - 1L) * pageSize;
    }

    /**
     * Returns the current page number.
     *
     * @return the current page number (1-based)
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the page number.
     *
     * @param page the page number to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Returns the current page size.
     *
     * @return the number of items per page
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the number of items per page to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
