package com.ikeyit.access.domain.repository;

import com.ikeyit.access.domain.model.Member;
import com.ikeyit.common.data.Page;
import com.ikeyit.common.data.PageParam;
import com.ikeyit.common.data.SortParam;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Optional<Member> findByUserId(Long userId);
    Page<Member> find(String name, PageParam pageParam, SortParam sortParam);

    @SuppressWarnings("all")
    Page<Member> findInRealm(String realmType, Long realmId, String name, PageParam pageParam, SortParam sortParam);
}
