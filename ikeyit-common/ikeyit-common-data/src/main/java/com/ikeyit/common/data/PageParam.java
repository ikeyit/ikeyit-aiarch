package com.ikeyit.common.data;

public class PageParam {

    protected int page = 1;

    protected int pageSize = 10;

    public PageParam() {
    }

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

    public long getOffset() {
        return (page - 1L) * pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
