package com.ikeyit.foo.domain.repository;

import com.ikeyit.common.data.domain.CrudRepository;
import com.ikeyit.foo.domain.model.Foo;

import java.util.List;

/**
 * Foo repository
 * NOTE:
 * If it's a CRUD repository, it should extend CrudRepository
 */
public interface FooRepository extends CrudRepository<Foo, Long> {
    List<Foo> findAll();
}