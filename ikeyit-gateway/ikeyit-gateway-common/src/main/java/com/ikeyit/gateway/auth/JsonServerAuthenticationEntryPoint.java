package com.ikeyit.gateway.auth;


import com.ikeyit.common.data.JsonUtils;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JsonServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorResp errorResp = new ErrorResp(CommonErrorCode.AUTHENTICATION_REQUIRED, ex.getMessage());
        DataBuffer buffer = response.bufferFactory().wrap(JsonUtils.writeValueAsBytes(errorResp));
        return response.writeWith(Mono.just(buffer));
    }
}
