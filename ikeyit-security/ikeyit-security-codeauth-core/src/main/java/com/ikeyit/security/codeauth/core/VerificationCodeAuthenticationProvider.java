package com.ikeyit.security.codeauth.core;

import com.ikeyit.security.code.core.VerificationCodeException;
import com.ikeyit.security.code.core.VerificationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Authentication provider for verification code
 *
 */
public class VerificationCodeAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(VerificationCodeAuthenticationProvider.class);

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private final CodeAuthUserService codeAuthUserService;

    private final VerificationCodeService verificationCodeService;

    public VerificationCodeAuthenticationProvider(VerificationCodeService verificationCodeService, CodeAuthUserService codeAuthUserService) {
        this.codeAuthUserService = codeAuthUserService;
        this.verificationCodeService = verificationCodeService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            VerificationCodeAuthenticationToken authenticationToken = (VerificationCodeAuthenticationToken) authentication;
            String target = (String) authenticationToken.getPrincipal();
            String code = (String) authenticationToken.getCredentials();
            verificationCodeService.validate(target, code);
            UserDetails user = null;
            try {
                user = codeAuthUserService.loadUserByTarget(target);
            } catch (UsernameNotFoundException ex) {
                throw new BadCredentialsException(messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }

            if (user == null)
                throw new BadCredentialsException(messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            check(user);
            VerificationCodeAuthenticationToken authenticationResult = new VerificationCodeAuthenticationToken(user, user.getAuthorities());
            authenticationResult.setDetails(authenticationToken.getDetails());
            return authenticationResult;
        } catch (VerificationCodeException e) {
            throw new BadCredentialsException(e.getLocalizedMessage());
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return VerificationCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            log.debug("Failed to authenticate since user account is locked");
            throw new LockedException(messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
        }
        if (!user.isEnabled()) {
            log.debug("Failed to authenticate since user account is disabled");
            throw new DisabledException(messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            log.debug("Failed to authenticate since user account has expired");
            throw new AccountExpiredException(messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
        }
    }
}
