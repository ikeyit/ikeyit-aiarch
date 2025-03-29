package com.ikeyit.common.data.spring.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PostgreSQLDomainEventRepositoryFinder implements DomainEventRepositoryFinder {
    private static final Logger log = LoggerFactory.getLogger(PostgreSQLDomainEventRepositoryFinder.class);
    private static final String SQL_FIND = """
        SELECT table_schema
        FROM information_schema.tables
        WHERE table_name = :tableName
          AND table_schema NOT IN (:schemaExcludes)
        GROUP BY table_schema;
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String tableName;

    private final Set<String> schemaExcludes = new HashSet<>();

    public PostgreSQLDomainEventRepositoryFinder(DataSource dataSource) {
        this(new NamedParameterJdbcTemplate(dataSource));
    }

    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        this(jdbcTemplate, null);
    }

    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate, Set<String> schemaExcludes) {
        this(jdbcTemplate, schemaExcludes, "domain_event");
    }
    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate, Set<String> schemaExcludes, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.schemaExcludes.add("pg_catalog");
        this.schemaExcludes.add("information_schema");
        if (schemaExcludes != null)
            this.schemaExcludes.addAll(schemaExcludes);
    }

    public List<DomainEventRepository> findAll() {
        List<String> schemas = jdbcTemplate.queryForList(SQL_FIND,
            Map.of("tableName", tableName, "schemaExcludes", schemaExcludes),
            String.class);
        log.info("Found domain event tables in these schemas: {}", schemas);
        return schemas.stream()
            .map(this::buildDomainEventRepository)
            .toList();
    }

    protected DomainEventRepository buildDomainEventRepository(String schema) {
        return new JdbcDomainEventRepository(jdbcTemplate, "PostgreSQL", schema + "." + tableName);
    }

}
