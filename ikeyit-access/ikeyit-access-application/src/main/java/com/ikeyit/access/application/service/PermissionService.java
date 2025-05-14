package com.ikeyit.access.application.service;

import com.google.common.collect.Sets;
import com.ikeyit.access.application.model.PermissionDTO;
import com.ikeyit.access.application.model.PermissionGroupDTO;
import com.ikeyit.access.domain.model.Permission;
import com.ikeyit.access.domain.repository.PermissionGroupRepository;
import com.ikeyit.access.domain.repository.PermissionRepository;
import com.ikeyit.common.exception.BizAssert;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionGroupRepository permissionGroupRepository;

    public PermissionService(PermissionRepository permissionRepository, PermissionGroupRepository permissionGroupRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    public List<PermissionGroupDTO> findPermissionByRealmType(String realmType) {
        List<Permission> permissions = permissionRepository.findByRealmType(realmType);
        var groupedPermissions = permissions.stream().collect(Collectors.groupingBy(Permission::getGroupId));
        Set<Long> groupIds = permissions.stream().map(Permission::getGroupId).collect(Collectors.toSet());
        var permissionGroups = permissionGroupRepository.findByIds(groupIds);
        List<PermissionGroupDTO> permissionGroupDTOs = new ArrayList<>();
        for (var permissionGroup : permissionGroups) {
            var permissionsInGroup = groupedPermissions.get(permissionGroup.getId());
            if (permissionsInGroup != null) {
                PermissionGroupDTO permissionGroupDTO = new PermissionGroupDTO();
                permissionGroupDTO.setName(permissionGroup.getName());
                permissionGroupDTO.setPermissions(permissionsInGroup.stream().map(this::buildPermissionDTO).toList());
                permissionGroupDTOs.add(permissionGroupDTO);
            }
        }

        return permissionGroupDTOs;
    }

    public List<PermissionDTO> findPermissionByIds(Collection<Long> ids) {
        return permissionRepository.findByIds(ids).stream()
            .map(this::buildPermissionDTO)
            .collect(Collectors.toList());
    }

    public Map<Long, PermissionDTO> findPermissionByIdsAsMap(Collection<Long> ids) {
        return permissionRepository.findByIds(ids).stream()
            .map(this::buildPermissionDTO)
            .collect(Collectors.toUnmodifiableMap(PermissionDTO::getId, Function.identity()));
    }

    public void validatePermissionIds(Collection<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            var notFoundIds = new HashSet<>(ids);
            notFoundIds.removeAll(permissionRepository.findByIdsAsMap(ids).keySet());
            BizAssert.isTrue(notFoundIds.isEmpty(), "Permission Ids {0} is not found", notFoundIds);
        }
    }

    private PermissionDTO buildPermissionDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        permissionDTO.setName(permission.getName());
        permissionDTO.setRealmType(permission.getRealmType());
        permissionDTO.setDescription(permission.getDescription());
        return permissionDTO;
    }
}
