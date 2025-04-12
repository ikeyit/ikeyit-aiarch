package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationCodeAuthenticationTokenMixin {

    @JsonCreator
    VerificationCodeAuthenticationTokenMixin(
                                       @JsonProperty("principal") Object principal,
                                       @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    }
}
