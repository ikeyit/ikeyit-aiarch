package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.PermissionDTO;
import com.ikeyit.access.application.model.RoleDTO;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.access.domain.model.SystemRole;
import com.ikeyit.access.domain.repository.SystemRoleRepository;
import com.ikeyit.common.exception.BizAssert;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemRoleService {

    private final SystemRoleRepository systemRoleRepository;

    private final PermissionService permissionService;

    public SystemRoleService(SystemRoleRepository systemRoleRepository, PermissionService permissionService) {
        this.systemRoleRepository = systemRoleRepository;
        this.permissionService = permissionService;
    }

    public RoleDTO getRoleById(String realmType, Long roleId, boolean includePermissions) {
        return systemRoleRepository.findById(roleId)
            .filter(v -> Objects.equals(realmType, v.getRealmType()))
            .map(v -> buildRoleDTO(v, includePermissions))
            .orElseThrow(BizAssert.failSupplier("Role is not found!"));
    }

    public RoleDTO getRoleByName(String realmType, String name, boolean includePermissions) {
        return systemRoleRepository.findByRealmAndName(realmType, name)
            .map(v -> buildRoleDTO(v, includePermissions))
            .orElseThrow(BizAssert.failSupplier("Role is not found!"));
    }

    public List<RoleDTO> findRoles(String realmType, Collection<Long> roleIds, boolean includePermissions) {
        return buildRoleDTOs(systemRoleRepository.findByRealmAndIds(realmType, roleIds), includePermissions);
    }

    public List<RoleDTO> findRoles(String realmType, boolean includePermissions) {
        return buildRoleDTOs(systemRoleRepository.findByRealm(realmType), includePermissions);
    }


    private List<RoleDTO> buildRoleDTOs(List<SystemRole> systemRoles, boolean includePermissions) {
        Map<Long, PermissionDTO> permissionMap = includePermissions ?
            permissionService.findPermissionByIdsAsMap(systemRoles.stream()
                .flatMap(role -> role.getPermissionIds().stream())
                .collect(Collectors.toSet())) : null;
        return systemRoles.stream()
            .map(v -> buildRoleDTO(v, permissionMap))
            .toList();
    }

    private RoleDTO buildRoleDTO(SystemRole systemRole, boolean includePermissions) {
        var roleDTO = buildRoleDTO(systemRole);
        if (includePermissions) {
            roleDTO.setPermissions(permissionService.findPermissionByIds(systemRole.getPermissionIds()));
        }
        return roleDTO;
    }


    private RoleDTO buildRoleDTO(SystemRole systemRole) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(systemRole.getId());
        roleDTO.setRoleType(RoleType.SYSTEM);
        roleDTO.setRealmType(systemRole.getRealmType());
        roleDTO.setName(systemRole.getName());
        roleDTO.setSupreme(systemRole.isSupreme());
        return roleDTO;
    }

    private RoleDTO buildRoleDTO(SystemRole systemRole, Map<Long, PermissionDTO> permissionMap) {
        RoleDTO roleDTO = buildRoleDTO(systemRole);
        if (permissionMap != null) {
            roleDTO.setPermissions(systemRole.getPermissionIds().stream()
                .map(permissionMap::get)
                .toList());
        }
        return roleDTO;
    }
}
