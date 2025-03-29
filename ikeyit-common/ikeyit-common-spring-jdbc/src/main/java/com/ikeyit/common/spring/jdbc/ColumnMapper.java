package com.ikeyit.common.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ColumnMapper<T> {
    void accept(T t, ResultSet rs, int rowNumber) throws SQLException;
}
