package com.ikeyit.account.interfaces.api.auth.rememberme;

import com.ikeyit.account.application.service.UserService;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.Collection;

public class CustomRememberMeServices extends TokenBasedRememberMeServices {
    public CustomRememberMeServices(String key, UserService userService) {
        super(key, new DelegateUserDetailService(userService));
    }

    public CustomRememberMeServices(String key, UserService userService, RememberMeTokenAlgorithm encodingAlgorithm) {
        super(key, new DelegateUserDetailService(userService), encodingAlgorithm);
    }

    @Override
    protected String retrieveUserName(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId().toString();
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
        return ((DelegateUserDetails) userDetails).getDelegate();
    }

    /**
     * We use user id rather than username to track remember me. Use this service to handle this case
     */
    static class DelegateUserDetailService implements UserDetailsService {
        private final UserService userService;

        public DelegateUserDetailService(UserService userService) {
            this.userService = userService;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            var user = userService.loadUserAuth(Long.parseLong(username));
            var userPrincipal = new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getDisplayName(),
                user.getAvatar(),
                user.getLocale(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles(),
                user.isEnabled(),
                true,
                true,
                true);

            return new DelegateUserDetails(userPrincipal);
        }
    }

    static class DelegateUserDetails implements UserDetails {
        private final UserPrincipal delegate;

        public DelegateUserDetails(UserPrincipal delegate) {
            this.delegate = delegate;
        }

        public UserPrincipal getDelegate() {
            return delegate;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return delegate.getAuthorities();
        }

        @Override
        public String getPassword() {
            return delegate.getPassword();
        }

        @Override
        public String getUsername() {
            return delegate.getId().toString();
        }

        @Override
        public boolean isAccountNonExpired() {
            return delegate.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return delegate.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return delegate.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return delegate.isEnabled();
        }
    }
}
