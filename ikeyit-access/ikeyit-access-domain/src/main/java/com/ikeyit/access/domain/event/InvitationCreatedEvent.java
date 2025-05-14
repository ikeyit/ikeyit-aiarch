package com.ikeyit.access.domain.event;

import com.ikeyit.access.domain.model.Invitation;
import com.ikeyit.common.data.domain.BaseDomainEvent;
import java.time.Duration;
import java.time.Instant;

public class InvitationCreatedEvent extends BaseDomainEvent {
    private String invitationId;
    private String orgName;
    private String roleName;
    private Long inviterId;
    private String inviterName;
    private String email;
    private Duration timeout;
    private Instant createdAt;

    public InvitationCreatedEvent() {
    }

    public InvitationCreatedEvent(Invitation invitation) {
        this.invitationId = invitation.getId();
        this.orgName = invitation.getOrgName();
        this.roleName = invitation.getRoleName();
        this.inviterId = invitation.getInviterId();
        this.inviterName = invitation.getInviterName();
        this.email = invitation.getEmail();
        this.timeout = invitation.getTimeout();
        this.createdAt = invitation.getCreatedAt();
    }

    public String getOrgName() {
        return orgName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getInvitationId() {
        return invitationId;
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
}

