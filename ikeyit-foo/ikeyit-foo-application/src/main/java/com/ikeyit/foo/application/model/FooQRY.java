package com.ikeyit.foo.application.model;

import com.ikeyit.common.data.PageParam;

/**
 * <pre>
 * === AI-NOTE ===
 * - Name a parameters object as FooQRY for querying and searching
 * === AI-NOTE-END ===
 * </pre>
 */
public class FooQRY extends PageParam {
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
