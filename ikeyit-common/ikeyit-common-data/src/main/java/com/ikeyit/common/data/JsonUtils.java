package com.ikeyit.common.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON serialization and deserialization using Jackson.
 * Provides custom handling for BigDecimal and EnumWithInt types, and supports Java 8 date/time types.
 */
public class JsonUtils {

    private final static ObjectMapper mapper = buildObjectMapper();

    /**
     * Builds and configures the ObjectMapper instance with custom modules and settings.
     *
     * @return configured ObjectMapper instance
     */
    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(supportModule());
        return objectMapper;
    }

    /**
     * Creates a Jackson module with custom serializers and deserializers.
     *
     * @return SimpleModule with custom serializers and deserializers
     */
    public static SimpleModule supportModule() {
        SimpleModule extraModule = new SimpleModule();
        extraModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        extraModule.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
//        extraModule.addSerializer(EnumWithInt.class, new EnumWithIntSerializer());
//        extraModule.setDeserializerModifier(new BeanDeserializerModifier(){
//            @Override
//            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
//                Class<?> rawClass = type.getRawClass();
//                if (EnumWithInt.class.isAssignableFrom(rawClass)) {
//                    return new EnumWithIntDeserializer(rawClass);
//                }
//                return super.modifyEnumDeserializer(config, type, beanDesc, deserializer);
//            }
//        });
        return extraModule;
    }

    /**
     * Custom serializer for BigDecimal that preserves scale using toPlainString.
     */
    private static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(value.toPlainString());  // Use toPlainString to preserve scale
            } else {
                gen.writeNull();
            }
        }
    }

    /**
     * Custom deserializer for BigDecimal that handles scale properly.
     */
    private static class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            if (value == null)
                return null;
            return new BigDecimal(value);  // BigDecimal constructor handles scale
        }
    }

    /**
     * Returns the configured ObjectMapper instance.
     *
     * @return the ObjectMapper instance
     */
    public static ObjectMapper mapper() {
        return mapper;
    }

    /**
     * Converts an object to its JSON string representation.
     *
     * @param object the object to convert
     * @return JSON string representation of the object
     * @throws IllegalStateException if serialization fails
     */
    public static String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json serialization error", e);
        }
    }

    /**
     * Converts an object to its JSON byte array representation.
     *
     * @param object the object to convert
     * @return JSON byte array representation of the object
     * @throws IllegalStateException if serialization fails
     */
    public static byte[] writeValueAsBytes(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json serialization error", e);
        }
    }

    /**
     * Converts a JSON string to an object of the specified type.
     *
     * @param content the JSON string to convert
     * @param valueType the class of the target object
     * @param <T> the type of the target object
     * @return the deserialized object
     * @throws IllegalStateException if deserialization fails
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json deserialization error", e);
        }
    }

    /**
     * Converts a JSON string to a List of objects of the specified type.
     *
     * @param content the JSON string to convert
     * @param valueType the class of the list elements
     * @param <T> the type of the list elements
     * @return the deserialized list
     * @throws IllegalStateException if deserialization fails
     */
    public static <T> List<T> readValueAsList(String content, Class<T> valueType) {
        try {
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, valueType);
            return mapper.readValue(content, javaType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json deserialization error", e);
        }
    }

    /**
     * Converts a JSON string to a Map with String keys and Object values.
     *
     * @param content the JSON string to convert
     * @return the deserialized map
     * @throws IllegalStateException if deserialization fails
     */
    public static Map<String, Object> readValueAsMap(String content) {
        try {
            MapType javaType = mapper.getTypeFactory()
                .constructMapType(Map.class, String.class, Object.class);
            return mapper.readValue(content, javaType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json deserialization error", e);
        }
    }
}
