package com.ikeyit.account.infrastructure.security;


import com.ikeyit.account.domain.repository.UserRepository;
import com.ikeyit.security.codeauth.core.CodeAuthUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DefaultCodeAuthUserService implements CodeAuthUserService {

    private final UserRepository userRepository;

    private final UserPrincipalBuilder userPrincipalBuilder;

    public DefaultCodeAuthUserService(UserRepository userRepository, UserPrincipalBuilder userPrincipalBuilder) {
        this.userRepository = userRepository;
        this.userPrincipalBuilder = userPrincipalBuilder;
    }

    @Override
    public UserDetails loadUserByTarget(String target) {
        if (!StringUtils.hasText(target)) {
            throw new UsernameNotFoundException("target is blank!");
        }

        // Check if username is an email address
        if (target.contains("@")) {
            return userRepository.findByEmail(target)
                .map(userPrincipalBuilder::buildUserPrincipal)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));
        }

        // If not an email, try mobile number
        return userRepository.findByMobile(target)
            .map(userPrincipalBuilder::buildUserPrincipal)
            .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));
    }
}
