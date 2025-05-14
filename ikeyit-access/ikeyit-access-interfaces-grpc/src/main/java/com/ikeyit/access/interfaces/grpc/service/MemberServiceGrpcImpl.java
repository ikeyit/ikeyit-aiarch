package com.ikeyit.access.interfaces.grpc.service;

import com.ikeyit.access.application.model.GrantSupremeRoleRO;
import com.ikeyit.access.application.model.MemberRoleCMD;
import com.ikeyit.access.application.model.UpdateMemberRolesCMD;
import com.ikeyit.access.application.service.MemberService;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.access.protobuf.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MemberServiceGrpcImpl extends MemberServiceGrpc.MemberServiceImplBase {

    private final MemberService memberService;

    public MemberServiceGrpcImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void updateRoles(UpdateMemberRolesRequest request, StreamObserver<UpdateMemberRolesResponse> responseObserver) {
        var updateMemberRolesCMD = new UpdateMemberRolesCMD();
        updateMemberRolesCMD.setRealmType(request.getRealmType());
        updateMemberRolesCMD.setRealmId(request.getRealmId());
        updateMemberRolesCMD.setUserId(request.getUserId());
        updateMemberRolesCMD.setAppended(request.getAppended());
        updateMemberRolesCMD.setRoles(request.getRolesList().stream()
            .map(item -> new MemberRoleCMD(item.getRoleId(), RoleType.of(item.getRoleType())))
            .toList());
        memberService.updateMemberRoles(updateMemberRolesCMD);
        responseObserver.onNext(UpdateMemberRolesResponse.newBuilder()
            .build());
        responseObserver.onCompleted();
    }

    @Override
    public void grantSupremeRole(GrantSupremeRoleRequest request, StreamObserver<GrantSupremeRoleResponse> responseObserver) {
        GrantSupremeRoleRO grantSupremeRoleRO = memberService.grantSupremeRole(request.getRealmType(), request.getRealmId(), request.getUserId());
        responseObserver.onNext(GrantSupremeRoleResponse.newBuilder()
            .setRoleId(grantSupremeRoleRO.getRoleId())
            .build());
        responseObserver.onCompleted();
    }
}
