package com.ikeyit.security.codeauth.web;

import com.ikeyit.security.codeauth.core.VerificationCodeAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * Filter for processing verification code based authentication requests.
 * 
 * <p>This filter intercepts requests to the configured login URL and extracts the target
 * (typically a phone number or email) and verification code from the request parameters.
 * It then creates an authentication token and passes it to the authentication manager for validation.</p>
 *
 * <p>By default, it processes POST requests to "/login-code" and expects parameters named
 * "target" and "code", but these can be customized.</p>
 */
public class VerificationCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * The default URL where this filter processes authentication requests.
     */
    public static final String DEFAULT_ENDPOINT_URI = "/login-code";
    
    /**
     * The default name of the request parameter containing the target (phone/email).
     */
    public static final String DEFAULT_TARGET_PARAMETER = "target";
    
    /**
     * The default name of the request parameter containing the verification code.
     */
    public static final String DEFAULT_CODE_PARAMETER = "code";
    
    /**
     * The name of the request parameter containing the target (phone/email).
     */
    private String targetParameter = DEFAULT_TARGET_PARAMETER;

    /**
     * The name of the request parameter containing the verification code.
     */
    private String codeParameter = DEFAULT_CODE_PARAMETER;

    /**
     * Creates a new instance with the default endpoint URI.
     */
    public VerificationCodeAuthenticationFilter() {
        this(DEFAULT_ENDPOINT_URI);
    }

    /**
     * Creates a new instance with the specified endpoint URI.
     *
     * @param endpointUri the URL where this filter will process authentication requests
     */
    public VerificationCodeAuthenticationFilter(String endpointUri) {
        super(new AntPathRequestMatcher(endpointUri == null ? DEFAULT_ENDPOINT_URI : endpointUri, HttpMethod.POST.name()));
    }

    /**
     * Creates a new instance with the specified request matcher.
     *
     * @param requestMatcher the matcher that determines which requests this filter processes
     */
    public VerificationCodeAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    /**
     * Attempts to authenticate the request using verification code authentication.
     * <p>
     * This method extracts the target and code from the request parameters, validates that
     * they are not empty, creates an authentication token, and passes it to the authentication
     * manager for validation.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the authenticated user token if successful
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String target = request.getParameter(targetParameter);
        String code = request.getParameter(codeParameter);
        if (!StringUtils.hasText(target) || !StringUtils.hasText(code))
            throw new BadCredentialsException("Target and code must not be empty or null");
        VerificationCodeAuthenticationToken authRequest = new VerificationCodeAuthenticationToken(target, code);
        setDetails(request, authRequest);
       return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Sets additional details about the authentication request.
     * <p>
     * This method uses the configured authentication details source to build details from the request
     * and sets them on the authentication token.
     *
     * @param request the HTTP request
     * @param authRequest the authentication token
     */
    protected void setDetails(HttpServletRequest request, VerificationCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the name of the request parameter containing the target (phone/email).
     *
     * @param targetParameter the name of the target parameter
     * @throws IllegalArgumentException if the provided parameter name is empty or null
     */
    public void setTargetParameter(String targetParameter) {
        Assert.hasText(targetParameter, "Target parameter must not be empty or null");
        this.targetParameter = targetParameter;
    }

    /**
     * Sets the name of the request parameter containing the verification code.
     *
     * @param codeParameter the name of the code parameter
     * @throws IllegalArgumentException if the provided parameter name is empty or null
     */
    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "Code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }
}





