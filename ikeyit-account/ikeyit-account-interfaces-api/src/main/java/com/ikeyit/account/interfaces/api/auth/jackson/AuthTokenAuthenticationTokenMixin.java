package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ikeyit.account.interfaces.api.auth.authsession.AuthSession;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AuthTokenAuthenticationTokenMixin {

    @JsonCreator
    AuthTokenAuthenticationTokenMixin(@JsonProperty("authSession") AuthSession authSession) {
    }
}