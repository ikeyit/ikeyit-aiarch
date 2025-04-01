package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.application.model.RegisterUserCMD;
import com.ikeyit.account.application.model.UserDTO;
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
    public UserDTO registerUser(@RequestBody RegisterUserCMD registerUserCMD) {
        return userService.registerUser(registerUserCMD);
    }

    @PostMapping("/auth/send-email-code")
    public void sendEmailCode(@RequestParam String email, @RequestParam String scenario) {

    }

    @PostMapping("/auth/send-mobile-code")
    public void sendMobileCode(@RequestParam String mobile, @RequestParam String email) {

    }
}
