package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.application.model.SignupCMD;
import com.ikeyit.account.application.model.VerifySignupCMD;
import com.ikeyit.account.application.model.VerifySignupResultDTO;
import com.ikeyit.account.application.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AccountSignupController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    public AccountSignupController(
        UserService userService,
        @Qualifier("passwordAuthenticationManager")
        AuthenticationManager authenticationManager,
        AuthenticationSuccessHandler loginSuccessHandler
    ) {
        this.userService = userService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles new user registration and creates a new user account in the system.
     */
    @PostMapping("/signup")
    public void signup(@RequestBody SignupCMD signupCMD,
                       HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        userService.signup(signupCMD);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            signupCMD.getUsername(),
            signupCMD.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Verify the email or phone for signup
     */
    @PostMapping("/signup/verify")
    public VerifySignupResultDTO verifySignup(@RequestBody VerifySignupCMD  verifySignupCMD) {
        return userService.verifySignup(verifySignupCMD);
    }
}
