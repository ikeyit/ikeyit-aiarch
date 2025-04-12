package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.account.infrastructure.security.oidc.OidcUserPrincipal;
import com.ikeyit.security.codeauth.core.VerificationCodeAuthenticationToken;
public class AuthJacksonModule extends SimpleModule {

    public AuthJacksonModule() {
        super(AuthJacksonModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserPrincipal.class, UserPrincipalMixin.class);
        context.setMixInAnnotations(OidcUserPrincipal.class, OidcUserPrincipalMixin.class);
        context.setMixInAnnotations(VerificationCodeAuthenticationToken.class, VerificationCodeAuthenticationTokenMixin.class);
        context.setMixInAnnotations(Long.class, LongMixin.class);
    }

    abstract static class LongMixin {
        @SuppressWarnings("unused")
        @JsonProperty("long")
        Long value;
    }

}
