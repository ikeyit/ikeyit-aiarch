package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = UserPrincipalDeserializer.class)
@JsonSerialize(using = UserPrincipalSerializer.class)
abstract class UserPrincipalMixin {

}
