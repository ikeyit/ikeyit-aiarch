package com.ikeyit.common.grpc.server.component;

import com.ikeyit.common.exception.BizException;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GrpcAdvice
public class GlobalGrpcExceptionAdvice {
    private static final Metadata.Key<String> BIZ_ERROR_CODE_KEY = Metadata.Key.of("biz_error_code", Metadata.ASCII_STRING_MARSHALLER);
    private static final Logger log = LoggerFactory.getLogger(GlobalGrpcExceptionAdvice.class);

    @GrpcExceptionHandler
    public StatusRuntimeException handleBizException(BizException e) {
        log.error("Failed to make a grpc call", e);
        Metadata metadata = new Metadata();
        metadata.put(BIZ_ERROR_CODE_KEY, Integer.toString(e.getErrorCode().value()));
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e).asRuntimeException(metadata);
    }

    @GrpcExceptionHandler
    public Status handleInvalidArgument(IllegalArgumentException e) {
        log.error("Failed to make a grpc call", e);
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleRuntimeException(RuntimeException e) {
        log.error("Failed to make a grpc call", e);
        return Status.INTERNAL.withDescription(e.getMessage()).withCause(e);
    }

}