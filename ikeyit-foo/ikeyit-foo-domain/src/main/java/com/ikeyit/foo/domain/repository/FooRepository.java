package com.ikeyit.foo.domain.repository;

import com.ikeyit.common.data.domain.CrudRepository;
import com.ikeyit.foo.domain.model.Foo;

import java.util.List;

public interface FooRepository extends CrudRepository<Foo, Long> {
    List<Foo> findAll();
}