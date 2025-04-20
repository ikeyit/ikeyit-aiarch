package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthSession;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthTokenAuthenticationToken;
public class AuthJacksonModule extends SimpleModule {

    public AuthJacksonModule() {
        super(AuthJacksonModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserPrincipal.class, UserPrincipalMixin.class);
        context.setMixInAnnotations(AuthTokenAuthenticationToken.class, AuthTokenAuthenticationTokenMixin.class);
        context.setMixInAnnotations(AuthSession.class, AuthSessionMixin.class);
    }
}
