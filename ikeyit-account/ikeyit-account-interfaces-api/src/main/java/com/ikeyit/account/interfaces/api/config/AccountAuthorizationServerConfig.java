package com.ikeyit.account.interfaces.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.account.interfaces.api.auth.authorization.*;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthSessionConfigurer;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthTokenCookieRepository;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
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

import static com.ikeyit.account.interfaces.api.config.AccountApiProperties.AUTH_BASE_RUL;
import static com.ikeyit.account.interfaces.api.config.AccountApiProperties.LOGIN_PAGE_URL;

/**
 * Enable authorization server and SSO (Single Sign-On) with OAuth2 and OIDC
 */
@Configuration(proxyBeanMethods = false)
public class AccountAuthorizationServerConfig {

    private AuthenticationManager oAuth2AuthenticationManager;

    @Bean
    @Lazy
    public AuthenticationManager oAuth2AuthenticationManager() {
        return oAuth2AuthenticationManager;
    }

    // Replace the default token generators
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {
        // refer to this method for default configuration
//        OAuth2TokenEndpointConfigurer.createDefaultAuthenticationProviders()
        TokenCustomizer tokenCustomizer = new TokenCustomizer();
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(tokenCustomizer);
        // We replace the default refresh token generator to allow issue refresh tokens for public clients
        // TODO implement DPoP to enhance the client security for refresh token
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, new RefreshTokenGenerator());
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(
        HttpSecurity http,
        AuthenticationEntryPoint authenticationEntryPoint,
        LogoutHandler logoutHandler,
        CsrfTokenRepository csrfTokenRepository,
        @Qualifier("authSessionAuthenticationManager")
        AuthenticationManager authSessionAuthenticationManager,
        AuthTokenCookieRepository authTokenCookieRepository,
        RegisteredClientRepository registeredClientRepository) throws Exception {
        // Handle all endpoints related to oauth2 authorization
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();
        var logoutAuthenticationSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();
        logoutAuthenticationSuccessHandler.setLogoutHandler((request, response, authentication) -> {
            logoutHandler.logout(request, response, (Authentication) authentication.getPrincipal());
        });
        http
            .securityMatcher(new OrRequestMatcher(
                authorizationServerConfigurer.getEndpointsMatcher(),
                new AntPathRequestMatcher("/connect/extension/**")))
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/connect/extension/**").permitAll()
                .anyRequest()
                .authenticated())
            .exceptionHandling(c -> c
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .with(new AuthSessionConfigurer<>(), c -> c
                .authTokenCookieRepository(authTokenCookieRepository)
                .authenticationManager(authSessionAuthenticationManager)
            )
            .with(authorizationServerConfigurer, a -> a
                .authorizationEndpoint(b -> b
                    .authorizationRequestConverters(converters-> {
                        converters.removeIf(converter -> converter instanceof OAuth2AuthorizationCodeRequestAuthenticationConverter);
                        converters.addFirst(new AuthorizationCodeRequestAuthenticationConverter());
                    })
                    .errorResponseHandler(new AuthorizationCodeRequestAuthenticationFailureHandler(
                        LOGIN_PAGE_URL,
                        AUTH_BASE_RUL + "/authorization"
                    ))
                )
                .clientAuthentication(c -> c
                    .authenticationProvider(new RefreshPublicClientAuthenticationProvider(registeredClientRepository))
                    .authenticationConverter(new RefreshPublicClientAuthenticationConverter()))
                .oidc(b -> b
                    .logoutEndpoint(c -> c.logoutResponseHandler(logoutAuthenticationSuccessHandler))
                )
                .withObjectPostProcessor(new ObjectPostProcessor<OAuth2AuthorizationEndpointFilter>() {
                    @Override
                    public <O extends OAuth2AuthorizationEndpointFilter> O postProcess(O filter) {
                        filter.setSessionAuthenticationStrategy((authentication, request, response) -> {});
                        return filter;
                    }
                })
            )
            .securityContext(AbstractHttpConfigurer::disable) // We do not use the built-in session and security context mechanism
            .sessionManagement(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .requestCache(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            // Note: CSRF actually is useless for the oauth2 endpoints. Can not disable because the authorization server code
            .csrf(c -> c.csrfTokenRepository(csrfTokenRepository));
        var result = http.build();
        oAuth2AuthenticationManager = http.getSharedObject(AuthenticationManager.class);
        return result;
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
    @Primary
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    @Primary
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
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