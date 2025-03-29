package com.ikeyit.common.data.domain;

/**
 * Mark one class as an entity,
 * @param <ID>
 */
public interface Entity<ID> {
    ID getId();
    @ForRepo
    void assignId(ID id);
}
