package com.ikeyit.access.interfaces.grpc.service;

import com.ikeyit.access.application.service.DefaultAccessService;
import com.ikeyit.access.core.AccessRequest;
import com.ikeyit.access.core.AuthoritiesRequest;
import com.ikeyit.access.protobuf.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class AccessServiceGrpcImpl extends AccessServiceGrpc.AccessServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(AccessServiceGrpcImpl.class);
    private final DefaultAccessService defaultAccessService;

    public AccessServiceGrpcImpl(DefaultAccessService defaultAccessService) {
        this.defaultAccessService = defaultAccessService;
    }

    @Override
    public void check(CheckAccessRequest request, StreamObserver<CheckAccessResponse> responseObserver) {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setUserId(request.getUserId());
        accessRequest.setPermission(request.getPermission());
        accessRequest.setRealmId(request.getRealmId());
        accessRequest.setRealmType(request.getRealmType());
        var decision = defaultAccessService.check(accessRequest);
        responseObserver.onNext(CheckAccessResponse.newBuilder()
            .setGranted(decision.isGranted())
            .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAuthorities(GetAuthoritiesRequest request, StreamObserver<GetAuthoritiesResponse> responseObserver) {
        AuthoritiesRequest authoritiesRequest = new AuthoritiesRequest(
            request.getUserId(),
            request.getRealmId(),
            request.getRealmType());
        var result = defaultAccessService.getAuthorities(authoritiesRequest);
        responseObserver.onNext(GetAuthoritiesResponse.newBuilder()
            .setMemberId(result.getMemberId())
            .setMemberDisplayName(result.getMemberDisplayName())
            .addAllRole(result.getRoles())
            .addAllPermission(result.getPermissions())
            .build());
        responseObserver.onCompleted();
    }
}
