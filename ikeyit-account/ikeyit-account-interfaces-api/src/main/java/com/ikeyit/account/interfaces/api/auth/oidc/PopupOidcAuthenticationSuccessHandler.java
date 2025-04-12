package com.ikeyit.account.interfaces.api.auth.oidc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class PopupOidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String HTML_CLOSE_SELF = """
        <!DOCTYPE html>
        <html>
        <body>
        <script type="text/javascript">
        if (window.opener) {
            window.opener.postMessage("loginSuccess", "*");
        }
        window.close();
        </script>
        </body>
        </html>
        """;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication
        authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().print(HTML_CLOSE_SELF);
    }
}
