package com.ikeyit.common.spring.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.common.data.EnumWithInt;
import com.ikeyit.common.data.JsonUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SimplePropertySqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
/**
 * An enhanced implementation of SimplePropertySqlParameterSource that provides special handling for
 * JSON fields, embedded objects, and enum values. This class extends Spring's SimplePropertySqlParameterSource
 * to support automatic JSON serialization for annotated fields and nested parameter access.
 */
public class EnhancedSqlParameterSource extends SimplePropertySqlParameterSource {

    /** Cache for storing JSON field names by class to improve performance */
    private static final Map<Class<?>, Set<String>> jsonFieldCache = new ConcurrentReferenceHashMap<>();

    /** Set of field names that should be serialized as JSON */
    private final Set<String> jsonFields;

    /** Additional parameter source for handling map-based parameters */
    private EnhancedMapSqlParameterSource mapSqlParameterSource;

    /** ObjectMapper instance for JSON serialization */
    private final ObjectMapper objectMapper;

    /** Map for handling embedded objects and their parameter sources */
    private final Map<String, EnhancedSqlParameterSource> embeddedMap = new HashMap<>();

    /**
     * Creates a new EnhancedSqlParameterSource using the default ObjectMapper.
     *
     * @param object the source object for parameters
     */
    public EnhancedSqlParameterSource(Object object) {
        this(object, null);
    }

    /**
     * Creates a new EnhancedSqlParameterSource with a custom ObjectMapper.
     *
     * @param object the source object for parameters
     * @param objectMapper custom ObjectMapper for JSON serialization, or null to use default
     */
    public EnhancedSqlParameterSource(Object object, ObjectMapper objectMapper) {
        super(object);
        this.jsonFields = jsonFieldCache.computeIfAbsent(object.getClass(), ReflectionSupport::getAnnotatedJsonFields);
        this.objectMapper = objectMapper == null ? JsonUtils.mapper() : objectMapper;
    }


    /**
     * Gets the value of a parameter, handling nested properties and special value conversions.
     *
     * @param paramName the name of the parameter to get the value for
     * @return the parameter value, possibly converted or transformed
     * @throws IllegalArgumentException if parameter value cannot be retrieved or processed
     */
    @Override
    @Nullable
    public Object getValue(String paramName) throws IllegalArgumentException {
        int i = paramName.indexOf('.');
        if (i < 0) {
            return retrieveValue(paramName);
        } else {
            String firstSeg = paramName.substring(0, i);
            String remaining = paramName.substring(i + 1);
            if (!embeddedMap.containsKey(firstSeg)) {
                Object embeddable = retrieveValue(firstSeg);
                embeddedMap.put(firstSeg, embeddable == null ? null : new EnhancedSqlParameterSource(embeddable));
            }
            EnhancedSqlParameterSource ps = embeddedMap.get(firstSeg);
            return ps == null ? null : ps.getValue(remaining);
        }
    }

    /**
     * Retrieves and converts a parameter value, handling special cases like Instant to Timestamp conversion.
     *
     * @param paramName the name of the parameter
     * @return the converted parameter value
     */
    private Object retrieveValue(String paramName) {
        Object value = retrieveValueRaw(paramName);
        if (value instanceof Instant instant) {
            return Timestamp.from(instant);
        } else {
            return value;
        }
    }

    /**
     * Retrieves the raw parameter value, handling JSON serialization and enum conversion.
     *
     * @param paramName the name of the parameter
     * @return the raw parameter value
     */
    private Object retrieveValueRaw(String paramName) {
        if (mapSqlParameterSource != null && mapSqlParameterSource.hasValue(paramName)) {
            return mapSqlParameterSource.getValue(paramName);
        }
        Object value = super.getValue(paramName);
        if (jsonFields.contains(paramName)) {
            try {
                return value == null ? null : objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Can't serialize object to json string", e);
            }
        }
        if (value instanceof EnumWithInt && value.getClass().isEnum()) {
            return ((EnumWithInt) value).value();
        }
        return value;
    }

    /**
     * Adds a parameter value to this parameter source.
     *
     * @param paramName the name of the parameter
     * @param value the value of the parameter
     * @return this parameter source instance for method chaining
     */
    public EnhancedSqlParameterSource addValue(String paramName, @Nullable Object value) {
        mapSqlParameterSource().addValue(paramName, value);
        return this;
    }

    /**
     * Gets or creates the map-based parameter source for additional parameters.
     *
     * @return the map-based parameter source
     */
    private MapSqlParameterSource mapSqlParameterSource() {
        if (mapSqlParameterSource == null) {
            mapSqlParameterSource = new EnhancedMapSqlParameterSource();
        }

        return mapSqlParameterSource;
    }
}
