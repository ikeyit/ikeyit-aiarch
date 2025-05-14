package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.RoleDTO;
import com.ikeyit.access.core.*;
import com.ikeyit.access.domain.model.Member;
import com.ikeyit.access.domain.model.MemberStatus;
import com.ikeyit.access.domain.model.RoleBinding;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.access.domain.repository.MemberRepository;
import com.ikeyit.access.domain.repository.RoleBindingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultAccessService implements AccessService {
    private static final Logger log = LoggerFactory.getLogger(DefaultAccessService.class);
    private final MemberRepository memberRepository;

    private final RoleBindingRepository roleBindingRepository;

    private final SystemRoleService systemRoleService;

    private final CustomRoleService customRoleService;

    public DefaultAccessService(MemberRepository memberRepository, RoleBindingRepository roleBindingRepository, SystemRoleService systemRoleService, CustomRoleService customRoleService) {
        this.memberRepository = memberRepository;
        this.roleBindingRepository = roleBindingRepository;
        this.systemRoleService = systemRoleService;
        this.customRoleService = customRoleService;
    }


    @Override
    public AccessDecision check(AccessRequest accessRequest) {
        var allRoles = getAllRoles(accessRequest.getRealmType(), accessRequest.getRealmId(), accessRequest.getUserId());
        var permissionSet = collectPermission(allRoles);
        return new AccessDecision(permissionSet.contains(accessRequest.getPermission()));
    }

    @Override
    public GrantedAuthorities getAuthorities(AuthoritiesRequest authoritiesRequest) {
        return memberRepository.findByUserId(authoritiesRequest.getUserId())
            .filter(member -> member.getStatus() == MemberStatus.ACTIVE)
            .map(member -> {
                var allRoles = getAllRoles(authoritiesRequest.getRealmType(), authoritiesRequest.getRealmId(), authoritiesRequest.getUserId());
                var permissionSet = collectPermission(allRoles);
                return new GrantedAuthorities(member.getId(), member.getDisplayName(), allRoles.stream()
                    .map(role -> role.getRoleType() == RoleType.SYSTEM ? "SYS_" + role.getName() : "CUS_" + role.getName())
                    .collect(Collectors.toUnmodifiableSet()),
                    permissionSet
                );
            }).orElseGet(GrantedAuthorities::new);
    }

    private List<RoleDTO> getAllRoles(String realmType, Long realmId, Long userId) {
        var roleBindings = roleBindingRepository.findByUserIdInRealm(realmType, realmId, userId);
        var systemRoleIds = roleBindings.stream()
            .filter(v -> v.getRoleType() == RoleType.SYSTEM)
            .map(RoleBinding::getRoleId).toList();
        var customRoleIds = roleBindings.stream()
            .filter(v -> v.getRoleType() == RoleType.CUSTOM)
            .map(RoleBinding::getRoleId).toList();
        var systemRoleDTOs = systemRoleService.findRoles(realmType, systemRoleIds, true);
        var customRoleDTOs = customRoleService.findRoles(realmType, realmId, customRoleIds, true);
        var all = new ArrayList<>(systemRoleDTOs);
        all.addAll(customRoleDTOs);
        return all;
    }

    private static Set<String> collectPermission(List<RoleDTO> roleDTOs) {
        var permissionSet = new HashSet<String>();
        for (var roleDTO : roleDTOs) {
            for (var permissionDTO : roleDTO.getPermissions()) {
                permissionSet.add(permissionDTO.getName());
            }
        }
        return permissionSet;
    }
}
