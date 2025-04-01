package com.ikeyit.security.codeauth.web;

import com.ikeyit.security.code.core.VerificationCode;
import com.ikeyit.security.code.core.VerificationCodeException;
import com.ikeyit.security.code.core.VerificationCodeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for handling verification code sending requests.
 * 
 * <p>This filter intercepts requests to the configured endpoint URL and extracts the target
 * (typically a phone number or email) from the request parameters. It then uses the
 * verification code service to generate and send a code to that target.</p>
 *
 * <p>By default, it processes POST requests to "/login-code/send" and expects a parameter named
 * "target", but these can be customized.</p>
 *
 * <p>The filter handles success and failure responses, setting appropriate HTTP status codes
 * and content types.</p>
 */
public class VerificationCodeSenderFilter extends OncePerRequestFilter {

    /**
     * The default URL where this filter processes code sending requests.
     */
    public static final String DEFAULT_ENDPOINT_URI = "/login-code/send";

    /**
     * The default name of the request parameter containing the target (phone/email).
     */
    public static final String DEFAULT_TARGET_PARAMETER = "target";

    /**
     * The matcher that determines which requests this filter processes.
     */
    private final RequestMatcher endpointMatcher;

    /**
     * The service responsible for generating and sending verification codes.
     */
    private final VerificationCodeService verificationCodeService;

    /**
     * The name of the request parameter containing the target (phone/email).
     */
    private String targetParameter = DEFAULT_TARGET_PARAMETER;

    /**
     * Creates a new instance with the default endpoint URI.
     *
     * @param verificationCodeService the service to use for generating and sending codes
     */
    public VerificationCodeSenderFilter(VerificationCodeService verificationCodeService) {
        this(verificationCodeService, DEFAULT_ENDPOINT_URI);
    }

    /**
     * Creates a new instance with the specified endpoint URI.
     *
     * @param verificationCodeService the service to use for generating and sending codes
     * @param endpointUri the URL where this filter will process code sending requests
     */
    public VerificationCodeSenderFilter(VerificationCodeService verificationCodeService, String endpointUri) {
        this.verificationCodeService = verificationCodeService;
        if (endpointUri == null)
            endpointUri = DEFAULT_ENDPOINT_URI;
        this.endpointMatcher = new AntPathRequestMatcher(endpointUri, HttpMethod.POST.name());
    }

    /**
     * Processes the HTTP request to send a verification code.
     * <p>
     * This method checks if the request matches the configured endpoint. If it does,
     * it extracts the target from the request parameters, validates that it is not empty,
     * and uses the verification code service to send a code to that target.
     * <p>
     * If the code is sent successfully, the success handler is called. If an exception
     * occurs, the failure handler is called.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain for passing the request to the next filter
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.endpointMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String target = request.getParameter(targetParameter);
        if (!StringUtils.hasText(target))
            throw new BadCredentialsException("Target and code must not be empty or null");
        try {
            VerificationCode verificationCode = verificationCodeService.sendCode(target);
            successHandler(request, response, verificationCode);
        } catch (VerificationCodeException e) {
            failureHandler(request, response, e);
        }
    }

    /**
     * Handles successful verification code sending.
     * <p>
     * This method is called when a code has been successfully generated and sent.
     * It sets the HTTP status to 200 OK and the content type to application/json.
     * <p>
     * Subclasses can override this method to customize the success response.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param verificationCode the verification code that was sent
     * @throws IOException if an I/O error occurs
     */
    protected void successHandler(HttpServletRequest request, HttpServletResponse response, VerificationCode verificationCode) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Handles failed verification code sending.
     * <p>
     * This method is called when an exception occurs during code generation or sending.
     * It sets the HTTP status to 400 Bad Request and the content type to application/json.
     * <p>
     * Subclasses can override this method to customize the failure response.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param ex the exception that occurred
     * @throws IOException if an I/O error occurs
     */
    protected void failureHandler(HttpServletRequest request, HttpServletResponse response, VerificationCodeException ex) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Sets the name of the request parameter containing the target (phone/email).
     *
     * @param targetParameter the name of the target parameter
     */
    public void setTargetParameter(String targetParameter) {
        this.targetParameter = targetParameter;
    }
}
