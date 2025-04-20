package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.application.model.*;
import com.ikeyit.account.application.service.UserService;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

@RestController
public class AccountUserSettingController {
    private final UserService userService;
    private final LocaleResolver localeResolver;
    public AccountUserSettingController(UserService userService, LocaleResolver localeResolver) {
        this.userService = userService;
        this.localeResolver = localeResolver;
    }

    /**
     * Retrieves the current user's profile information.
     */
    @GetMapping("/me/profile")
    public UserDTO getUser(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getUser(principal.getId());
    }

    /**
     * Updates the current user's profile information.
     */
    @PostMapping("/me/profile")
    public UserDTO updateProfile(@RequestBody UpdateUserProfileCMD updateUserProfileCMD,
                              @AuthenticationPrincipal UserPrincipal principal) {
        updateUserProfileCMD.setUserId(principal.getId());
        return userService.updateUserProfile(updateUserProfileCMD);
    }

    /**
     * Updates the user's locale preferences and sets the corresponding cookie.
     */
    @PostMapping("/me/locale")
    public UserDTO updateLocale(@RequestBody UpdateUserLocaleCMD updateUserLocaleCMD,
                              @AuthenticationPrincipal UserPrincipal principal,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        updateUserLocaleCMD.setUserId(principal.getId());
        UserDTO userDTO = userService.updateUserLocale(updateUserLocaleCMD);
        // set user's preferred locale to cookie
        if (updateUserLocaleCMD.getLocale() != null) {
            localeResolver.setLocale(request, response, StringUtils.parseLocale(updateUserLocaleCMD.getLocale()));
        }
        return userDTO;
    }

    /**
     * Updates the user's password after validating the current password.
     */
    @PostMapping("/me/password")
    public void updatePassword(@RequestBody UpdateUserPasswordCMD updateUserPasswordCMD,
                               @AuthenticationPrincipal UserPrincipal principal) {
        updateUserPasswordCMD.setUserId(principal.getId());
        userService.updatePassword(updateUserPasswordCMD);
    }

    @PostMapping("/me/email/verification")
    public void sendVerificationCodeForUpdateEmail(@RequestBody SendEmailVerificationCodeCMD sendEmailVerificationCodeCMD,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        sendEmailVerificationCodeCMD.setUserId(principal.getId());
        userService.sendVerificationCodeForUpdateEmail(sendEmailVerificationCodeCMD);
    }

    @PostMapping("/me/phone/verification")
    public void sendVerificationCodeForUpdatePhone(@RequestBody SendPhoneVerificationCodeCMD sendPhoneVerificationCodeCMD,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        sendPhoneVerificationCodeCMD.setUserId(principal.getId());
        userService.sendVerificationCodeForUpdatePhone(sendPhoneVerificationCodeCMD);
    }

    @PostMapping("/me/email")
    public void updateEmail(@RequestBody UpdateUserEmailCMD updateUserEmailCMD,
                            @AuthenticationPrincipal UserPrincipal principal) {
        updateUserEmailCMD.setUserId(principal.getId());
        userService.updateUserEmail(updateUserEmailCMD);
    }
    
    @PostMapping("/me/phone")
    public void updatePhone(@RequestBody UpdateUserPhoneCMD updateUserPhoneCMD,
                             @AuthenticationPrincipal UserPrincipal principal) {
        updateUserPhoneCMD.setUserId(principal.getId());
        userService.updateUserPhone(updateUserPhoneCMD);
    }
}
