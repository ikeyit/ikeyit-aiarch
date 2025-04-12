package com.ikeyit.account.domain.repository;


import com.ikeyit.account.domain.model.UserConnection;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserConnectionRepository extends CrudRepository<UserConnection, Long> {

	List<UserConnection> findByLocalUserId(Long userId);

	List<UserConnection> findByLocalUserIdAndProvider(Long userId, String provider);

	Optional<UserConnection> findByProviderSub(String provider, String sub);

}
