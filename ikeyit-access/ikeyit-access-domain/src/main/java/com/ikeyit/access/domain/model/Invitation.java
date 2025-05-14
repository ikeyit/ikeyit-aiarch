package com.ikeyit.access.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.access.domain.event.InvitationCreatedEvent;
import com.ikeyit.common.exception.BizAssert;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Invitation extends BaseAggregateRoot<String> {
    private String id;
    private String realmType;
    private Long realmId;
    private Long roleId;
    private String roleName;
    private String orgName;
    private Long inviterId;
    private String inviterName;
    private boolean requireApproval;
    private String email;
    private Duration timeout;
    private Instant createdAt;
    public Invitation() {
    }
    private Invitation(Builder builder) {
        BizAssert.notNull(builder.realmType, "realmType is required");
        BizAssert.notNull(builder.realmId, "realmId is required");
        BizAssert.notEmpty(builder.orgName, "orgName is required");
        orgName = builder.orgName;
        realmType = builder.realmType;
        realmId = builder.realmId;
        roleId = builder.roleId;
        roleName = builder.roleName;
        inviterId = builder.inviterId;
        inviterName = builder.inviterName;
        requireApproval = builder.requireApproval;
        email = builder.email;
        timeout = builder.timeout;
        createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.addDomainEvent(() -> new InvitationCreatedEvent(this));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void assignId(String id) {
        this.id = id;
    }

    public String getRealmType() {
        return realmType;
    }

    public Long getRealmId() {
        return realmId;
    }

    public String getOrgName() {
        return orgName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public String getEmail() {
        return email;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isRequireApproval() {
        return requireApproval;
    }

    public static final class Builder {
        private String orgName;
        private String realmType;
        private Long realmId = 0L;
        private Long roleId;
        private String roleName;
        private Long inviterId;
        private String inviterName;
        private boolean requireApproval;
        private String email;
        private Duration timeout = Duration.ofDays(1);

        private Builder() {
        }

        public Builder orgName(String val) {
            orgName = val;
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

        public Builder roleId(Long val) {
            roleId = val;
            return this;
        }

        public Builder roleName(String val) {
            roleName = val;
            return this;
        }

        public Builder inviterId(Long val) {
            inviterId = val;
            return this;
        }

        public Builder inviterName(String val) {
            inviterName = val;
            return this;
        }

        public Builder requireApproval(boolean val) {
            requireApproval = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder timeout(Duration val) {
            timeout = val;
            return this;
        }

        public Invitation build() {
            return new Invitation(this);
        }
    }
}
