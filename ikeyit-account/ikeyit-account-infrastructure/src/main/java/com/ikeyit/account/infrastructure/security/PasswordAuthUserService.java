package com.ikeyit.account.infrastructure.security;


import com.ikeyit.account.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PasswordAuthUserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserPrincipalBuilder userPrincipalBuilder;

    public PasswordAuthUserService(UserRepository userRepository, UserPrincipalBuilder userPrincipalBuilder) {
        this.userRepository = userRepository;
        this.userPrincipalBuilder = userPrincipalBuilder;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("The login name is blank!");
        }
        
        // Check if username is an email address
        if (username.contains("@")) {
            return userRepository.findByEmail(username)
                .map(userPrincipalBuilder::buildUserPrincipal)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));
        }
        
        // If not an email, try mobile number
        return userRepository.findByMobile(username)
            .map(userPrincipalBuilder::buildUserPrincipal)
            .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));
    }
}
