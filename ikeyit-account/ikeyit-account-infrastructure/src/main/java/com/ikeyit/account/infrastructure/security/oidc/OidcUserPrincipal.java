package com.ikeyit.account.infrastructure.security.oidc;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;
import java.util.Set;

public class OidcUserPrincipal extends UserPrincipal implements OidcUser {

    public OidcUserPrincipal(Long id, String username, String password, String displayName, String avatar, String locale, String email, String phone, Set<String> roles, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired) {
        super(id, username, password, displayName, avatar, locale, email, phone, roles, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired);
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return getId().toString();
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    @JsonIgnore
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    @JsonIgnore
    public OidcIdToken getIdToken() {
        return null;
    }
}
