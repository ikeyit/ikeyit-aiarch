package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.CreateCustomRoleCMD;
import com.ikeyit.access.application.model.PermissionDTO;
import com.ikeyit.access.application.model.RoleDTO;
import com.ikeyit.access.application.model.UpdateCustomRoleCMD;
import com.ikeyit.access.domain.model.CustomRole;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.access.domain.repository.CustomRoleRepository;
import com.ikeyit.common.exception.BizAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomRoleService {

    private final CustomRoleRepository customRoleRepository;

    private final PermissionService permissionService;

    public CustomRoleService(CustomRoleRepository customRoleRepository, PermissionService permissionService) {
        this.customRoleRepository = customRoleRepository;
        this.permissionService = permissionService;
    }

    public RoleDTO getRoleById(String realmType, Long realmId, Long roleId, boolean includePermissions) {
        return customRoleRepository.findById(roleId)
            .filter(v -> Objects.equals(realmType, v.getRealmType()) && Objects.equals(realmId, v.getRealmId()))
            .map(v -> buildRoleDTO(v, includePermissions))
            .orElseThrow(BizAssert.failSupplier("Role is not found!"));
    }

    public RoleDTO getRoleByName(String realmType, Long realmId, String name, boolean includePermissions) {
        return customRoleRepository.findByRealmAndName(realmType, realmId, name)
            .map(v -> buildRoleDTO(v, includePermissions))
            .orElseThrow(BizAssert.failSupplier("Role is not found!"));
    }

    public List<RoleDTO> findRoles(String realmType, Long realmId, Collection<Long> roleIds, boolean includePermissions) {
        return buildRoleDTOs(customRoleRepository.findByRealmAndIds(realmType, realmId, roleIds), includePermissions);
    }

    public List<RoleDTO> findRoles(String realmType, Long realmId, boolean includePermissions) {
        return buildRoleDTOs(customRoleRepository.findByRealm(realmType, realmId), includePermissions);
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public RoleDTO createRole(CreateCustomRoleCMD createCustomRoleCMD) {
        BizAssert.notNull(createCustomRoleCMD, "createCustomRoleCMD must not be null!");
        String name = createCustomRoleCMD.getName();
        String realmType = createCustomRoleCMD.getRealmType();
        Long realmId = createCustomRoleCMD.getRealmId();
        List<Long> permissionIds = createCustomRoleCMD.getPermissionIds();

        BizAssert.notEmpty(permissionIds, "permissionIds must not be empty!");
        permissionService.validatePermissionIds(permissionIds);
        customRoleRepository.findByRealmAndName(realmType, realmId, name)
            .ifPresent(BizAssert.failAction("Duplicate role name!"));
        CustomRole customRole = new CustomRole(realmType, realmId, name, permissionIds);
        customRoleRepository.create(customRole);
        return buildRoleDTO(customRole, false);
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public RoleDTO updateRole(UpdateCustomRoleCMD updateCustomRoleCMD) {
        BizAssert.notNull(updateCustomRoleCMD, "updateCustomRoleCMD must not be null!");
        Long id = updateCustomRoleCMD.getId();
        List<Long> permissionIds = updateCustomRoleCMD.getPermissionIds();
        BizAssert.notEmpty(permissionIds, "permissionIds must not be empty!");
        permissionService.validatePermissionIds(permissionIds);
        CustomRole customRole = customRoleRepository.findById(id)
            .filter(role -> Objects.equals(role.getRealmType(), updateCustomRoleCMD.getRealmType()) && Objects.equals(role.getRealmId(), updateCustomRoleCMD.getRealmId()))
            .orElseThrow(BizAssert.failSupplier("Role is not found!"));
        if (!Objects.equals(customRole.getName(), updateCustomRoleCMD.getName())) {
            customRoleRepository.findByRealmAndName(updateCustomRoleCMD.getRealmType(), updateCustomRoleCMD.getRealmId(), updateCustomRoleCMD.getName())
                .ifPresent(BizAssert.failAction("Duplicate role name!"));
        }
        customRole.update(updateCustomRoleCMD.getName(), permissionIds);
        customRoleRepository.update(customRole);
        return buildRoleDTO(customRole, false);
    }

    private List<RoleDTO> buildRoleDTOs(List<CustomRole> customRoles, boolean includePermissions) {
        Map<Long, PermissionDTO> permissionMap = includePermissions ?
            permissionService.findPermissionByIdsAsMap(customRoles.stream()
                .flatMap(role -> role.getPermissionIds().stream())
                .collect(Collectors.toSet())) : null;
        return customRoles.stream()
            .map(v -> buildRoleDTO(v, permissionMap))
            .toList();
    }

    private RoleDTO buildRoleDTO(CustomRole customRole, boolean includePermissions) {
        var roleDTO = buildRoleDTO(customRole);
        if (includePermissions) {
            roleDTO.setPermissions(permissionService.findPermissionByIds(customRole.getPermissionIds()));
        }
        return roleDTO;
    }

    private RoleDTO buildRoleDTO(CustomRole customRole) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(customRole.getId());
        roleDTO.setRoleType(RoleType.CUSTOM);
        roleDTO.setRealmType(customRole.getRealmType());
        roleDTO.setName(customRole.getName());
        return roleDTO;
    }

    private RoleDTO buildRoleDTO(CustomRole customRole, Map<Long, PermissionDTO> permissionMap) {
        RoleDTO roleDTO = buildRoleDTO(customRole);
        if (permissionMap != null) {
            roleDTO.setPermissions(customRole.getPermissionIds().stream()
                .map(permissionMap::get)
                .toList());
        }
        return roleDTO;
    }
}
