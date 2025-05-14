package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.*;
import com.ikeyit.access.domain.model.*;
import com.ikeyit.access.domain.repository.CustomRoleRepository;
import com.ikeyit.access.domain.repository.MemberRepository;
import com.ikeyit.access.domain.repository.RoleBindingRepository;
import com.ikeyit.access.domain.repository.SystemRoleRepository;
import com.ikeyit.common.data.Page;
import com.ikeyit.common.data.PageParam;
import com.ikeyit.common.data.SortParam;
import com.ikeyit.common.exception.BizAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ikeyit.access.application.service.AccessConstant.MASTER_REALM_TYPE;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final RoleBindingRepository roleBindingRepository;

    private final SystemRoleRepository systemRoleRepository;

    private final CustomRoleRepository customRoleRepository;

    public MemberService(MemberRepository memberRepository, RoleBindingRepository roleBindingRepository, SystemRoleRepository systemRoleRepository, CustomRoleRepository customRoleRepository) {
        this.memberRepository = memberRepository;
        this.roleBindingRepository = roleBindingRepository;
        this.systemRoleRepository = systemRoleRepository;
        this.customRoleRepository = customRoleRepository;
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public void createOwner(Long ownerId) {
        var memberBuilder = Member.newBuilder()
            .userId(ownerId)
            .status(MemberStatus.ACTIVE)
            .displayName("Owner");
        var member = memberBuilder.build();
        memberRepository.create(member);
        SystemRole systemRole = systemRoleRepository.findSupreme(MASTER_REALM_TYPE)
            .orElseThrow(BizAssert.failSupplier("Supreme role is not found"));
        RoleBinding supremeRoleBinding = RoleBinding.newBuilder()
            .realmType(systemRole.getRealmType())
            .realmId(0L)
            .roleType(RoleType.SYSTEM)
            .roleId(systemRole.getId())
            .userId(ownerId)
            .build();
        roleBindingRepository.create(supremeRoleBinding);
    }

    public GrantSupremeRoleRO grantSupremeRole(String realmType, Long realmId, Long userId) {
        memberRepository.findByUserId(userId)
            .orElseThrow(BizAssert.failSupplier("User is not found"));
        SystemRole systemRole = systemRoleRepository.findSupreme(realmType)
            .orElseThrow(BizAssert.failSupplier("Supreme role is not found"));
        List<RoleBinding> existingRoleBindings = roleBindingRepository.findByRoleAndRealm(realmType, realmId, RoleType.SYSTEM, systemRole.getId());
        BizAssert.isTrue(existingRoleBindings.isEmpty(), "Supreme role exists");
        RoleBinding supremeRoleBinding = RoleBinding.newBuilder()
            .realmType(systemRole.getRealmType())
            .realmId(realmId)
            .roleType(RoleType.SYSTEM)
            .roleId(systemRole.getId())
            .userId(userId)
            .build();
        roleBindingRepository.create(supremeRoleBinding);
        var grantSupremeRoleResultDTO = new GrantSupremeRoleRO();
        grantSupremeRoleResultDTO.setRoleId(systemRole.getId());
        return grantSupremeRoleResultDTO;
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public void updateMemberRoles(UpdateMemberRolesCMD updateMemberRolesCMD) {
        Long userId = updateMemberRolesCMD.getUserId();
        String realmType = updateMemberRolesCMD.getRealmType();
        Long realmId = updateMemberRolesCMD.getRealmId();
        var roles = updateMemberRolesCMD.getRoles();
        BizAssert.notEmpty(roles, "At least one role id is required");
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(BizAssert.failSupplier("Member is not found"));
        var systemRoleIds = roles.stream().filter(r -> r.roleType() == RoleType.SYSTEM).map(MemberRoleCMD::id).collect(Collectors.toSet());
        List<SystemRole> systemRoles = systemRoleRepository.findByRealmAndIds(realmType, systemRoleIds);
        BizAssert.equals(systemRoles.size(), systemRoleIds.size(), "Some roles are not found");
        BizAssert.isTrue(systemRoles.stream().noneMatch(SystemRole::isSupreme), "Supreme role is not allowed to grant");

        var customRoleIds = roles.stream().filter(r -> r.roleType() == RoleType.CUSTOM).map(MemberRoleCMD::id).collect(Collectors.toSet());
        List<CustomRole> customRoles = customRoleRepository.findByRealmAndIds(realmType, realmId, customRoleIds);
        BizAssert.equals(customRoles.size(), customRoleIds.size(), "Some roles are not found");

        List<RoleBinding> roleBindings = roleBindingRepository.findByUserIdInRealm(realmType, realmId, userId);
        var toDelete = new ArrayList<RoleBinding>();
        var toCreate = new HashSet<>(roles);
        for (var roleBinding : roleBindings) {
            var existing = new MemberRoleCMD(roleBinding.getRoleId(), roleBinding.getRoleType());
            if (toCreate.contains(existing)) {
                toCreate.remove(existing);
            } else {
                toDelete.add(roleBinding);
            }
        }
        for (var memberRoleCmd : toCreate) {
            roleBindingRepository.create(RoleBinding.newBuilder()
                .realmType(realmType)
                .realmId(realmId)
                .roleId(memberRoleCmd.id())
                .roleType(memberRoleCmd.roleType())
                .userId(userId)
                .build());
        }

        if (!updateMemberRolesCMD.isAppended()) {
            for (var roleBinding : toDelete) {
                roleBinding.delete();
                roleBindingRepository.delete(roleBinding);
            }
        }
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public MemberDTO createMember(CreateMemberCMD createMemberCMD) {
        var member = memberRepository.findByUserId(createMemberCMD.getUserId()).orElseGet(() -> {
            var memberBuilder = Member.newBuilder()
                .userId(createMemberCMD.getUserId())
                .displayName(createMemberCMD.getDisplayName());
            if (createMemberCMD.isRequireApproval()) {
                memberBuilder.status(MemberStatus.UNAPPROVED);
            } else {
                memberBuilder.status(MemberStatus.ACTIVE);
            }
            var newMember = memberBuilder.build();
            memberRepository.create(newMember);
            return newMember;
        });
        if (createMemberCMD.getRealmType() == null || Objects.equals(createMemberCMD.getRealmType(), MASTER_REALM_TYPE)) {
            createSystemRoleBinding(
                MASTER_REALM_TYPE,
                0L,
                createMemberCMD.getUserId(),
                createMemberCMD.getRoleId()
            );
        } else {
            // Automatically create the default role
            createSystemRoleBinding(
                MASTER_REALM_TYPE,
                0L,
                createMemberCMD.getUserId(),
                null
            );
            createSystemRoleBinding(
                createMemberCMD.getRealmType(),
                createMemberCMD.getRealmId(),
                createMemberCMD.getUserId(),
                createMemberCMD.getRoleId()
            );
        }
        return buildBasicMemberDTO(member);
    }

    private RoleBinding createSystemRoleBinding(String realmType, Long realmId, Long userId, Long roleId) {
        SystemRole systemRole;
        if (roleId == null) {
            systemRole = systemRoleRepository.findDefault(realmType)
                .orElseThrow(BizAssert.failSupplier("Role is not found"));
        } else {
            systemRole = systemRoleRepository.findByRealmAndId(realmType, roleId)
                .orElseThrow(BizAssert.failSupplier("Role is not found"));
        }
        BizAssert.isTrue(!systemRole.isSupreme(), "Invalid system role");
        return roleBindingRepository.findByUserIdAndRoleIdInRealm(
            realmType,
            realmId,
            userId,
            RoleType.SYSTEM,
            systemRole.getId()
        ).orElseGet(() -> {
            RoleBinding roleBinding = RoleBinding.newBuilder()
                .userId(userId)
                .realmType(realmType)
                .realmId(realmId)
                .roleType(RoleType.SYSTEM)
                .roleId(systemRole.getId())
                .build();
            roleBindingRepository.create(roleBinding);
            return roleBinding;
        });
    }

    public Page<MemberDTO> findMembers(String realmType, Long realmId, String name, PageParam pageParam, SortParam sortParam) {
        var members = memberRepository.findInRealm(realmType, realmId, name, pageParam, sortParam);
        var userIds = members.getContent().stream().map(Member::getUserId).collect(Collectors.toSet());
        var roleBindings = roleBindingRepository.findByUserIdsInRealm(realmType, realmId, userIds);
        var roleBindingMap = roleBindings.stream().collect(Collectors.groupingBy(RoleBinding::getUserId));
        var systemRoleIds = roleBindings.stream()
            .filter(roleBinding -> roleBinding.getRoleType() == RoleType.SYSTEM)
            .map(RoleBinding::getRoleId)
            .collect(Collectors.toSet());
        var customRoleIds = roleBindings.stream()
            .filter(roleBinding -> roleBinding.getRoleType() == RoleType.CUSTOM)
            .map(RoleBinding::getRoleId)
            .collect(Collectors.toSet());
        var systemRoles = systemRoleRepository.findByRealmAndIdsAsMap(realmType, systemRoleIds);
        var customRoles = customRoleRepository.findByRealmAndIdsAsMap(realmType, realmId, customRoleIds);
        return members.map(member -> {
            MemberDTO memberDTO = buildBasicMemberDTO(member);
            memberDTO.setRoles(roleBindingMap.get(member.getUserId()).stream().map(roleBinding -> {
                MemberRoleDTO memberRoleDTO = new MemberRoleDTO();
                memberRoleDTO.setId(roleBinding.getRoleId());
                memberRoleDTO.setRoleType(roleBinding.getRoleType());
                memberRoleDTO.setName(roleBinding.getRoleType() == RoleType.SYSTEM ? systemRoles.get(roleBinding.getRoleId()).getName() :
                    customRoles.get(roleBinding.getRoleId()).getName());
                return memberRoleDTO;
            }).toList());
            return memberDTO;
        });
    }

    public Optional<MemberDTO> findMemberByUserId(Long userId) {
       return memberRepository.findByUserId(userId).map(this::buildBasicMemberDTO);
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public void approveMember(Long id) {
        Member member = getExistingMember(id);
        member.approve();
        memberRepository.update(member);
    }

    @Transactional(transactionManager = "accessTransactionManager")
    public void rejectMember(Long id) {
        Member member = getExistingMember(id);
        member.reject();
        memberRepository.update(member);
    }

    private MemberDTO buildBasicMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUserId(member.getUserId());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setDisplayName(member.getDisplayName());
        memberDTO.setCreatedAt(member.getCreatedAt());
        memberDTO.setUpdatedAt(member.getUpdatedAt());
        return memberDTO;
    }

    private Member getExistingMember(Long id) {
        return memberRepository.findById(id).orElseThrow(BizAssert.failSupplier("Member is not found!"));
    }
}
