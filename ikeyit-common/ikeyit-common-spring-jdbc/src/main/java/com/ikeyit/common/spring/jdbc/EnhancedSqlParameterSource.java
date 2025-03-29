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
public class EnhancedSqlParameterSource extends SimplePropertySqlParameterSource {

    private static final Map<Class<?>, Set<String>> jsonFieldCache = new ConcurrentReferenceHashMap<>();

    private final Set<String> jsonFields;

    private EnhancedMapSqlParameterSource mapSqlParameterSource;

    private final ObjectMapper objectMapper;

    private final Map<String, EnhancedSqlParameterSource> embeddedMap = new HashMap<>();


    public EnhancedSqlParameterSource(Object object) {
        this(object, null);
    }

    public EnhancedSqlParameterSource(Object object, ObjectMapper objectMapper) {
        super(object);
        this.jsonFields = jsonFieldCache.computeIfAbsent(object.getClass(), ReflectionSupport::getAnnotatedJsonFields);
        this.objectMapper = objectMapper == null ? JsonUtils.mapper() : objectMapper;
    }


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

    private Object retrieveValue(String paramName) {
        Object value = retrieveValueRaw(paramName);
        if (value instanceof Instant instant) {
            return Timestamp.from(instant);
        } else {
            return value;
        }
    }

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

    public EnhancedSqlParameterSource addValue(String paramName, @Nullable Object value) {
        mapSqlParameterSource().addValue(paramName, value);
        return this;
    }

    private MapSqlParameterSource mapSqlParameterSource() {
        if (mapSqlParameterSource == null) {
            mapSqlParameterSource = new EnhancedMapSqlParameterSource();
        }

        return mapSqlParameterSource;
    }
}
