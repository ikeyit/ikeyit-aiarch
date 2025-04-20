package com.ikeyit.account.interfaces.api.config;

import com.ikeyit.account.interfaces.api.auth.authsession.*;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AccountAuthSessionConfig {

    private final SecretKey secretKey;

    public AccountAuthSessionConfig(
        @Value("${spring.security.account.authsession.secret:}")
        String secret) {
        if (!StringUtils.hasLength(secret)) {
            secret = UUID.randomUUID().toString();
        }
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public AuthTokenGenerator authTokenGenerator() {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretKey)
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.HS256)
            .build();
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) ->
            jwkSelector.select(new JWKSet(jwk));
        return new AuthTokenGenerator(new NimbusJwtEncoder(jwkSource));
    }

    @Bean
    public AuthSessionService authSessionService(StringRedisTemplate stringRedisTemplate) {
        return new AuthSessionService(new RedisAuthSessionRepository(stringRedisTemplate));
    }

    @Bean
    public AuthTokenCookieRepository authTokenCookieRepository() {
        return new AuthTokenCookieRepository();
    }

    @Bean
    public AuthenticationManager authSessionAuthenticationManager(AuthSessionService authSessionService) {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(new AuthTokenAuthenticationConverter(authSessionService));
        return new ProviderManager(provider);
    }
}
