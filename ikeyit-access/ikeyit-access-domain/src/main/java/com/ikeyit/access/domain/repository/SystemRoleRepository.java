package com.ikeyit.access.domain.repository;

import com.ikeyit.access.domain.model.SystemRole;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SystemRoleRepository extends CrudRepository<SystemRole, Long> {
    List<SystemRole> findByRealmAndIds(String realmType, Collection<Long> roleIds);
    Map<Long, SystemRole> findByRealmAndIdsAsMap(String realmType, Collection<Long> roleIds);
    List<SystemRole> findByRealm(String realmType);
    Optional<SystemRole> findByRealmAndName(String realmType, String name);
    Optional<SystemRole> findByRealmAndId(String realmType, Long id);
    Optional<SystemRole> findDefault(String realmType);
    Optional<SystemRole> findSupreme(String realmType);
}
