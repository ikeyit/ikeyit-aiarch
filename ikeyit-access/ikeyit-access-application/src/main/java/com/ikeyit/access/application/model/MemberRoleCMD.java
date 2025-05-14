package com.ikeyit.access.application.model;

import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.common.exception.BizAssert;

public record MemberRoleCMD(Long id, RoleType roleType) {
    public MemberRoleCMD {
        BizAssert.notNull(id, "id must not be null");
        BizAssert.notNull(roleType, "roleType must not be null");
    }
}
