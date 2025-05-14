package com.ikeyit.access.application.model;

import com.ikeyit.access.domain.model.MemberStatus;

public class AcceptInvitationResultDTO {
    private MemberDTO member;

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }
}
