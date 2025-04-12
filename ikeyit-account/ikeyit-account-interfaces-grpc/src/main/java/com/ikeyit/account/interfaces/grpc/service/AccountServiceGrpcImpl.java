package com.ikeyit.account.interfaces.grpc.service;

import com.ikeyit.account.application.model.UserDTO;
import com.ikeyit.account.application.service.UserService;
import com.ikeyit.account.protobuf.AccountServiceGrpc;
import com.ikeyit.account.protobuf.GetUserRequest;
import com.ikeyit.account.protobuf.GetUserResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AccountServiceGrpcImpl extends AccountServiceGrpc.AccountServiceImplBase {

    private final UserService accountService;

    public AccountServiceGrpcImpl(UserService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {
       UserDTO userDTO = accountService.getUser(request.getId());
       responseObserver.onNext(GetUserResponse.newBuilder()
           .setId(userDTO.getId())
           .setAvatar(userDTO.getAvatar())
           .setUsername(userDTO.getUsername())
           .setDisplayName(userDTO.getDisplayName())
           .setEmail(userDTO.getEmail())
           .setPhone(userDTO.getPhone())
           .build());
        responseObserver.onCompleted();
    }
}
