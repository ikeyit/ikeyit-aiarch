package com.ikeyit.account.interfaces.api.auth.oidc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.function.Consumer;

public class OidcLoginExtraAuthorizationRequestCustomizer implements Consumer<OAuth2AuthorizationRequest.Builder> {
    @Override
    public void accept(OAuth2AuthorizationRequest.Builder builder) {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes == null)
            return;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String redirectAfterAuth = request.getParameter("redirect");
        if (redirectAfterAuth != null) {
            builder.attributes(params -> params.put("redirect", redirectAfterAuth));
        }
    }
}
