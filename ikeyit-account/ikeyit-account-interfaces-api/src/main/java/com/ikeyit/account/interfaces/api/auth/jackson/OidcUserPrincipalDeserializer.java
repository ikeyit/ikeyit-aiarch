package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.account.infrastructure.security.oidc.OidcUserPrincipal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OidcUserPrincipalDeserializer extends JsonDeserializer<OidcUserPrincipal> {
    @Override
    public OidcUserPrincipal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<String> authorities = new HashSet<>();
        for (Iterator<JsonNode> it = jsonNode.path("authorities").elements(); it.hasNext(); ) {
            authorities.add(it.next().asText());
        }
        return new OidcUserPrincipal(
            jsonNode.path("id").asLong(),
            jsonNode.path("username").asText(),
            null,
            jsonNode.path("displayName").asText(),
            jsonNode.path("avatar").asText(),
            jsonNode.path("locale").asText(),
            jsonNode.path("email").asText(null),
            jsonNode.path("phone").asText(null),
            authorities,
            jsonNode.path("enabled").asBoolean(),
            jsonNode.path("accountNonExpired").asBoolean(),
            jsonNode.path("credentialsNonExpired").asBoolean(),
            jsonNode.path("accountNonLocked").asBoolean()
        );
    }
}
