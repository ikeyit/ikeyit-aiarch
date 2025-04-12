package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.application.model.SignupCMD;
import com.ikeyit.account.application.model.UserAuthDTO;
import com.ikeyit.account.application.model.VerifySignupCMD;
import com.ikeyit.account.application.model.VerifySignupResultDTO;
import com.ikeyit.account.application.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationManager authenticationManager;
    public AuthController(UserService userService, SecurityContextRepository securityContextRepository, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles new user registration and creates a new user account in the system.
     */
    @PostMapping("/auth/signup")
    public void signup(@RequestBody SignupCMD signupCMD,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        UserAuthDTO userAuthDTO =  userService.signup(signupCMD);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            signupCMD.getUsername(),
            signupCMD.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    /**
     * Verify the email or phone for signup
     */
    @PostMapping("/auth/signup/verify")
    public VerifySignupResultDTO verifySignup(@RequestBody VerifySignupCMD  verifySignupCMD) {
        return userService.verifySignup(verifySignupCMD);
    }
}
