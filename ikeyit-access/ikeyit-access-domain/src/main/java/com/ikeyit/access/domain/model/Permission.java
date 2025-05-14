package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;

public class Permission extends BaseAggregateRoot<Long> {
    private Long id;
    private String realmType;
    private Long groupId;
    private String name;
    private String description;

    public Permission() {
    }

    public Permission(String name, String description, Long groupId, String realmType) {
        this.realmType = realmType;
        this.name = name;
        this.groupId = groupId;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getRealmType() {
        return realmType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
