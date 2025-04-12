package com.ikeyit.account.interfaces.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.account.interfaces.api.auth.authorization.TokenCustomizer;
import com.ikeyit.account.interfaces.api.auth.jackson.AuthJacksonModule;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

/**
 * Enable authorization server and SSO (Single Sign-On) with OAuth2 and OIDC
 */
@Configuration(proxyBeanMethods = false)
public class Oauth2AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      SecurityContextRepository securityContextRepository,
                                                                      AuthenticationEntryPoint authenticationEntryPoint)
        throws Exception {
        // Handle all endpoints related to oauth2 authorization
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();
        return http
            .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
            .securityContext(c->c.securityContextRepository(securityContextRepository))
            .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint))
            .requestCache(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .with(authorizationServerConfigurer, c -> c
                .oidc(Customizer.withDefaults())
                .withObjectPostProcessor(new ObjectPostProcessor<OidcLogoutAuthenticationProvider>() {
                         @Override
                         public <O extends OidcLogoutAuthenticationProvider> O postProcess(O object) {
                             object.setAuthenticationValidator(authenticationContext -> {
                             });
                             return object;
                         }
                     }
                ))
            .build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/oauth2/**", configuration);
        source.registerCorsConfiguration("/.well-known/**", configuration);
        source.registerCorsConfiguration("/connect/**", configuration);
        return source;
    }

    /**
     * Set all endpoints exposed by authorization server
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .oidcUserInfoEndpoint("/connect/userinfo")
            .build();
    }

    @Bean
    @ConditionalOnProperty(name="spring.security.oauth2.authorizationserver.jwk-set-file")
    public JWKSource<SecurityContext> fileJwkSource(
        @Value("${spring.security.oauth2.authorizationserver.jwk-set-file}")
        String jwkSetFile) throws IOException, ParseException {
        JWKSet jwkSet = JWKSet.load(new File(jwkSetFile));
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }


    @Bean
    public TokenCustomizer tokenCustomizer() {
        return new TokenCustomizer();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
        @Qualifier("accountJdbcOperations")
        JdbcOperations jdbcOperations,
        RegisteredClientRepository registeredClientRepository) {
        var jdbcOAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);

        var authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
            registeredClientRepository);
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModules(new AuthJacksonModule());
        authorizationRowMapper.setObjectMapper(objectMapper);
        jdbcOAuth2AuthorizationService.setAuthorizationRowMapper(authorizationRowMapper);
        return jdbcOAuth2AuthorizationService;
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(
        @Qualifier("accountJdbcOperations")
        JdbcOperations jdbcOperations,
        RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
    }
}