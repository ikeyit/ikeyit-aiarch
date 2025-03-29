package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.JsonUtils;
import com.ikeyit.common.data.domain.DomainEvent;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It's a jdbc version to store and read domain events.
 *
 */
/**
 * A JDBC implementation of the DomainEventRepository interface for storing and managing domain events.
 * This implementation supports both PostgreSQL and MySQL databases, providing efficient storage and
 * retrieval of domain events with proper transaction handling and concurrent processing support.
 */
public class JdbcDomainEventRepository implements DomainEventRepository {

    private static final String SQL_SAVE = """
        INSERT INTO %s (event_id, listener_id, event_type, payload, created_at, headers)
        VALUES
        (:eventId, :listenerId, :eventType, :payload, :createdAt, :headers)
        """;

    private static final String SQL_DELETE = """
        DELETE FROM %s where event_id = :eventId and listener_id = :listenerId
        """;

    // Use skip lock to concurrently resend the events.
    private static final String SQL_FIND = """
        SELECT event_id, listener_id, event_type, payload, created_at, headers FROM %s
        WHERE created_at < :time
        ORDER BY created_at
        LIMIT :maxCount
        FOR UPDATE SKIP LOCKED
        """;

    private static final String SQL_SAVE_MYSQL = """
        INSERT INTO %s (event_id, listener_id, event_type, payload, created_at, headers)
        VALUES
        (UUID_TO_BIN(:eventId), :listenerId, :eventType, :payload, :createdAt, :headers)
        """;

    private static final String SQL_DELETE_MYSQL = """
        DELETE FROM %s where event_id = UUID_TO_BIN(:eventId) and listener_id = :listenerId
        """;

    private static final String SQL_FIND_MYSQL = """
        SELECT BIN_TO_UUID(event_id), listener_id, event_type, payload, created_at, headers FROM %s
        WHERE created_at < :time
        ORDER BY created_at
        LIMIT :maxCount
        FOR UPDATE SKIP LOCKED
        """;
    public static final String POSTGRESQL = "PostgreSQL";
    public static final String MYSQL = "MySQL";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String sqlSave;
    private final String sqlDelete;
    private final String sqlFind;
    private final RowMapper<DomainEventPublication> rowMapper = (rs, rowNum) -> {
//        UUID eventId = rs.getObject("event_id", UUID.class);
        String listenerId = rs.getString("listener_id");
        String eventType = rs.getString("event_type");
        Instant createdAt = rs.getTimestamp("created_at").toInstant();
        DomainEvent domainEvent = deserializeDomainEvent(rs.getString("payload"), eventType);
        Map<String, Object> headers = deserializeHeaders(rs.getString("headers"));
        return new DomainEventPublication(
            domainEvent,
            listenerId,
            createdAt,
            headers
        );
    };

    /**
     * Constructs a new JdbcDomainEventRepository with default PostgreSQL dialect and table name.
     *
     * @param jdbcTemplate The JDBC template for database operations
     */
    public JdbcDomainEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this(jdbcTemplate, POSTGRESQL, "domain_event");
    }

    /**
     * Constructs a new JdbcDomainEventRepository with specified dialect and table name.
     *
     * @param jdbcTemplate The JDBC template for database operations
     * @param dialect The database dialect (PostgreSQL or MySQL)
     * @param tableName The name of the table to store domain events
     * @throws IllegalArgumentException if the specified dialect is not supported
     */
    public JdbcDomainEventRepository(NamedParameterJdbcTemplate jdbcTemplate, String dialect, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        switch (dialect) {
            case POSTGRESQL -> {
                this.sqlSave = String.format(SQL_SAVE, tableName);
                this.sqlDelete = String.format(SQL_DELETE, tableName);
                this.sqlFind = String.format(SQL_FIND, tableName);
            }
            case MYSQL -> {
                this.sqlSave = String.format(SQL_SAVE_MYSQL, tableName);
                this.sqlDelete = String.format(SQL_DELETE_MYSQL, tableName);
                this.sqlFind = String.format(SQL_FIND_MYSQL, tableName);
            }
            default -> throw new IllegalArgumentException("dialect " + dialect + " is not supported!");
        }

    }

    /**
     * Saves a domain event publication to the database.
     *
     * @param domainEventPublication The domain event publication to save
     */
    @Override
    public void save(DomainEventPublication domainEventPublication) {
        jdbcTemplate.update(sqlSave, new MapSqlParameterSource()
            .addValue("eventId", domainEventPublication.getEvent().getEventId())
            .addValue("listenerId", domainEventPublication.getListenerId())
            .addValue("eventType", domainEventPublication.getEvent().getClass().getName())
            .addValue("payload", serializeDomainEvent(domainEventPublication.getEvent()))
            .addValue("createdAt", Timestamp.from(domainEventPublication.getCreatedAt()))
            .addValue("headers", serializeHeaders(domainEventPublication.getHeaders()))
        );
    }

    /**
     * Deletes a domain event from the database.
     *
     * @param eventId The ID of the event to delete
     * @param listenerId The ID of the listener that processed the event
     */
    @Override
    public void delete(UUID eventId, String listenerId) {
        jdbcTemplate.update(sqlDelete, Map.of("eventId", eventId, "listenerId", listenerId));
    }

    /**
     * The method uses SKIP LOCK to query records. The caller can put the method in a transaction to avoid duplicate processing
     * in distributed scenario. It allows we to retry the failed events in multiple scheduled jobs.
     * @param time
     * @param maxCount
     * @return
     */
    /**
     * Finds domain events created before the specified time, using SKIP LOCK for concurrent processing.
     *
     * @param time The time threshold for selecting events
     * @param maxCount The maximum number of events to return
     * @return List of domain event publications
     */
    @Override
    public List<DomainEventPublication> findBefore(Instant time, int maxCount) {
        return jdbcTemplate.query(sqlFind, Map.of(
            "time", Timestamp.from(time),
            "maxCount", maxCount
        ), rowMapper);
    }

    /**
     * Serializes event headers to JSON format.
     *
     * @param headers The headers to serialize
     * @return JSON string representation of the headers
     */
    protected String serializeHeaders(Map<String, Object> headers) {
        return JsonUtils.writeValueAsString(headers);
    }

    /**
     * Deserializes JSON string to event headers.
     *
     * @param rawHeader The JSON string to deserialize
     * @return Map of header key-value pairs
     */
    protected Map<String, Object> deserializeHeaders(String rawHeader) {
        return JsonUtils.readValueAsMap(rawHeader);
    }

    /**
     * Serializes a domain event to JSON format.
     *
     * @param event The domain event to serialize
     * @return JSON string representation of the event
     */
    protected String serializeDomainEvent(DomainEvent event) {
        return JsonUtils.writeValueAsString(event);
    }

    /**
     * Deserializes JSON string to a domain event.
     *
     * @param payload The JSON string to deserialize
     * @param eventType The class name of the event type
     * @return The deserialized domain event
     * @throws IllegalStateException if the event type class is not found
     */
    protected DomainEvent deserializeDomainEvent(String payload, String eventType)  {
        try {
            return (DomainEvent) JsonUtils.readValue(payload, Class.forName(eventType));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(eventType + " is not found!");
        }
    }
}
