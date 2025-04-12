package com.ikeyit.account.interfaces.api.config;

import com.ikeyit.account.infrastructure.security.codeauth.CustomCodeAuthUserService;
import com.ikeyit.account.infrastructure.security.codeauth.LoginVerificationCodeService;
import com.ikeyit.account.infrastructure.security.oidc.CustomOidcUserService;
import com.ikeyit.account.interfaces.api.auth.*;
import com.ikeyit.account.interfaces.api.auth.oidc.PopupOidcAuthenticationSuccessHandler;
import com.ikeyit.security.codeauth.web.VerificationCodeAuthenticationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Basic security configuration, including api protection, password and verification authentication
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AccountSecurityConfig {
    private final static MediaTypeRequestMatcher JSON_REQUEST_MATCHER = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON);
    private final static MediaTypeRequestMatcher HTML_REQUEST_MATCHER = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
    private final static RequestMatcher ALL_MATCHER = request -> true;
    private final String loginUrl = "/login";
    static {
        JSON_REQUEST_MATCHER.setUseEquals(true);
    }

    /**
     * Handle the logic after users failed to login
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        var authenticationFailureHandler = new CompositeAuthenticationFailureHandler();
        // Response a failure json if the request expects json
        var jsonAuthenticationFailureHandler = new JsonAuthenticationFailureHandler();
        authenticationFailureHandler.addAuthenticationFailureHandler(jsonAuthenticationFailureHandler, JSON_REQUEST_MATCHER);
        // Response a redirect to login if the request expects html. Generally the request is sent by browser.
        var redirectAuthenticationFailureHandler = new RedirectAuthenticationFailureHandler(loginUrl);
        authenticationFailureHandler.addAuthenticationFailureHandler(redirectAuthenticationFailureHandler, HTML_REQUEST_MATCHER);
        return authenticationFailureHandler;
    }

    /**
     * Handle the logic after users login successfully
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(LocaleResolver localeResolver) {
        var authenticationSuccessHandler = new CompositeAuthenticationSuccessHandler();
        // Response a success json if the request expects json
        var jsonAuthenticationSuccessHandler = new JsonAuthenticationSuccessHandler();
        authenticationSuccessHandler.addAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler, JSON_REQUEST_MATCHER);
        // Response a redirect to a target url if the request expects html. Generally the request is sent by browser.
        var simpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler("/");
        simpleUrlAuthenticationSuccessHandler.setTargetUrlParameter("redirect");
        authenticationSuccessHandler.addAuthenticationSuccessHandler(simpleUrlAuthenticationSuccessHandler, HTML_REQUEST_MATCHER);
        // Set the user preferred locale in the cookie after login successfully
        var localeAuthenticationSuccessHandler = new LocaleAuthenticationSuccessHandler(localeResolver);
        authenticationSuccessHandler.addAuthenticationSuccessHandler(localeAuthenticationSuccessHandler, ALL_MATCHER);
        return authenticationSuccessHandler;
    }

    /**
     * Handle the logic when users requested a url without permissions
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        var authenticationEntryPoint = new CompositeAuthenticationEntryPoint();
        // If the client expects json, return a json with error message
        var jsonAuthenticationEntryPoint = new JsonAuthenticationEntryPoint();
        authenticationEntryPoint.addAuthenticationEntryPoint(jsonAuthenticationEntryPoint, JSON_REQUEST_MATCHER);
        // If the client expects html, meaning the client is browser, redirect the request to login
        var loginUrlAuthenticationEntryPoint = new RedirectAuthenticationEntryPoint(loginUrl);
        loginUrlAuthenticationEntryPoint.setRedirectParameter("redirect");
        authenticationEntryPoint.addAuthenticationEntryPoint(loginUrlAuthenticationEntryPoint, HTML_REQUEST_MATCHER);
        return authenticationEntryPoint;
    }

    /**
     * Store csrf token in session
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CookieCsrfTokenRepository();
    }

    /**
     * Store security context in session
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * How to encode the password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Configure security filter chain for auth related endpoints
     */
    @Bean
    @Order(2)
    public SecurityFilterChain accountDefaultSecurityFilterChain(HttpSecurity http,
                                                                 CsrfTokenRepository csrfTokenRepository,
                                                                 SecurityContextRepository securityContextRepository,
                                                                 AuthenticationEntryPoint authenticationEntryPoint,
                                                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                 AuthenticationFailureHandler authenticationFailureHandler,
                                                                 CustomCodeAuthUserService codeAuthUserService,
                                                                 LoginVerificationCodeService loginVerificationCodeService,
                                                                 @Autowired(required = false)
                                                                 ClientRegistrationRepository clientRegistrationRepository,
                                                                 CustomOidcUserService oidcUserService,
                                                                 @Autowired(required = false)
                                                                 RememberMeServices rememberMeServices
                                                                 ) throws Exception {
        http.securityMatcher("/auth/**")
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/auth/signup/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .securityContext(c->c.securityContextRepository(securityContextRepository))
            .csrf(c -> c.csrfTokenRepository(csrfTokenRepository))
            .formLogin(c -> c
                .loginPage(loginUrl)
                .loginProcessingUrl("/auth/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            .logout(c -> c.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", HttpMethod.GET.name())))
            .with(new VerificationCodeAuthenticationConfigurer<>(), c -> c
                .sendCodeUrl("/auth/send-code")
                .loginUrl("/auth/verify-code")
                .codeUserService(codeAuthUserService)
                .verificationCodeService(loginVerificationCodeService)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            // Handle the logic if a user requests an endpoint without permissions
            .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint))
            // Disable request cache. We use query parameter
            .requestCache(AbstractHttpConfigurer::disable)
            // Disable anonymous user
            .anonymous(AbstractHttpConfigurer::disable)
            // Disable remember me.
            .rememberMe(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);
        if (rememberMeServices != null) {
            http.rememberMe(c -> c.rememberMeServices(rememberMeServices));
        }
        if (clientRegistrationRepository != null) {
            http.oauth2Login(c -> c
                .successHandler(new PopupOidcAuthenticationSuccessHandler())
                .userInfoEndpoint(user -> user.oidcUserService(oidcUserService))
                .loginProcessingUrl("/auth/oauth2/code/*")
                .authorizationEndpoint(a -> a.baseUri("/auth/authorization"))
            );
        }
        return http.build();
    }

    /**
     * Configure for api related endpoints
     */
    @Bean
    @Order(3)
    public SecurityFilterChain accountApiSecurityFilterChain(HttpSecurity http,
                                                      CsrfTokenRepository csrfTokenRepository,
                                                      SecurityContextRepository securityContextRepository,
                                                      AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(c -> c
                    .requestMatchers("/api/session", "/api/oidc-providers")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
            .securityContext(c->c.securityContextRepository(securityContextRepository))
            .csrf(c -> c.csrfTokenRepository(csrfTokenRepository))
            // Disable useless filter to improve performance
            .requestCache(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

} 