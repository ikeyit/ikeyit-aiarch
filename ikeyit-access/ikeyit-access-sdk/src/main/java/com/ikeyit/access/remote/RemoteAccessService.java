package com.ikeyit.access.remote;

import com.ikeyit.access.core.*;
import com.ikeyit.access.protobuf.AccessServiceGrpc;
import com.ikeyit.access.protobuf.CheckAccessRequest;
import com.ikeyit.access.protobuf.GetAuthoritiesRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;

public class RemoteAccessService implements AccessService {

    private AccessServiceGrpc.AccessServiceBlockingStub accessServiceBlockingStub;

    @Override
    public AccessDecision check(AccessRequest accessRequest) {
        var request = CheckAccessRequest.newBuilder().setUserId(accessRequest.getUserId())
            .setPermission(accessRequest.getPermission())
            .setRealmType(accessRequest.getRealmType())
            .setRealmId(accessRequest.getRealmId()).build();
        var response = accessServiceBlockingStub.check(request);
        return new AccessDecision(response.getGranted());
    }

    @Override
    public GrantedAuthorities getAuthorities(AuthoritiesRequest authoritiesRequest) {
        var request = GetAuthoritiesRequest.newBuilder()
            .setUserId(authoritiesRequest.getUserId())
            .setRealmType(authoritiesRequest.getRealmType())
            .setRealmId(authoritiesRequest.getRealmId()).build();
        var response = accessServiceBlockingStub.getAuthorities(request);
        return new GrantedAuthorities(
            response.getMemberId(),
            response.getMemberDisplayName(),
            response.getRoleList(),
            response.getPermissionList());
    }

    @GrpcClient("access-service-grpc")
    public void setAccessServiceBlockingStub(AccessServiceGrpc.AccessServiceBlockingStub accessServiceBlockingStub) {
        this.accessServiceBlockingStub = accessServiceBlockingStub;
    }
}
