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

/**
 * A Spring converter that handles conversion of JSON-formatted objects to their corresponding Java types.
 * This converter supports conversion to various types including collections, maps, arrays, and custom objects
 * that are annotated with @JsonField. It uses Jackson's ObjectMapper for JSON deserialization.
 */
public class JsonToObjectConverter implements ConditionalGenericConverter {

    /**
     * Specifies the source and target types this converter supports.
     * This converter can handle conversion from any Object to any other Object type,
     * with actual conversion determined by the matches method.
     *
     * @return A set containing the supported convertible type pair
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    /**
     * Determines if this converter can handle the conversion between the source and target types.
     * Only matches if the target type is annotated with @JsonField.
     *
     * @param sourceType The type descriptor of the source type
     * @param targetType The type descriptor of the target type
     * @return true if conversion is supported, false otherwise
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(JsonField.class) != null;
    }

    /**
     * Converts a source object to the target type by treating it as JSON.
     * Supports conversion to collections, maps, arrays, and custom objects.
     *
     * @param source The source object to convert
     * @param sourceType The type descriptor of the source type
     * @param targetType The type descriptor of the target type
     * @return The converted object, or null if source is null
     * @throws IllegalStateException if JSON deserialization fails
     */
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
