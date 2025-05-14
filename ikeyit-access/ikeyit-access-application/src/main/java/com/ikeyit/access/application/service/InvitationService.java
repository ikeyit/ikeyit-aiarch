package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.*;
import com.ikeyit.access.domain.model.Invitation;
import com.ikeyit.access.domain.model.SystemRole;
import com.ikeyit.access.domain.repository.InvitationRepository;
import com.ikeyit.access.domain.repository.SystemRoleRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final MemberService memberService;

    private final SystemRoleRepository systemRoleRepository;

    public InvitationService(InvitationRepository invitationRepository, MemberService memberService, SystemRoleRepository systemRoleRepository) {
        this.invitationRepository = invitationRepository;
        this.memberService = memberService;
        this.systemRoleRepository = systemRoleRepository;
    }

    public InvitationDTO createInvitation(CreateInvitationCMD createInvitationCMD) {
        BizAssert.notNull(createInvitationCMD, "createInvitationCMD must not be null");
        BizAssert.notNull(createInvitationCMD.getRealmType(), "createInvitationCMD.RealmType must not be null");
        SystemRole systemRole = null;
        if (createInvitationCMD.getRoleId() == null) {
            systemRole = systemRoleRepository.findDefault(createInvitationCMD.getRealmType())
                .orElseThrow(BizAssert.failSupplier("Default system role is required"));
        } else {
            systemRole = systemRoleRepository.findById(createInvitationCMD.getRoleId())
                .orElseThrow(BizAssert.failSupplier("Role is not found"));
            BizAssert.isTrue(Objects.equals(systemRole.getRealmType(), createInvitationCMD.getRealmType()), "Role is not found");
        }
        Invitation invitation = Invitation.newBuilder()
            .realmType(systemRole.getRealmType())
            .realmId(createInvitationCMD.getRealmId())
            .roleId(systemRole.getId())
            .roleName(systemRole.getName())
            .orgName(createInvitationCMD.getOrgName())
            .inviterId(createInvitationCMD.getInviterId())
            .inviterName(createInvitationCMD.getInviterName())
            .email(createInvitationCMD.getEmail())
            .requireApproval(false)
            .build();
        invitationRepository.create(invitation);
        return buildInvitationDTO(invitation);
    }


    public InvitationDTO getInvitation(String id) {
        Invitation invitation = invitationRepository.findById(id)
            .orElseThrow(BizAssert.failSupplier("Invitation is not found or expired!"));
        return buildInvitationDTO(invitation);
    }

    public InvitationToUserDTO getInvitationToUser(String id, Long inviteeId) {
        BizAssert.notNull(inviteeId, "inviteeId must not be null");
        Invitation invitation = getExistingInvitation(id);
        BizAssert.isTrue(!Objects.equals(invitation.getInviterId(), inviteeId), "The invitation is from you. No action is required!");

        memberService.findMemberByUserId(inviteeId)
            .ifPresent(BizAssert.failAction(AccessErrorCode.ALREADY_MEMBER, "You already is a member of this organization"));
        var invitationToUserDTO = new InvitationToUserDTO();
        invitationToUserDTO.setId(invitation.getId());
        invitationToUserDTO.setInviterName(invitation.getInviterName());
        invitationToUserDTO.setOrgName(invitation.getOrgName());
        return invitationToUserDTO;
    }

    private Invitation getExistingInvitation(String id) {
        BizAssert.notNull(id, "id must not be null");
        return invitationRepository.findById(id)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND, "Invitation is not found or expired!"));
    }

    public AcceptInvitationResultDTO acceptInvitation(AcceptInvitationCMD acceptInvitationCMD) {
        BizAssert.notNull(acceptInvitationCMD.getInviteeId(), "inviteeId must not be null");
        var invitation = getExistingInvitation(acceptInvitationCMD.getId());
        BizAssert.isTrue(!Objects.equals(invitation.getInviterId(), acceptInvitationCMD.getInviteeId()), "The invitation is from you. No action is required!");

        var createMemberCMD = new CreateMemberCMD();
        createMemberCMD.setUserId(acceptInvitationCMD.getInviteeId());
        createMemberCMD.setDisplayName(acceptInvitationCMD.getDisplayName());
        createMemberCMD.setRoleId(invitation.getRoleId());
        createMemberCMD.setRealmType(invitation.getRealmType());
        createMemberCMD.setRealmId(invitation.getRealmId());
        createMemberCMD.setRequireApproval(invitation.isRequireApproval());
        var memberDTO = memberService.createMember(createMemberCMD);
        var acceptInvitationResultDTO = new AcceptInvitationResultDTO();
        acceptInvitationResultDTO.setMember(memberDTO);
        invitationRepository.delete(invitation);
        return acceptInvitationResultDTO;
    }

    private InvitationDTO buildInvitationDTO(Invitation invitation) {
        InvitationDTO invitationDTO = new InvitationDTO();
        invitationDTO.setId(invitation.getId());
        invitationDTO.setInviterName(invitation.getInviterName());
        invitationDTO.setOrgName(invitation.getOrgName());
        invitationDTO.setCreatedAt(invitation.getCreatedAt());
        return invitationDTO;
    }
}
