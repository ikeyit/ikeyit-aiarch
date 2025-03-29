package com.ikeyit.common.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private final static ObjectMapper mapper = buildObjectMapper();

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(supportModule());
        return objectMapper;
    }

    public static SimpleModule supportModule() {
        SimpleModule extraModule = new SimpleModule();
        extraModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        extraModule.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        extraModule.addSerializer(EnumWithInt.class, new EnumWithIntSerializer());
        extraModule.setDeserializerModifier(new BeanDeserializerModifier(){
            @Override
            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                Class<?> rawClass = type.getRawClass();
                if (EnumWithInt.class.isAssignableFrom(rawClass)) {
                    return new EnumWithIntDeserializer(rawClass);
                }
                return super.modifyEnumDeserializer(config, type, beanDesc, deserializer);
            }
        });
        return extraModule;
    }

    @SuppressWarnings("all")
    private static class EnumWithIntSerializer extends JsonSerializer<EnumWithInt> {
        @Override
        public void serialize(EnumWithInt obj, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeObject(obj.value());
        }
    }

    @SuppressWarnings("all")
    private static class EnumWithIntDeserializer extends JsonDeserializer<EnumWithInt> {
        private Class<?> javaType;
        public EnumWithIntDeserializer(Class<?> javaType) {
            this.javaType = javaType;
        }

        @Override
        public EnumWithInt deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            int val = jsonParser.getIntValue();

            for (Object enumObj : javaType.getEnumConstants()) {
                EnumWithInt enumWithInt = (EnumWithInt) enumObj;
                if (val == enumWithInt.value()) {
                    return enumWithInt;
                }
            }
            throw new IOException(val + " can't be converted to " + javaType);
        }
    }

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

    private static class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            if (value == null)
                return null;
            return new BigDecimal(value);  // BigDecimal constructor handles scale
        }
    }

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json serialization error", e);
        }
    }

    public static byte[] writeValueAsBytes(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json serialization error", e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json deserialization error", e);
        }
    }

    public static <T> List<T> readValueAsList(String content, Class<T> valueType) {
        try {
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, valueType);
            return mapper.readValue(content, javaType);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json deserialization error", e);
        }
    }

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
