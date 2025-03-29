package com.ikeyit.common.data.spring.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A repository finder implementation for PostgreSQL that locates domain event tables
 * across different database schemas. This class helps in identifying and creating
 * repositories for domain event tables in a PostgreSQL database.
 */
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

    /**
     * Creates a new finder instance with the specified data source.
     * 
     * @param dataSource The PostgreSQL data source to use for querying schemas
     */
    public PostgreSQLDomainEventRepositoryFinder(DataSource dataSource) {
        this(new NamedParameterJdbcTemplate(dataSource));
    }

    /**
     * Creates a new finder instance with the specified JDBC template.
     * 
     * @param jdbcTemplate The JDBC template to use for database operations
     */
    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        this(jdbcTemplate, null);
    }

    /**
     * Creates a new finder instance with the specified JDBC template and schema exclusions.
     * 
     * @param jdbcTemplate The JDBC template to use for database operations
     * @param schemaExcludes Set of schema names to exclude from the search
     */
    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate, Set<String> schemaExcludes) {
        this(jdbcTemplate, schemaExcludes, "domain_event");
    }
    /**
     * Creates a new finder instance with custom configuration.
     * 
     * @param jdbcTemplate The JDBC template to use for database operations
     * @param schemaExcludes Set of schema names to exclude from the search
     * @param tableName The name of the domain event table to search for
     */
    public PostgreSQLDomainEventRepositoryFinder(NamedParameterJdbcTemplate jdbcTemplate, Set<String> schemaExcludes, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.schemaExcludes.add("pg_catalog");
        this.schemaExcludes.add("information_schema");
        if (schemaExcludes != null)
            this.schemaExcludes.addAll(schemaExcludes);
    }

    /**
     * Finds all domain event repositories across available PostgreSQL schemas.
     * Excludes system schemas and any additional schemas specified in the constructor.
     * 
     * @return A list of domain event repositories, one for each schema containing the domain event table
     */
    public List<DomainEventRepository> findAll() {
        List<String> schemas = jdbcTemplate.queryForList(SQL_FIND,
            Map.of("tableName", tableName, "schemaExcludes", schemaExcludes),
            String.class);
        log.info("Found domain event tables in these schemas: {}", schemas);
        return schemas.stream()
            .map(this::buildDomainEventRepository)
            .toList();
    }

    /**
     * Creates a domain event repository for the specified schema.
     * 
     * @param schema The database schema name
     * @return A new JdbcDomainEventRepository instance configured for the schema
     */
    protected DomainEventRepository buildDomainEventRepository(String schema) {
        return new JdbcDomainEventRepository(jdbcTemplate, "PostgreSQL", schema + "." + tableName);
    }

}
