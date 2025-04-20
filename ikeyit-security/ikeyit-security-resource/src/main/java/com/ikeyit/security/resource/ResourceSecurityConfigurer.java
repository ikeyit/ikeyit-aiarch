package com.ikeyit.security.resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

public class ResourceSecurityConfigurer {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    private final Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

    private final BearerTokenResolver bearerTokenResolver;

    public ResourceSecurityConfigurer(AuthenticationEntryPoint authenticationEntryPoint,
                                      AccessDeniedHandler accessDeniedHandler,
                                      Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter,
                                      BearerTokenResolver bearerTokenResolver) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.bearerTokenResolver = bearerTokenResolver;
    }

    public HttpSecurity configure(HttpSecurity http) throws Exception {
        return configure(http, this.jwtAuthenticationConverter);
    }

    public HttpSecurity configure(HttpSecurity http, Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) throws Exception {
        return http.oauth2ResourceServer(c -> c
                .authenticationEntryPoint(authenticationEntryPoint)
                .bearerTokenResolver(bearerTokenResolver)
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)))
            .requestCache(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .exceptionHandling(c -> c
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler))
            .securityContext(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable);
    }
}
