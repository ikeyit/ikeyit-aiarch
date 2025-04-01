package com.ikeyit.security.codeauth.web;

import com.ikeyit.security.code.core.*;
import com.ikeyit.security.codeauth.core.CodeAuthUserService;
import com.ikeyit.security.codeauth.core.InMemoryCodeAuthUserService;
import com.ikeyit.security.codeauth.core.VerificationCodeAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;

/**
 * A Spring Security configuration class for configuring the verification code authentication process.
 * This class extends AbstractHttpConfigurer and provides methods for configuring various aspects of the verification code authentication process.
 *
 * @param <B> The type of HttpSecurityBuilder.
 * @param <C> The type of the SecurityContextHolderStrategy.    
 */
public class VerificationCodeAuthenticationConfigurer<B extends HttpSecurityBuilder<B>>
		extends AbstractHttpConfigurer<VerificationCodeAuthenticationConfigurer<B>, B> {

    private VerificationCodeService verificationCodeService;

    private CodeAuthUserService codeAuthUserService;

    private String loginUrl;

    private AuthenticationFailureHandler failureHandler;

    private AuthenticationSuccessHandler successHandler;

    private String sendCodeUrl;

    private int resendInterval;

    private VerificationCodeSender verificationCodeSender;

    private VerificationCodeGenerator verificationCodeGenerator;

    private VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeAuthenticationConfigurer<B> verificationCodeService(VerificationCodeService verificationCodeService) {
        Assert.notNull(verificationCodeService, "verificationCodeService cannot be null");
        this.verificationCodeService = verificationCodeService;
        return this;
    }

    public VerificationCodeAuthenticationConfigurer<B> codeUserService(CodeAuthUserService codeAuthUserService) {
        Assert.notNull(codeAuthUserService, "codeUserService cannot be null");
        this.codeAuthUserService = codeAuthUserService;
        return this;
    }

    public VerificationCodeAuthenticationConfigurer<B> loginUrl(String loginUrl) {
        Assert.hasText(loginUrl, "loginUrl cannot be empty");
        this.loginUrl = loginUrl;
        return this;
    }


    public VerificationCodeAuthenticationConfigurer<B> sendCodeUrl(String sendCodeUrl) {
        Assert.hasText(sendCodeUrl, "sendCodeUrl cannot be empty");
        this.sendCodeUrl = sendCodeUrl;
        return this;
    }

    public VerificationCodeAuthenticationConfigurer<B> resendInterval(int resendInterval) {
        Assert.isTrue(resendInterval > 0, "resendInterval cannot be empty");
        this.resendInterval = resendInterval;
        return this;
    }

    public VerificationCodeAuthenticationConfigurer<B> verificationCodeSender(VerificationCodeSender verificationCodeSender) {
        Assert.notNull(verificationCodeSender, "verificationCodeSender cannot be null");
        this.verificationCodeSender = verificationCodeSender;
        return this;
    }


    public VerificationCodeAuthenticationConfigurer<B> verificationCodeGenerator(VerificationCodeGenerator verificationCodeGenerator) {
        Assert.notNull(verificationCodeGenerator, "verificationCodeGenerator cannot be null");
        this.verificationCodeGenerator = verificationCodeGenerator;
        return this;
    }


    public VerificationCodeAuthenticationConfigurer<B> verificationCodeRepository(VerificationCodeRepository verificationCodeRepository) {
        Assert.notNull(verificationCodeRepository, "verificationCodeRepository cannot be null");
        this.verificationCodeRepository = verificationCodeRepository;
        return this;
    }


    private VerificationCodeService getVerificationCodeService(B builder) {
        if (this.verificationCodeService != null)
            return this.verificationCodeService;

        DefaultVerificationCodeService defaultVerificationCodeService = new DefaultVerificationCodeService(
            getVerificationCodeRepository(builder),
            getVerificationCodeSender(builder),
            getVerificationCodeGenerator(builder)
        );
        if (this.resendInterval > 0) {
            defaultVerificationCodeService.setResendInterval(this.resendInterval);
        }
        this.verificationCodeService = defaultVerificationCodeService;
        return this.verificationCodeService;
    }

    private VerificationCodeSender getVerificationCodeSender(B builder) {
        if (this.verificationCodeSender != null)
            return this.verificationCodeSender;

        this.verificationCodeSender = new ConsoleVerificationCodeSender();
        return this.verificationCodeSender;
    }

    private VerificationCodeGenerator getVerificationCodeGenerator(B builder) {
        if (this.verificationCodeGenerator != null)
            return this.verificationCodeGenerator;

        this.verificationCodeGenerator = new DefaultVerificationCodeGenerator();
        return this.verificationCodeGenerator;
    }

    private VerificationCodeRepository getVerificationCodeRepository(B builder) {
        if (this.verificationCodeRepository != null)
            return this.verificationCodeRepository;

        this.verificationCodeRepository = new InMemoryVerificationCodeRepository();
        return this.verificationCodeRepository;
    }


    private CodeAuthUserService getCodeUserService(B builder) {
        if (this.codeAuthUserService != null)
            return this.codeAuthUserService;

        this.codeAuthUserService = new InMemoryCodeAuthUserService();
        return this.codeAuthUserService;
    }

    public VerificationCodeAuthenticationConfigurer<B> successHandler(
            AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.successHandler = authenticationSuccessHandler;
        return this;
    }


    public VerificationCodeAuthenticationConfigurer<B> failureHandler(
            AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureHandler = authenticationFailureHandler;
        return this;
    }


    @Override
    public void init(B builder) {
        VerificationCodeAuthenticationProvider verificationCodeAuthenticationProvider =
                new VerificationCodeAuthenticationProvider(getVerificationCodeService(builder), getCodeUserService(builder));
        builder.authenticationProvider(verificationCodeAuthenticationProvider);
    }


    @Override
    public void configure(B builder) {
        VerificationCodeSenderFilter verificationCodeSenderFilter = new VerificationCodeSenderFilter(getVerificationCodeService(builder), this.sendCodeUrl);
        builder.addFilterBefore(verificationCodeSenderFilter, AbstractPreAuthenticatedProcessingFilter.class);
        VerificationCodeAuthenticationFilter verificationCodeAuthenticationFilter = new VerificationCodeAuthenticationFilter(this.loginUrl);
        verificationCodeAuthenticationFilter.setAuthenticationManager(builder
            .getSharedObject(AuthenticationManager.class));
        SessionAuthenticationStrategy sessionAuthenticationStrategy = builder
            .getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            verificationCodeAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        if (this.successHandler != null)
            verificationCodeAuthenticationFilter.setAuthenticationSuccessHandler(this.successHandler);
        if (this.failureHandler != null)
            verificationCodeAuthenticationFilter.setAuthenticationFailureHandler(this.failureHandler);

        RememberMeServices rememberMeServices = builder.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            verificationCodeAuthenticationFilter.setRememberMeServices(rememberMeServices);
        }

        SecurityContextRepository securityContextRepository = builder
            .getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository != null) {
            verificationCodeAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
        }

        verificationCodeAuthenticationFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        builder.addFilterAfter(verificationCodeAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
