package com.ikeyit.common.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface for mapping individual columns from a ResultSet to an entity object.
 * This interface is used within row mappers to handle custom column mapping logic.
 *
 * @param <T> the type of entity object being mapped to
 */
@FunctionalInterface
public interface ColumnMapper<T> {
    /**
     * Maps a column value from the ResultSet to the target entity object.
     *
     * @param t the target entity object to map to
     * @param rs the ResultSet containing the column data
     * @param rowNumber the current row number being processed
     * @throws SQLException if a database access error occurs
     */
    void accept(T t, ResultSet rs, int rowNumber) throws SQLException;
}
