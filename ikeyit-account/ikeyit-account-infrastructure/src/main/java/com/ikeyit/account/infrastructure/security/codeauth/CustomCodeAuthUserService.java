package com.ikeyit.account.infrastructure.security.codeauth;


import com.ikeyit.account.application.service.UserService;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.security.codeauth.core.CodeAuthUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomCodeAuthUserService implements CodeAuthUserService {

    private final UserService userService;

    public CustomCodeAuthUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByTarget(String target) {
        var user = userService.loadOrCreateUserByEmailOrPhone(target);
        return new UserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getDisplayName(),
            user.getAvatar(),
            user.getLocale(),
            user.getEmail(),
            user.getPhone(),
            user.getRoles(),
            user.isEnabled(),
            true,
            true,
            true);
    }
}
