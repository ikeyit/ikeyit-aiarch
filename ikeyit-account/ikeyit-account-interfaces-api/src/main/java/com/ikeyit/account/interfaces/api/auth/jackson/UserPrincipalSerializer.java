package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;

public class UserPrincipalSerializer extends JsonSerializer<UserPrincipal> {

    @Override
    public void serializeWithType(UserPrincipal value, JsonGenerator jsonGenerator, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        WritableTypeId typeIdDef = typeSer. writeTypePrefix(jsonGenerator, typeSer.typeId(value, JsonToken.START_OBJECT));
        serialize(value, jsonGenerator);
        typeSer.writeTypeSuffix(jsonGenerator, typeIdDef);
    }

    @Override
    public void serialize(UserPrincipal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        serialize(value, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void serialize(UserPrincipal value, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField("id", value.getId());
        jsonGenerator.writeStringField("username", value.getUsername());
        jsonGenerator.writeStringField("displayName", value.getDisplayName());
        jsonGenerator.writeStringField("avatar", value.getAvatar());
        jsonGenerator.writeStringField("locale", value.getLocale());
        if (value.getEmail() != null) {
            jsonGenerator.writeStringField("email", value.getEmail());
        }
        if (value.getPhone() != null) {
            jsonGenerator.writeStringField("phone", value.getPhone());
        }
        jsonGenerator.writeArrayFieldStart("authorities");
        for (GrantedAuthority grantedAuthority : value.getAuthorities()) {
            jsonGenerator.writeString(grantedAuthority.getAuthority());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeBooleanField("enabled", value.isEnabled());
        jsonGenerator.writeBooleanField("accountNonExpired", value.isAccountNonExpired());
        jsonGenerator.writeBooleanField("credentialsNonExpired", value.isCredentialsNonExpired());
        jsonGenerator.writeBooleanField("accountNonLocked", value.isAccountNonLocked());

    }
}
