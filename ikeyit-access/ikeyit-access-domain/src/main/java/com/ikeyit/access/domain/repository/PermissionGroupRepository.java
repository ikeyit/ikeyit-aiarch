package com.ikeyit.access.domain.repository;


import com.ikeyit.access.domain.model.PermissionGroup;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PermissionGroupRepository extends CrudRepository<PermissionGroup, Long> {
    List<PermissionGroup> findByIds(Collection<Long> ids);
    Map<Long, PermissionGroup> findByIdsAsMap(Collection<Long> ids);
}
