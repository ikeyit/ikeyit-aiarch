package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.application.model.SignupCMD;
import com.ikeyit.account.application.model.UserDTO;
import com.ikeyit.account.application.model.VerifySignupResultDTO;
import com.ikeyit.account.application.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles new user registration and creates a new user account in the system.
     */
    @PostMapping("/auth/signup")
    public UserDTO signup(@RequestBody SignupCMD signupCMD) {
        return userService.signup(signupCMD);
    }

    /**
     * Verify the email or mobile for signup
     */
    @PostMapping("/auth/signup/verify")
    public VerifySignupResultDTO verifySignup(@RequestParam String target) {
        return userService.verifySignup(target);
    }
}
