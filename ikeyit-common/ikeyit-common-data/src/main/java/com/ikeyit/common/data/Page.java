package com.ikeyit.common.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class representing a page of data with pagination information.
 * This class is immutable and thread-safe.
 *
 * @param <T> the type of elements in the page
 */
public class Page<T> {
    private final List<T> content;
    private final int page;
    private final int pageSize;
    private final long total;
    private final int totalPages;

    /**
     * Creates a new Page instance using a PageParam object.
     *
     * @param content the list of items in the current page
     * @param pageParam the pagination parameters
     * @param total the total number of items across all pages
     */
    public Page(List<T> content, PageParam pageParam, long total) {
        this(content, pageParam.getPage(), pageParam.getPageSize(), total);
    }

    /**
     * Creates a new Page instance with the specified parameters.
     *
     * @param content the list of items in the current page
     * @param page the current page number (1-based)
     * @param pageSize the size of each page
     * @param total the total number of items across all pages
     */
    public Page(List<T> content, int page, int pageSize, long total) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.content = content;
        this.totalPages = (int) Math.ceil((double) this.total / (double) this.pageSize);
    }

    /**
     * Returns the content of the current page.
     *
     * @return the list of items in the current page
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Returns the current page number (1-based).
     *
     * @return the current page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Returns the size of each page.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns the total number of items across all pages.
     *
     * @return the total number of items
     */
    public long getTotal() {
        return total;
    }

    /**
     * Returns the total number of pages.
     *
     * @return the total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Checks if there is a next page available.
     *
     * @return true if there is a next page, false otherwise
     */
    public boolean hasNext() {
        return page < totalPages;
    }

    /**
     * Checks if this is the last page.
     *
     * @return true if this is the last page, false otherwise
     */
    public boolean isLast() {
        return !hasNext();
    }

    /**
     * Checks if there is a previous page available.
     *
     * @return true if there is a previous page, false otherwise
     */
    public boolean hasPrevious() {
        return page > 1;
    }

    /**
     * Checks if this is the first page.
     *
     * @return true if this is the first page, false otherwise
     */
    public boolean isFirst() {
        return !hasPrevious();
    }

    /**
     * Maps the content of this page to a new Page with transformed content.
     *
     * @param <R> the type of elements in the new page
     * @param mapper the function to transform the content
     * @return a new Page instance with transformed content
     */
    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        if (content == null) {
            return new Page<>(null, page, pageSize, total);
        }

        List<R> mappedContent = content.stream().map(mapper).collect(Collectors.toList());
        return new Page<>(mappedContent, page, pageSize, total);
    }

    /**
     * A pre-defined empty page instance.
     */
    @SuppressWarnings("rawtypes")
    public static final Page EMPTY = new Page<>(null, 1, 1, 0);

    /**
     * Returns an empty page instance.
     *
     * @param <T> the type of elements in the page
     * @return an empty page
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> emptyPage() {
        return (Page<T>) EMPTY;
    }

    /**
     * Creates an empty page with specified page number and size.
     *
     * @param <T> the type of elements in the page
     * @param page the page number
     * @param pageSize the page size
     * @return an empty page with the specified parameters
     */
    public static <T> Page<T> emptyPage(int page, int pageSize) {
        return new Page<>(null, page, pageSize, 0);
    }
}
