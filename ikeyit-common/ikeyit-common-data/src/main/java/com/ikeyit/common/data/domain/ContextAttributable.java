package com.ikeyit.common.data.domain;

public interface ContextAttributable {
    void putContextAttribute(String name, Object value);
    Object getContextAttribute(String name);
}
