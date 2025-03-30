package com.ikeyit.foo.infrastructure.repository;

import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.foo.domain.model.Foo;
import com.ikeyit.foo.domain.repository.FooRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
public class InMemoryFooRepository implements FooRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryFooRepository.class);

    private final List<Foo> records = new ArrayList<>();

    @Override
    public List<Foo> findAll() {
        return records;
    }

    @Override
    public Optional<Foo> findById(Long id) {
        return Optional.empty();
    }

    @Override
    @PublishDomainEvent
    public void create(Foo entity) {
        entity.assignId(System.nanoTime());
        logger.info("Persist new entity: {}", entity);
        records.add(entity);
    }

    @Override
    @PublishDomainEvent
    public void update(Foo entity) {
        records.replaceAll(record -> {
            if (Objects.equals(entity.getId(), record.getId())) {
                return entity;
            } else {
                return record;
            }
        });
    }

    @Override
    @PublishDomainEvent
    public void delete(Foo entity) {
        records.removeIf(record -> Objects.equals(entity.getId(), record.getId()));
    }
}
