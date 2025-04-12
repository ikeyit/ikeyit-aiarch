package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = OidcUserPrincipalDeserializer.class)
@JsonSerialize(using = OidcUserPrincipalSerializer.class)
abstract class OidcUserPrincipalMixin {

}
