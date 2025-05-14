package com.ikeyit.access.application.model;

import com.ikeyit.access.domain.model.MemberStatus;

import java.time.Instant;
import java.util.List;

public class MemberDTO {

    private Long id;

    private Long userId;

    private String displayName;

    private List<MemberRoleDTO> roles;

    private MemberStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    public MemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<MemberRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<MemberRoleDTO> roles) {
        this.roles = roles;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
