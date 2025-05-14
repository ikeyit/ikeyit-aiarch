package com.ikeyit.account.infrastructure.security;


import com.ikeyit.account.application.service.UserService;
import com.ikeyit.common.exception.BizException;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PasswordAuthUserService implements UserDetailsService {

    private final UserService userService;

    private PasswordAuthUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) {
        var user = userService.loadUserAuth(username);
        if (!StringUtils.hasLength(user.getPassword())) {
            throw new BizException(CommonErrorCode.AUTHORIZATION_REQUIRED, "It's not allowed to login with password");
        }
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
