package com.ikeyit.access.domain.model;

import com.ikeyit.access.domain.event.MemberActivatedEvent;
import com.ikeyit.access.domain.event.MemberCreatedEvent;
import com.ikeyit.access.domain.event.MemberRejectedEvent;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Member extends BaseAggregateRoot<Long> {
    private Long id;
    private Long userId;
    private String displayName;
    private MemberStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public Member() {
    }

    private Member(Builder builder) {
        BizAssert.notNull(builder.userId, "userId is required");
        BizAssert.notEmpty(builder.displayName, "displayName is required");
        BizAssert.notNull(builder.status, "status is required");
        BizAssert.isTrue(builder.status == MemberStatus.UNAPPROVED || builder.status == MemberStatus.ACTIVE, "status should be unapproved or active");
        userId = builder.userId;
        displayName = builder.displayName;
        status = builder.status;
        createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        updatedAt = createdAt;
        addDomainEvent(() -> new MemberCreatedEvent(this));
        if (status == MemberStatus.ACTIVE)
            addDomainEvent(() -> new MemberActivatedEvent(this));
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

    public void approve() {
        BizAssert.isTrue(status == MemberStatus.UNAPPROVED, "Invalid status");
        status = MemberStatus.ACTIVE;
        updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        addDomainEvent(new MemberActivatedEvent(this));
    }

    public void reject() {
        BizAssert.isTrue(status == MemberStatus.UNAPPROVED, "Invalid status");
        status = MemberStatus.REJECTED;
        updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        addDomainEvent(new MemberRejectedEvent(this));
    }

    public void updateProfile(String displayName) {
        BizAssert.notEmpty(displayName, "displayName is required");
        this.displayName = displayName;
        updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static final class Builder {
        private Long userId;
        private String displayName;
        private MemberStatus status = MemberStatus.UNAPPROVED;
        private Builder() {
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder displayName(String val) {
            displayName = val;
            return this;
        }

        public Builder status(MemberStatus val) {
            status = val;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
