package com.ikeyit.foo.domain.repository;

import com.ikeyit.common.data.domain.CrudRepository;
import com.ikeyit.foo.domain.model.Foo;

import java.util.List;

/**
 * <pre>
 * === AI-NOTE ===
 * - A CRUD repository should extend CrudRepository
 * === AI-NOTE-END ===
 * </pre>
 * Foo repository
 */
public interface FooRepository extends CrudRepository<Foo, Long> {
    List<Foo> findAll();
}