package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Org extends BaseAggregateRoot<Long> {
    private Long id;
    private String name;
    private String picture;
    private Instant createdAt;
    private Instant updatedAt;

    public Org() {
    }

    private Org(String name, String picture) {
        this.name = name;
        this.picture = picture;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = this.createdAt;
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

    public String getPicture() {
        return picture;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name, String picture) {
        BizAssert.notEmpty(name, "name should not be empty");
        BizAssert.notNull(picture, "picture should not be null");
        this.name = name;
        this.picture = picture;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

}
