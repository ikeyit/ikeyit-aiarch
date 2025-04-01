package com.ikeyit.common.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom serializer for EnumWithInt that writes the enum's integer value.
 */
class EnumWithIntSerializer extends JsonSerializer<EnumWithInt> {
    @Override
    public void serialize(EnumWithInt obj, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeObject(obj.value());
    }
}
