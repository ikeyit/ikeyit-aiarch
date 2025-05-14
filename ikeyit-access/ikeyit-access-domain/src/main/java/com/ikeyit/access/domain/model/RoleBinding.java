package com.ikeyit.access.domain.model;

import com.ikeyit.access.domain.event.RoleBindingCreatedEvent;
import com.ikeyit.access.domain.event.RoleBindingDeletedEvent;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.exception.BizAssert;

public class RoleBinding extends BaseAggregateRoot<Long> {
    private Long id;
    private Long userId;
    private Long roleId;
    private RoleType roleType;
    private String realmType;
    private Long realmId;

    public RoleBinding() {
    }

    private RoleBinding(Builder builder) {
        BizAssert.notNull(builder.userId, "userId is required");
        BizAssert.notNull(builder.roleId, "roleId is required");
        BizAssert.notNull(builder.roleType, "roleType is required");
        BizAssert.notNull(builder.realmType, "realmType is required");
        BizAssert.notNull(builder.realmId, "realmId is required");
        userId = builder.userId;
        roleId = builder.roleId;
        roleType = builder.roleType;
        realmType = builder.realmType;
        realmId = builder.realmId;
        addDomainEvent(() -> new RoleBindingCreatedEvent(this));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRealmType() {
        return realmType;
    }

    public Long getRealmId() {
        return realmId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void delete() {
        addDomainEvent(new RoleBindingDeletedEvent(this));
    }

    public static final class Builder {
        private Long userId;
        private Long roleId;
        private RoleType roleType;
        private String realmType;
        private Long realmId;

        private Builder() {
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder roleId(Long val) {
            roleId = val;
            return this;
        }

        public Builder roleType(RoleType val) {
            roleType = val;
            return this;
        }

        public Builder realmType(String val) {
            realmType = val;
            return this;
        }

        public Builder realmId(Long val) {
            realmId = val;
            return this;
        }

        public RoleBinding build() {
            return new RoleBinding(this);
        }
    }
}
