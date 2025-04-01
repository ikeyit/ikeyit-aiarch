package com.ikeyit.security.codeauth.core;

import org.springframework.security.core.userdetails.UserDetails;

public interface CodeAuthUserService {
    UserDetails loadUserByTarget(String target);
}
