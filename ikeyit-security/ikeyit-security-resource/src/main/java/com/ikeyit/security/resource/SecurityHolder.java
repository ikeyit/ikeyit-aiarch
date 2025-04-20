package com.ikeyit.security.resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHolder {
    public static Long userId() {
        AuthenticatedUser authenticatedUser = authenticatedUser();
        if (authenticatedUser == null) {
            throw new IllegalStateException("User is not logged in!");
        }
        return authenticatedUser.getId();
    }

    public static AuthenticatedUser authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof AuthenticatedUser userPrincipal) {
                return userPrincipal;
            }
        }
        return null;
    }

    public static AuthenticatedUser authenticatedUserOrThrow() {
        AuthenticatedUser authenticatedUser = authenticatedUser();
        if (authenticatedUser == null) {
            throw new IllegalArgumentException("Tenant context is not found!");
        }
        return authenticatedUser;
    }
}
