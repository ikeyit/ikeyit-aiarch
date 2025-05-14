package com.ikeyit.gateway.auth;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class GatewayAuthModule extends SimpleModule {
    public GatewayAuthModule() {
        super(GatewayAuthModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(AuthorizedClientDO.class, AuthorizedClientDOMixin.class);
    }
}
