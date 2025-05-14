package com.ikeyit.gateway.admin.config;

import com.ikeyit.gateway.auth.*;
import com.ikeyit.gateway.filter.RefreshTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@EnableWebFluxSecurity
public class AdminGatewaySecurityConfig {
    public static final MediaTypeServerWebExchangeMatcher JSON_MATCHER = new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON);
    public static final MediaTypeServerWebExchangeMatcher HTML_MATCHER = new MediaTypeServerWebExchangeMatcher(MediaType.TEXT_HTML);
    static {
        JSON_MATCHER.setUseEquals(true);
    }
    @Value("${ikeyit.bff.authorizationRequestBaseUri:/auth/authorization/}")
    private String authorizationRequestBaseUri;

    @Bean
    @Order(2)
    public SecurityWebFilterChain adminGatewayDefaultSecurityWebFilterChain(ServerHttpSecurity http,
                                                                            ServerAuthenticationEntryPoint authenticationEntryPoint,
                                                                            ServerAccessDeniedHandler accessDeniedHandler,
                                                                            ServerCsrfTokenRepository serverCsrfTokenRepository,
                                                                            TokenRefreshService tokenRefreshService,
                                                                            ServerSecurityContextRepository securityContextRepository) {
        return http
            .securityMatcher(new NegatedServerWebExchangeMatcher(pathMatchers("/actuator/**", "/error", "/favicon.ico")))
            .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
            .authorizeExchange(authorize -> authorize.anyExchange().permitAll())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .securityContextRepository(securityContextRepository)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)
            .csrf(csrf -> csrf.csrfTokenRepository(serverCsrfTokenRepository))
            .addFilterAfter(new RefreshTokenFilter(tokenRefreshService), SecurityWebFiltersOrder.REACTOR_CONTEXT)
            .build();
    }

    @Bean
    @Order(1)
    public SecurityWebFilterChain adminGatewayAuthSecurityWebFilterChain(ServerHttpSecurity http,
                                                                         ServerAuthenticationEntryPoint authenticationEntryPoint,
                                                                         ServerAccessDeniedHandler accessDeniedHandler,
                                                                         ServerCsrfTokenRepository serverCsrfTokenRepository,
                                                                         ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                                         SessionServerOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
                                                                         ServerSecurityContextRepository securityContextRepository) {
        return http
            .securityMatcher(pathMatchers("/auth/**"))
            .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
            .authorizeExchange(authorize -> authorize.anyExchange().authenticated())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .securityContextRepository(securityContextRepository)
            .oauth2Login(c -> c
                .authenticationConverter(new AuthorizationCodeAuthenticationTokenConverter(clientRegistrationRepository))
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler())
                .authorizedClientRepository(oAuth2AuthorizedClientRepository)
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/login"))
                .authenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/auth/oauth2/code/{registrationId}"))
                .authorizationRequestResolver(new RedirectServerAuthorizationRequestResolver(clientRegistrationRepository,
                    new PathPatternParserServerWebExchangeMatcher(authorizationRequestBaseUri + "{registrationId}"))))
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .logout(logout -> logout
                .requiresLogout(new PathPatternParserServerWebExchangeMatcher("/auth/logout", HttpMethod.GET))
                .logoutSuccessHandler(serverLogoutSuccessHandler(clientRegistrationRepository)))
            .csrf(csrf -> csrf.csrfTokenRepository(serverCsrfTokenRepository))
//            .headers(headerSpec -> headerSpec
//                .contentSecurityPolicy(spec -> spec.policyDirectives("default-src 'self' 'unsafe-inline';")))
            .build();
    }

    @Bean
    public ServerCsrfTokenRepository serverCsrfTokenRepository() {
        var cookieServerCsrfTokenRepository = new CookieServerCsrfTokenRepository();
        cookieServerCsrfTokenRepository.setCookieName("adm_csrf");
        cookieServerCsrfTokenRepository.setCookieCustomizer(cookie -> cookie
                .sameSite("Strict"));
        return cookieServerCsrfTokenRepository;
    }

    private ServerLogoutSuccessHandler serverLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var serverLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        serverLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/login");
        return serverLogoutSuccessHandler;
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return new JsonServerAuthenticationEntryPoint();
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return new JsonServerAccessDeniedHandler();
    }

    @Bean
    public SessionServerOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(
        ReactiveClientRegistrationRepository clientRegistrationRepository
    ) {
        return new SessionServerOAuth2AuthorizedClientRepository(clientRegistrationRepository);
    }

    @Bean
    public SessionServerSecurityContextRepository serverSecurityContextRepository() {
        var sessionServerSecurityContextRepository = new SessionServerSecurityContextRepository();
        sessionServerSecurityContextRepository.setCacheSecurityContext(true);
        return sessionServerSecurityContextRepository;
    }

    @Bean
    public JwtDecoderFactory<ClientRegistration> jwtDecoderFactory() {
        return new OidcIdTokenDecoderFactory();
    }

    @Bean
    public TokenRefreshService tokenRefreshService(
        SessionServerOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
        SessionServerSecurityContextRepository serverSecurityContextRepository,
        JwtDecoderFactory<ClientRegistration> jwtDecoderFactory
    ) {
        return new TokenRefreshService(oAuth2AuthorizedClientRepository, serverSecurityContextRepository, jwtDecoderFactory);
    }
}
