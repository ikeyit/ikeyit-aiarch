package com.ikeyit.account.interfaces.api.oauthcontroller;

import com.ikeyit.account.interfaces.api.auth.authsession.AuthSessionService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connect/extension")
public class OidcExtensionController {

    private final ObjectProvider<AuthenticationManager> authManagerProvider;
    private final AuthSessionService authSessionService;

    public OidcExtensionController(
        @Qualifier("oAuth2AuthenticationManager")
        ObjectProvider<AuthenticationManager> authManagerProvider, AuthSessionService authSessionService
    ) {
        this.authManagerProvider = authManagerProvider;
        this.authSessionService = authSessionService;
    }

    @PostMapping("/end-session")
    public String endSession(String id_token_hint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(id_token_hint, "principal must not be null");
        var oidcLogoutAuthenticationToken = new OidcLogoutAuthenticationToken(id_token_hint, authentication,
            null,null,null,null);
        AuthenticationManager authenticationManager = authManagerProvider.getObject();
        var oidcLogoutAuthenticationResult = (OidcLogoutAuthenticationToken) authenticationManager
            .authenticate(oidcLogoutAuthenticationToken);
        OidcIdToken oidcIdToken = oidcLogoutAuthenticationResult.getIdToken();
        if (oidcIdToken != null) {
            String asid = oidcIdToken.getClaim("asid");
            authSessionService.deleteAuthSession(asid);
        }
        return "OK";
    }
}
