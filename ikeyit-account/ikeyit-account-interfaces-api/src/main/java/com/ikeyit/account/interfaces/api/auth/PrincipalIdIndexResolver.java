package com.ikeyit.account.interfaces.api.auth;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.IndexResolver;
import org.springframework.session.Session;

import java.util.Map;

/**
 * Use user id instead of name to index session
 */
public class PrincipalIdIndexResolver<S extends Session> implements IndexResolver<S> {
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public PrincipalIdIndexResolver() {

    }

    @Override
    public Map<String, String> resolveIndexesFor(S session) {
        Long userId = null;
        if (session.getAttribute(SPRING_SECURITY_CONTEXT) instanceof SecurityContext securityContext) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                userId = userPrincipal.getId();
            }
        }

        return userId == null ? Map.of() : Map.of(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, userId.toString());
    }
}
