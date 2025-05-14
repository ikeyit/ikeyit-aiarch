package com.ikeyit.access.domain.repository;

import com.ikeyit.access.domain.model.Org;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Optional;

public interface OrgRepository extends CrudRepository<Org, Long> {
    Optional<Org> find();
}
