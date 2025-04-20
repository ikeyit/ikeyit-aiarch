package com.ikeyit.account.interfaces.api.auth.authsession;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.common.data.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.Instant;

/**
 * Manage the auth sessions
 */
public class AuthSessionService {
    private static final Logger logger = LoggerFactory.getLogger(AuthSessionService.class);

    private final AuthSessionRepository authSessionRepository;

    private Duration authSessionDuration = Duration.ofDays(7);

    public AuthSessionService(AuthSessionRepository authSessionRepository) {
        this.authSessionRepository = authSessionRepository;
    }

    public AuthSessionService(AuthSessionRepository authSessionRepository, Duration authSessionDuration) {
        this.authSessionRepository = authSessionRepository;
        this.authSessionDuration = authSessionDuration;
    }

    protected AuthSession buildAuthSession(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            var authSession = new AuthSession();
            authSession.setCreatedAt(Instant.now());
            authSession.setExpiresAt(Instant.now().plus(authSessionDuration));
            authSession.setPrincipal(userPrincipal);
            authSession.setId(IdUtils.uuid());
            authSession.setMethod(authentication.getClass().getSimpleName());
            return authSession;
        }
        logger.debug("Failed to build AuthSession. Principal is not supported: {}", authentication.getPrincipal().getClass());
        return null;
    }


    public AuthSession getAuthSession(String id) {
        if (id == null) {
            return null;
        }
        return authSessionRepository.findById(id);
    }

    public void deleteAuthSession(String id) {
        if (id == null) {
            return;
        }
        logger.debug("Delete AuthSession: {}", id);
        authSessionRepository.deleteById(id);
    }

    public AuthSession createAuthSession(Authentication authentication) {
        AuthSession authSession = buildAuthSession(authentication);
        if (authSession != null) {
            logger.debug("Created AuthSession: {}", authSession.getId());
            authSessionRepository.save(authSession);
        }
        return authSession;
    }

    public void updateAllPrincipal(UserPrincipal userPrincipal) {
        logger.debug("update all principal {}", userPrincipal);
    }
}
