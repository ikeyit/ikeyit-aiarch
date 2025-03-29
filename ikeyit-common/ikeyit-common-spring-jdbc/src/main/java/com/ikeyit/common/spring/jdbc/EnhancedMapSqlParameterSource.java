package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.EnumWithInt;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * An enhanced implementation of MapSqlParameterSource that provides special handling for EnumWithInt values.
 * This class extends Spring's MapSqlParameterSource to automatically convert enum values that implement
 * EnumWithInt interface to their corresponding integer values when used in SQL queries.
 */
public class EnhancedMapSqlParameterSource extends MapSqlParameterSource {

    /**
     * Creates a new empty EnhancedMapSqlParameterSource.
     */
    public EnhancedMapSqlParameterSource() {
        super();
    }

    /**
     * Creates a new EnhancedMapSqlParameterSource with a single parameter.
     *
     * @param paramName the name of the parameter
     * @param value the value of the parameter
     */
    public EnhancedMapSqlParameterSource(String paramName, Object value) {
        super(paramName, value);
    }

    /**
     * Creates a new EnhancedMapSqlParameterSource with a map of parameters.
     *
     * @param values a map holding parameter names and values
     */
    public EnhancedMapSqlParameterSource(Map<String, ?> values) {
        super(values);
    }

    /**
     * Gets the value of a parameter, converting EnumWithInt values to their integer representation.
     *
     * @param paramName the name of the parameter to get the value for
     * @return the value of the parameter, with EnumWithInt values converted to integers
     */
    @Override
    public Object getValue(@Nonnull String paramName) {
        Object value = super.getValue(paramName);
        if (value instanceof EnumWithInt && value.getClass().isEnum()) {
            return ((EnumWithInt) value).value();
        }
        return value;
    }
}
