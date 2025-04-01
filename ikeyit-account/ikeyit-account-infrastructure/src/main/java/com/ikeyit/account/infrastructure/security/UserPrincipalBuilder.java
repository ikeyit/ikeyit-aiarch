package com.ikeyit.account.infrastructure.security;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.storage.ObjectStorageService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserPrincipalBuilder {

    private final ObjectStorageService objectStorageService;

    public UserPrincipalBuilder(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    public UserPrincipal buildUserPrincipal(User user) {
        Set<SimpleGrantedAuthority> grantedAuthorities = user.getRoles() == null ? Set.of() : user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toUnmodifiableSet());
        UserPrincipal userPrincipal = new UserPrincipal(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                grantedAuthorities);
        userPrincipal.setDisplayName(user.getDisplayName());
        userPrincipal.setAvatar(objectStorageService.getCdnUrl(user.getAvatar()));
        userPrincipal.setLocale(user.getLocale());
        return userPrincipal;
    }

}
