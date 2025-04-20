package com.ikeyit.account.interfaces.api.listener;

import com.ikeyit.account.domain.event.UserLocaleUpdatedEvent;
import com.ikeyit.account.domain.event.UserProfileUpdatedEvent;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthSessionService;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthTokenAuthenticationToken;
import com.ikeyit.common.storage.ObjectStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.function.Consumer;

@Component
@SuppressWarnings("all")
public class UserProfileUpdatedEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserProfileUpdatedEventListener.class);
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public final AuthSessionService authSessionService;

    private final ObjectStorageService objectStorageService;

    public UserProfileUpdatedEventListener(AuthSessionService authSessionService, ObjectStorageService objectStorageService) {
        this.authSessionService = authSessionService;
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
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AuthTokenAuthenticationToken authToken) {
            UserPrincipal userPrincipal = authToken.getAuthSession().getPrincipal();
            consumer.accept(userPrincipal);
            authSessionService.updateAllPrincipal(userPrincipal);
        }
    }
}