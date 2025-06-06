package com.ikeyit.access.domain.event;

import com.ikeyit.access.domain.model.Member;
import com.ikeyit.common.data.domain.BaseDomainEvent;

public class MemberCreatedEvent extends BaseDomainEvent {
    private Long memberId;
    private Long userId;
    private String displayName;

    public MemberCreatedEvent() {
    }
    public MemberCreatedEvent(Member member) {
        this.memberId = member.getId();
        this.userId = member.getUserId();
        this.displayName = member.getDisplayName();
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
