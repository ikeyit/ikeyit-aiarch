package com.ikeyit.account.infrastructure.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
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

    private Set<String> roles;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private String locale;

    public UserPrincipal() {
    }

    public UserPrincipal(Long id,
                         String username,
                         String password,
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
        this.roles = roles;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return roles == null ? Set.of() : roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> getRoles() {
        return roles;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal principal = (UserPrincipal) o;
        return accountNonExpired == principal.accountNonExpired && accountNonLocked == principal.accountNonLocked && credentialsNonExpired == principal.credentialsNonExpired && enabled == principal.enabled && Objects.equals(id, principal.id) && Objects.equals(password, principal.password) && Objects.equals(username, principal.username) && Objects.equals(displayName, principal.displayName) && Objects.equals(avatar, principal.avatar) && Objects.equals(email, principal.email) && Objects.equals(phone, principal.phone) && Objects.equals(roles, principal.roles) && Objects.equals(locale, principal.locale);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(displayName);
        result = 31 * result + Objects.hashCode(avatar);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(phone);
        result = 31 * result + Objects.hashCode(roles);
        result = 31 * result + Boolean.hashCode(accountNonExpired);
        result = 31 * result + Boolean.hashCode(accountNonLocked);
        result = 31 * result + Boolean.hashCode(credentialsNonExpired);
        result = 31 * result + Boolean.hashCode(enabled);
        result = 31 * result + Objects.hashCode(locale);
        return result;
    }
}
