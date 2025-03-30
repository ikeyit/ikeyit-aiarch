package com.ikeyit.foo.interfaces.grpc.service;

import com.ikeyit.foo.application.service.FooService;
import com.ikeyit.foo.protobuf.Foo;
import com.ikeyit.foo.protobuf.FooServiceGrpc;
import com.ikeyit.foo.protobuf.GetAllFoosRequest;
import com.ikeyit.foo.protobuf.GetAllFoosResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class FooServiceGrpcImpl extends FooServiceGrpc.FooServiceImplBase {
    private final FooService fooService;

    public FooServiceGrpcImpl(FooService fooService) {
        this.fooService = fooService;
    }

    @Override
    public void getAllFoos(GetAllFoosRequest request, StreamObserver<GetAllFoosResponse> responseObserver) {
        var fooDTOList = fooService.findAll();
        var builder = GetAllFoosResponse.newBuilder();
        for (var fooDTO : fooDTOList) {
            builder.addFoos(Foo.newBuilder()
                    .setId(fooDTO.getId())
                    .setMessage(fooDTO.getMessage()));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}