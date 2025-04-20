package com.ikeyit.account.interfaces.api.auth.authsession;

import java.util.List;

public interface AuthSessionRepository {
    void save(AuthSession session);
    AuthSession findById(String id);
    List<AuthSession> findByUserId(Long userId);
    void deleteById(String id);
}
