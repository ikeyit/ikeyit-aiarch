package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;

public class PermissionGroup extends BaseAggregateRoot<Long> {
    private Long id;
    private String name;

    public PermissionGroup() {
    }
    public PermissionGroup(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

}
