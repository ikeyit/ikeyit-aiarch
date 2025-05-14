package com.ikeyit.gateway.admin.controller;

import com.ikeyit.common.data.JsonUtils;
import com.ikeyit.common.exception.BizException;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("handle exception", ex);
        HttpStatusCode status;
        ErrorResp errorResp;
        switch (ex) {
            case AuthenticationException authenticationException -> {
                status = HttpStatus.UNAUTHORIZED;
                errorResp = new ErrorResp(CommonErrorCode.AUTHENTICATION_REQUIRED.name(), ex.getMessage());
            }
            case AccessDeniedException accessDeniedException -> {
                status = HttpStatus.FORBIDDEN;
                errorResp = new ErrorResp(CommonErrorCode.AUTHORIZATION_REQUIRED.name(), ex.getMessage());
            }
            case ResponseStatusException responseStatusException -> {
                status = responseStatusException.getStatusCode();
                errorResp = new ErrorResp(Integer.toString(status.value()), responseStatusException.getReason());
            }
            case BizException bizException -> {
                status = HttpStatus.BAD_REQUEST;
                errorResp = new ErrorResp(bizException.getErrorCode().name(), ex.getMessage());
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorResp = new ErrorResp(CommonErrorCode.INTERNAL_SERVER_ERROR.name(), "Unknown Error!");
                log.error("Internal server error", ex);
            }
        }

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(response
                .bufferFactory()
                .wrap(JsonUtils.writeValueAsBytes(errorResp))
        ));
    }
}