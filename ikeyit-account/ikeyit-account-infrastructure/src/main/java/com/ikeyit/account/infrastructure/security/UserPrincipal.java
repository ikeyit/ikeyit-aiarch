package com.ikeyit.account.infrastructure.security;


import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

/**
 * A user representation in security context
 */
public class UserPrincipal implements UserDetails, CredentialsContainer {

    private Long id;

    private String password;

    private String username;

    private String displayName;

    private String avatar;

    private Set<? extends GrantedAuthority>  authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private String locale;

    public UserPrincipal() {
    }

    public UserPrincipal(Long id, String username, Set<? extends GrantedAuthority>  authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public UserPrincipal(Long id, String username, String password, boolean enabled,
                                boolean accountNonExpired, boolean credentialsNonExpired,
                                boolean accountNonLocked, Set<? extends GrantedAuthority>  authorities) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Cannot pass null or empty values to constructor");
        }
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(authorities);
    }


    public Long getId() {
        return id;
    }

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void eraseCredentials() {
        this.password = null;
    }
}
