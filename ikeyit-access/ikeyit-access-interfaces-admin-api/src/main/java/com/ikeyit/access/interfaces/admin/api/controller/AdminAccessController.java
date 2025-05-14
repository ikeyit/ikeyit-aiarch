package com.ikeyit.access.interfaces.admin.api.controller;

import com.ikeyit.access.application.model.*;
import com.ikeyit.access.application.service.*;
import com.ikeyit.common.data.IdUtils;
import com.ikeyit.common.data.Page;
import com.ikeyit.common.data.PageParam;
import com.ikeyit.common.data.SortParam;
import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.common.storage.PresignResult;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ikeyit.access.application.service.AccessConstant.MASTER_REALM_TYPE;

@RestController
public class AdminAccessController {
    private final InvitationService invitationService;
    private final MemberService memberService;
    private final OrgService orgService;
    private final ObjectStorageService objectStorageService;
    private final SystemRoleService systemRoleService;
    private final CustomRoleService customRoleService;
    private final PermissionService permissionService;

    public AdminAccessController(InvitationService invitationService,
                                 MemberService memberService,
                                 OrgService orgService,
                                 ObjectStorageService objectStorageService,
                                 SystemRoleService systemRoleService,
                                 CustomRoleService customRoleService,
                                 PermissionService permissionService) {
        this.invitationService = invitationService;
        this.memberService = memberService;
        this.orgService = orgService;
        this.objectStorageService = objectStorageService;
        this.systemRoleService = systemRoleService;
        this.customRoleService = customRoleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/org")
    public OrgDTO getOrg() {
        return orgService.getOrg();
    }

    @PreAuthorize("hasAuthority('update.org.profile')")
    @PostMapping("/org")
    public OrgDTO updateOrg(@RequestBody UpdateOrgCMD updateOrgCMD) {
        return orgService.updateOrg(updateOrgCMD);
    }

    @PostMapping("/presign-upload")
    public PresignResult presignUpload() {
        return objectStorageService.presign("misc/" + IdUtils.uuid());
    }

    @GetMapping("/org/me/authorities")
    public Object getGrantedAuthorities(@AuthenticationPrincipal AuthenticatedUser user) {
        return user.getAttribute("grantedAuthorities");
    }

    @GetMapping("/org/roles")
    public List<RoleDTO> getOrgRoles() {
        var systemRoles = systemRoleService.findRoles(MASTER_REALM_TYPE, false);
        var customRoles = customRoleService.findRoles(MASTER_REALM_TYPE, 0L, false);
        var all = new ArrayList<>(systemRoles);
        all.addAll(customRoles);
        return all;
    }

    @GetMapping("/org/system-roles/{roleId}")
    public RoleDTO getSystemRole(@PathVariable Long roleId) {
        return systemRoleService.getRoleById(MASTER_REALM_TYPE, roleId, true);
    }

    @GetMapping("/org/custom-roles/{roleId}")
    public RoleDTO getCustomRole(@PathVariable Long roleId) {
        return customRoleService.getRoleById(MASTER_REALM_TYPE, 0L, roleId, true);
    }

    @GetMapping("/org/permissions")
    public List<PermissionGroupDTO> getPermissions() {
        return permissionService.findPermissionByRealmType(MASTER_REALM_TYPE);
    }

    @PreAuthorize("hasAuthority('update.member.roles')")
    @PostMapping("/org/member/roles")
    public void updateMemberRoles(@RequestBody  UpdateMemberRolesCMD updateMemberRolesCMD) {
        updateMemberRolesCMD.setAppended(false);
        updateMemberRolesCMD.setRealmType(MASTER_REALM_TYPE);
        updateMemberRolesCMD.setRealmId(0L);
        memberService.updateMemberRoles(updateMemberRolesCMD);
    }


    @PreAuthorize("hasAuthority('create.custom.role')")
    @PostMapping("/org/custom-roles")
    public RoleDTO createCustomRole(@RequestBody CreateCustomRoleCMD createCustomRoleCMD) {
        createCustomRoleCMD.setRealmType(MASTER_REALM_TYPE);
        createCustomRoleCMD.setRealmId(0L);
        return customRoleService.createRole(createCustomRoleCMD);
    }

    @PreAuthorize("hasAuthority('update.custom.role')")
    @PutMapping("/org/custom-roles/{roleId}")
    public RoleDTO updateCustomRole(@PathVariable Long roleId, @RequestBody UpdateCustomRoleCMD updateCustomRoleCMD) {
        updateCustomRoleCMD.setId(roleId);
        updateCustomRoleCMD.setRealmType(MASTER_REALM_TYPE);
        updateCustomRoleCMD.setRealmId(0L);
        return customRoleService.updateRole(updateCustomRoleCMD);
    }

    @GetMapping("/member-invitations/{id}")
    public InvitationToUserDTO getInvitationToUser(@AuthenticationPrincipal AuthenticatedUser user,
                                                   @PathVariable String id) {
        return invitationService.getInvitationToUser(id, user.getId());
    }

    @PostMapping("/member-invitations/{id}/accept")
    public AcceptInvitationResultDTO acceptInvitation(@AuthenticationPrincipal AuthenticatedUser user,
                                                      @PathVariable String id,
                                                      @RequestBody AcceptInvitationCMD acceptInvitationCMD) {
        acceptInvitationCMD.setId(id);
        acceptInvitationCMD.setInviteeId(user.getId());
        return invitationService.acceptInvitation(acceptInvitationCMD);
    }

    @PreAuthorize("hasAuthority('create.invitation')")
    @PostMapping("/invitations")
    public InvitationDTO createInvitation(@RequestBody CreateInvitationCMD createInvitationCMD,
                                          @AuthenticationPrincipal AuthenticatedUser user) {
        createInvitationCMD.setInviterId(user.getId());
        createInvitationCMD.setInviterName(user.getDisplayName());
        return invitationService.createInvitation(createInvitationCMD);
    }

    @GetMapping("/members")
    public Page<MemberDTO> findMembers(@RequestParam(required = false) String name, PageParam pageParam, SortParam sortParam) {
        return memberService.findMembers(MASTER_REALM_TYPE, 0L, name, pageParam, sortParam);
    }
}
