package com.ikeyit.common.data.spring.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ikeyit.common.data.JsonField;
import com.ikeyit.common.data.JsonUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JsonToObjectConverter implements ConditionalGenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(JsonField.class) != null;
    }

    @Override
    @Nullable
    @SuppressWarnings("all")
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String string = source.toString();
        ObjectMapper mapper = JsonUtils.mapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        Class<?> type = targetType.getType();
        JavaType javaType = null;
        if (targetType.isMap()) {
            javaType = typeFactory.constructMapType((Class<? extends Map>) type, targetType.getMapKeyTypeDescriptor().getType(),
                targetType.getMapValueTypeDescriptor().getType());
        } else if (targetType.isCollection()) {
            javaType = typeFactory.constructCollectionType((Class<? extends Collection>) type, targetType.getElementTypeDescriptor().getType());
        } else if (targetType.isArray()) {
            javaType = typeFactory.constructArrayType(targetType.getElementTypeDescriptor().getType());
        } else {
            javaType = typeFactory.constructType(type);
        }
        try {
            return mapper.readValue(string, javaType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Fail to deserialize json!", e);
        }
    }

}
