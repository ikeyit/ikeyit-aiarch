package com.ikeyit.security.codeauth.core;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for managing user details based on verification codes.
 * This interface defines methods for loading user details based on a verification code target.
 */
public interface CodeAuthUserService {
    UserDetails loadUserByTarget(String target);
}
