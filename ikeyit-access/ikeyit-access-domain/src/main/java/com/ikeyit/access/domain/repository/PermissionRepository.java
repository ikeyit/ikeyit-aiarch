package com.ikeyit.access.domain.repository;


import com.ikeyit.access.domain.model.Permission;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
    List<Permission> findByIds(Collection<Long> ids);
    Map<Long, Permission> findByIdsAsMap(Collection<Long> ids);
    List<Permission> findByRealmType(String realmType);
}
