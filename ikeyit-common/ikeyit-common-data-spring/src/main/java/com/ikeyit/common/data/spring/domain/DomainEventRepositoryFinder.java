package com.ikeyit.common.data.spring.domain;

import java.util.List;

public interface DomainEventRepositoryFinder {
    List<DomainEventRepository> findAll();
}
