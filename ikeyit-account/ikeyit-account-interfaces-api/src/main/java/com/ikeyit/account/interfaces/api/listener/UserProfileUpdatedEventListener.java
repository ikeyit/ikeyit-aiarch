package com.ikeyit.account.interfaces.api.listener;

import com.ikeyit.account.domain.event.UserLocaleUpdatedEvent;
import com.ikeyit.account.domain.event.UserProfileUpdatedEvent;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.common.storage.ObjectStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.function.Consumer;

@Component
@SuppressWarnings("all")
public class UserProfileUpdatedEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserProfileUpdatedEventListener.class);
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public final FindByIndexNameSessionRepository sessionRepository;

    private final ObjectStorageService objectStorageService;

    public UserProfileUpdatedEventListener(FindByIndexNameSessionRepository sessionRepository, ObjectStorageService objectStorageService) {
        this.sessionRepository = sessionRepository;
        this.objectStorageService = objectStorageService;
    }

    @TransactionalEventListener
    public void refreshSessionOnUserProfileUpdatedEvent(UserProfileUpdatedEvent event) {
        updateUserPrincipal(event.getUserId(), userPrincipal -> {
            userPrincipal.setAvatar(objectStorageService.getCdnUrl(event.getAvatar()));
            userPrincipal.setDisplayName(event.getDisplayName());
        });
    }

    @TransactionalEventListener
    public void refreshSessionOnUserLocaleUpdatedEvent(UserLocaleUpdatedEvent event) {
        updateUserPrincipal(event.getUserId(), userPrincipal -> {
            userPrincipal.setLocale(event.getLocale());
        });
    }

    private void updateUserPrincipal(Long userId, Consumer<UserPrincipal> consumer) {
        log.info("Refresh session of user {}", userId);
        Map<String, ? extends Session> sessionMap = sessionRepository.findByPrincipalName(userId.toString());
        for (Session session : sessionMap.values()) {
            if (session.getAttribute(SPRING_SECURITY_CONTEXT) instanceof SecurityContext securityContext) {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                    consumer.accept(userPrincipal);
                    // force delta update
                    session.setAttribute(SPRING_SECURITY_CONTEXT, securityContext);
                    sessionRepository.save(session);
                }
            }
        }
    }
}