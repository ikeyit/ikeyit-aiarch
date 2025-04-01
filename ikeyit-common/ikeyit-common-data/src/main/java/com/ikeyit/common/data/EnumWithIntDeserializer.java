package com.ikeyit.common.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Custom deserializer for EnumWithInt that creates enum instances from integer values.
 */
public class EnumWithIntDeserializer extends JsonDeserializer<EnumWithInt> {
    private final Class<?> javaType;

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