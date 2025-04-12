package com.ikeyit.account.infrastructure.security;


import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A user representation in security context
 */
public class UserPrincipal implements UserDetails, CredentialsContainer {

    private Long id;

    private String password;

    private String username;

    private String displayName;

    private String avatar;

    private String email;

    private String phone;

    private Set<? extends GrantedAuthority>  authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private String locale;

    public UserPrincipal() {
    }

    public UserPrincipal(Long id,
                         String username, String password,
                         String displayName,
                         String avatar,
                         String locale,
                         String email,
                         String phone,
                         Set<String> roles,
                         boolean enabled,
                         boolean accountNonExpired,
                         boolean accountNonLocked,
                         boolean credentialsNonExpired) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.displayName = displayName;
        this.avatar = avatar;
        this.locale = locale;
        this.email = email;
        this.phone = phone;
        this.authorities = roles == null ? Set.of() : roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toUnmodifiableSet());
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void eraseCredentials() {
        this.password = null;
    }
}
