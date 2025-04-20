package com.ikeyit.security.resource;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

@Configuration(proxyBeanMethods = false)
public class ResourceSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "resourceAuthenticationEntryPoint")
    public JsonAuthenticationEntryPoint resourceAuthenticationEntryPoint() {
        return new JsonAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(name = "resourceAccessDeniedHandler")
    public JsonAccessDeniedHandler resourceAccessDeniedHandler() {
        return new JsonAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "resourceJwtAuthenticationConverter")
    public JwtAuthenticationConverter resourceJwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "resourceBearerTokenResolver")
    public BearerTokenResolver resourceBearerTokenResolver() {
        var composite = new CompositeBearerTokenResolver();
        composite.addBearerTokenResolver(new DefaultBearerTokenResolver());
        composite.addBearerTokenResolver(new CookieBearerTokenResolver());
        return composite;
    }

    // only for dev, use jwk-set-uri for the real jwks endpoint
    // Set it if you run standalone mode without account project
    @Bean
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-file")
    JwtDecoder jwtDecoderByJwkKeySetUri(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-file}") String jwkSetFile) throws IOException, ParseException {
        JWKSet jwkSet = JWKSet.load(new File(jwkSetFile));
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return new NimbusJwtDecoder(jwtProcessor);
    }

    @Bean
    public ResourceSecurityConfigurer resourceSecurityConfigurer(
        @Qualifier("resourceAuthenticationEntryPoint")
        AuthenticationEntryPoint authenticationEntryPoint,
        @Qualifier("resourceAccessDeniedHandler")
        AccessDeniedHandler accessDeniedHandler,
        @Qualifier("resourceJwtAuthenticationConverter")
        Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter,
        @Qualifier("resourceBearerTokenResolver")
        BearerTokenResolver bearerTokenResolver
    ) {
        return new ResourceSecurityConfigurer(
            authenticationEntryPoint,
            accessDeniedHandler,
            jwtAuthenticationConverter,
            bearerTokenResolver);
    }
}
