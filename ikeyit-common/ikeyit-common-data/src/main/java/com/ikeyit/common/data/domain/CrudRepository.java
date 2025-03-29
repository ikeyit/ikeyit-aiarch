package com.ikeyit.common.data.domain;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> findById(ID id);

    void create(T entity);

    void update(T entity);

    void delete(T entity);
}
