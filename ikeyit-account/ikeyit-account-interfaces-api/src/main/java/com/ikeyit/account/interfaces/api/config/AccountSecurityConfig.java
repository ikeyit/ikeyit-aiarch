package com.ikeyit.account.interfaces.api.config;

import com.ikeyit.account.infrastructure.security.codeauth.CustomCodeAuthUserService;
import com.ikeyit.account.infrastructure.security.codeauth.LoginVerificationCodeService;
import com.ikeyit.account.infrastructure.security.oidc.CustomOidcUserService;
import com.ikeyit.account.interfaces.api.auth.*;
import com.ikeyit.account.interfaces.api.auth.authsession.*;
import com.ikeyit.account.interfaces.api.auth.oidc.CookieAuthorizationRequestRepository;
import com.ikeyit.account.interfaces.api.auth.oidc.OidcLoginExtraAuthenticationResultConverter;
import com.ikeyit.account.interfaces.api.auth.oidc.OidcLoginExtraAuthorizationRequestCustomizer;
import com.ikeyit.account.interfaces.api.auth.oidc.OidcLoginRedirectAuthenticationSuccessHandler;
import com.ikeyit.security.codeauth.web.VerificationCodeAuthenticationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.servlet.LocaleResolver;

import static com.ikeyit.account.interfaces.api.config.AccountApiProperties.*;

/**
 * Basic security configuration, including api protection, password and verification authentication
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AccountSecurityConfig {
    private final static MediaTypeRequestMatcher JSON_REQUEST_MATCHER = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON);


    static {
        JSON_REQUEST_MATCHER.setUseEquals(true);
    }

    /**
     * Handle the logic after users failed to login
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        // Response a failure json if the request expects json
        var jsonAuthenticationFailureHandler = new JsonAuthenticationFailureHandler();
        // Response a redirect to login if the request expects html. Generally the request is sent by browser.
        var redirectAuthenticationFailureHandler = new RedirectAuthenticationFailureHandler(LOGIN_PAGE_URL);
        return (request, response, authentication) -> {
            if (JSON_REQUEST_MATCHER.matches(request)) {
                jsonAuthenticationFailureHandler.onAuthenticationFailure(request, response, authentication);
            } else {
                redirectAuthenticationFailureHandler.onAuthenticationFailure(request, response, authentication);
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new JsonAccessDeniedHandler();
    }

    /**
     * Handle the logic when users requested a url without permissions
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        // If the client expects json, return a json with error message
        var jsonAuthenticationEntryPoint = new JsonAuthenticationEntryPoint();
        // If the client expects html, meaning the client is browser, redirect the request to login
        var loginUrlAuthenticationEntryPoint = new RedirectAuthenticationEntryPoint(LOGIN_PAGE_URL);
        loginUrlAuthenticationEntryPoint.setRedirectParameter("redirect");
        return (request, response, authentication) -> {
            if (JSON_REQUEST_MATCHER.matches(request)) {
                jsonAuthenticationEntryPoint.commence(request, response, authentication);
            } else {
                loginUrlAuthenticationEntryPoint.commence(request, response, authentication);
            }
        };
    }

    /**
     * Store csrf token in session
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CookieCsrfTokenRepository();
    }

    /**
     * How to encode the password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(
        AuthSessionService authSessionService,
        AuthTokenGenerator authTokenGenerator,
        AuthTokenCookieRepository authTokenCookieRepository,
        LocaleResolver localeResolver
    ) {
        var loginAuthenticationSuccessHandler = new LoginAuthenticationSuccessHandler(
            authSessionService,
            authTokenGenerator,
            authTokenCookieRepository);
        var localeAuthenticationSuccessHandler = new LocaleAuthenticationSuccessHandler(localeResolver);
        return (request, response, authentication) -> {
            loginAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            localeAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        };
    }

    @Bean
    public LogoutHandler logoutHandler(
        AuthSessionService authSessionService,
        AuthTokenCookieRepository authTokenCookieRepository
    ) {
        return new AuthSessionLogoutHandler(authSessionService, authTokenCookieRepository);
    }
    /**
     * Configure security filter chain for auth related endpoints
     */
    @Bean
    public SecurityFilterChain accountDefaultSecurityFilterChain(
        HttpSecurity http,
        CsrfTokenRepository csrfTokenRepository,
        AuthenticationSuccessHandler loginSuccessHandler,
        LogoutHandler logoutHandler,
        AuthenticationEntryPoint authenticationEntryPoint,
        AuthenticationFailureHandler authenticationFailureHandler,
        AccessDeniedHandler accessDeniedHandler,
        @Qualifier("authSessionAuthenticationManager")
        AuthenticationManager authSessionAuthenticationManager,
        AuthTokenCookieRepository authTokenCookieRepository,
        CustomCodeAuthUserService codeAuthUserService,
        LoginVerificationCodeService loginVerificationCodeService,
        @Autowired(required = false)
        ClientRegistrationRepository clientRegistrationRepository,
        CustomOidcUserService oidcUserService
    ) throws Exception {
        var jsonAuthenticationSuccessHandler = new JsonAuthenticationSuccessHandler();
        var simpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler("/");
        simpleUrlAuthenticationSuccessHandler.setTargetUrlParameter("redirect");
        var oidcLoginRedirectAuthenticationSuccessHandler = new OidcLoginRedirectAuthenticationSuccessHandler();
        AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
            loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            if (JSON_REQUEST_MATCHER.matches(request)) {
                jsonAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            } else {
                simpleUrlAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            }
        };

        AuthenticationSuccessHandler oidcLoginAuthenticationSuccessHandler = (request, response, authentication) -> {
            loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            oidcLoginRedirectAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        };

        http.securityMatcher(AUTH_BASE_RUL + "/**")
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest()
                .authenticated()
            )
            .securityContext(AbstractHttpConfigurer::disable) // we do not save security context to any session or storage.
            .sessionManagement(AbstractHttpConfigurer::disable)
            .csrf(c -> c.csrfTokenRepository(csrfTokenRepository))
            .formLogin(c -> c
                .loginPage(LOGIN_PAGE_URL)
                .loginProcessingUrl(AUTH_BASE_RUL + "/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            .logout(c -> c
                .logoutRequestMatcher(new AntPathRequestMatcher(AUTH_BASE_RUL + "/logout", HttpMethod.GET.name()))
                .addLogoutHandler(logoutHandler)
            )
            .with(new VerificationCodeAuthenticationConfigurer<>(), c -> c
                .sendCodeUrl(AUTH_BASE_RUL + "/send-code")
                .loginUrl(AUTH_BASE_RUL + "/verify-code")
                .codeUserService(codeAuthUserService)
                .verificationCodeService(loginVerificationCodeService)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            // Handle the logic if a user requests an endpoint without permissions
            .exceptionHandling(c -> c
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            // Disable request cache. We use query parameter
            .requestCache(AbstractHttpConfigurer::disable)
            // Disable anonymous user
            .anonymous(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .with(new AuthSessionConfigurer<>(), c -> c
                .authTokenCookieRepository(authTokenCookieRepository)
                .authenticationManager(authSessionAuthenticationManager)
            );

        if (clientRegistrationRepository != null) {
            var authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                AUTH_BASE_RUL + "/authorization");
            // make oauth2 login callback able to redirect the original url
            authorizationRequestResolver.setAuthorizationRequestCustomizer(new OidcLoginExtraAuthorizationRequestCustomizer());
            http.oauth2Login(c -> c
                .withObjectPostProcessor(
                    new ObjectPostProcessor<OAuth2LoginAuthenticationFilter>() {
                        @Override
                        public <O extends OAuth2LoginAuthenticationFilter> O postProcess(O object) {
                            object.setAuthenticationResultConverter(new OidcLoginExtraAuthenticationResultConverter());
                            return object;
                        }
                    })
                .successHandler(oidcLoginAuthenticationSuccessHandler)
                .userInfoEndpoint(user -> user.oidcUserService(oidcUserService))
                // Callback redirected back by the IDP with authorization code
                .loginProcessingUrl(AUTH_BASE_RUL + "/oauth2/code/*")
                .authorizationEndpoint(a -> a
                    // Construct oidc authorization url for the IDP
                    .authorizationRequestRepository(new CookieAuthorizationRequestRepository())
                    .authorizationRequestResolver(authorizationRequestResolver)
                )
            );
        }
        return http.build();
    }

    /**
     * Configure for api related endpoints
     */
    @Bean
    public SecurityFilterChain accountApiSecurityFilterChain(
        HttpSecurity http,
        CsrfTokenRepository csrfTokenRepository,
        AuthenticationEntryPoint authenticationEntryPoint,
        AccessDeniedHandler accessDeniedHandler,
        @Qualifier("authSessionAuthenticationManager")
        AuthenticationManager authSessionAuthenticationManager,
        AuthTokenCookieRepository authTokenCookieRepository
    ) throws Exception {
        http
            .securityMatcher(API_BASE_URL + "/**")
            .authorizeHttpRequests(c -> c
                .requestMatchers(
                    API_BASE_URL + "/signup/**",
                    API_BASE_URL + "/session",
                    API_BASE_URL + "/oidc-providers")
                .permitAll()
                .anyRequest()
                .authenticated())
            .securityContext(AbstractHttpConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable)
            .csrf(c -> c.csrfTokenRepository(csrfTokenRepository))
            // Disable useless filter to improve performance
            .requestCache(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .with(new AuthSessionConfigurer<>(), c -> c
                .authTokenCookieRepository(authTokenCookieRepository)
                .authenticationManager(authSessionAuthenticationManager)
            )
            .exceptionHandling(c -> c
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );
        return http.build();
    }

    @Bean
    public AuthenticationManager passwordAuthenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

} 