package com.ikeyit.common.data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {
    private final List<T> content;
    private final int page;
    private final int pageSize;
    private final long total;
    private final int totalPages;

    public Page(List<T> content, PageParam pageParam, long total) {
        this(content, pageParam.getPage(), pageParam.getPageSize(), total);
    }

    public Page(List<T> content, int page, int pageSize, long total) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.content = content;
        this.totalPages = (int) Math.ceil((double) this.total / (double) this.pageSize);
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasNext() {
        return page < totalPages;
    }


    public boolean isLast() {
        return !hasNext();
    }

    public boolean hasPrevious() {
        return page > 1;
    }


    public boolean isFirst() {
        return !hasPrevious();
    }

    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        if (content == null) {
            return new Page<>(null, page, pageSize, total);
        }

        List<R> mappedContent = content.stream().map(mapper).collect(Collectors.toList());
        return new Page<>(mappedContent, page, pageSize, total);
    }

    @SuppressWarnings("rawtypes")
    public static final Page EMPTY = new Page<>(null, 1, 1, 0);

    @SuppressWarnings("unchecked")
    public static <T> Page<T> emptyPage() {
        return (Page<T>) EMPTY;
    }

    public static <T> Page<T> emptyPage(int page, int pageSize) {
        return new Page<>(null, page, pageSize, 0);
    }
}
