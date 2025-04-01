package com.ikeyit.account.domain.repository;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Optional;

/**
 * User repository
 */
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByMobile(String mobile);
} 