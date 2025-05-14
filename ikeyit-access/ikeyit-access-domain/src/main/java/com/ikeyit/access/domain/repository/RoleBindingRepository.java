package com.ikeyit.access.domain.repository;

import com.ikeyit.access.domain.model.RoleBinding;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoleBindingRepository extends CrudRepository<RoleBinding, Long> {
    List<RoleBinding> findByUserIdInRealm(String realmType, Long realmId, Long userId);
    Optional<RoleBinding> findByUserIdAndRoleIdInRealm(String realmType, Long realmId, Long userId, RoleType roleType, Long roleId);
    List<RoleBinding> findByUserIdsInRealm(String realmType, Long realmId, Collection<Long> userIds);
    List<RoleBinding> findByRoleAndRealm(String realmType, Long realmId, RoleType roleType, Long roleId);
}
