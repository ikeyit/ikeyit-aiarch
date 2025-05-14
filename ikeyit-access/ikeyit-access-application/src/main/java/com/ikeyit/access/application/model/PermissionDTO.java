package com.ikeyit.access.application.model;

public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
    private String realmType;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealmType() {
        return realmType;
    }

    public void setRealmType(String realmType) {
        this.realmType = realmType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
