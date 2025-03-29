package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.EnumWithInt;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.annotation.Nonnull;
import java.util.Map;

public class EnhancedMapSqlParameterSource extends MapSqlParameterSource {

    public EnhancedMapSqlParameterSource() {
        super();
    }

    public EnhancedMapSqlParameterSource(String paramName, Object value) {
        super(paramName, value);
    }

    public EnhancedMapSqlParameterSource(Map<String, ?> values) {
        super(values);
    }

    @Override
    public Object getValue(@Nonnull String paramName) {
        Object value = super.getValue(paramName);
        if (value instanceof EnumWithInt && value.getClass().isEnum()) {
            return ((EnumWithInt) value).value();
        }
        return value;
    }
}
