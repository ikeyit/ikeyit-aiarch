package com.ikeyit.access.domain.repository;

import com.ikeyit.access.domain.model.CustomRole;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomRoleRepository extends CrudRepository<CustomRole, Long> {
    List<CustomRole> findByRealmAndIds(String realmType, Long realmId, Collection<Long> roleIds);
    Map<Long, CustomRole> findByRealmAndIdsAsMap(String realmType, Long realmId, Collection<Long> roleIds);
    List<CustomRole> findByRealm(String realmType, Long realmId);
    Optional<CustomRole> findByRealmAndName(String realmType, Long realmId, String name);
}
