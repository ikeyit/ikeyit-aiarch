package com.ikeyit.common.data;

/**
 * 分页请求参数封装类
 */
public class PageRequest {
    private int page;
    private int size;
    private String sortBy;
    private boolean ascending;

    /**
     * 创建分页请求
     * @param page 页码（从0开始）
     * @param size 每页大小
     */
    public PageRequest(int page, int size) {
        this(page, size, null, true);
    }

    /**
     * 创建分页请求（带排序）
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param ascending 是否升序
     */
    public PageRequest(int page, int size, String sortBy, boolean ascending) {
        this.page = Math.max(0, page);
        this.size = Math.max(1, size);
        this.sortBy = sortBy;
        this.ascending = ascending;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = Math.max(1, size);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * 获取偏移量
     * @return 当前页的起始偏移量
     */
    public int getOffset() {
        return page * size;
    }
}