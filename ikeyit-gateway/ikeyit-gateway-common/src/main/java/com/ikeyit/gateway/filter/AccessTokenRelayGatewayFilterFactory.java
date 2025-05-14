package com.ikeyit.gateway.filter;

import com.ikeyit.gateway.auth.AuthorizedClientDO;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;

/** 
 * 
 */
public class AccessTokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<AccessTokenRelayGatewayFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange
            .getSession()
            .mapNotNull(session -> {
                AuthorizedClientDO authorizedClientDO = session.getAttribute("gatewayAuthorizedClient");
                if (authorizedClientDO == null) {
                    return null;
                }
                return exchange.mutate().request(r -> {
                    r.headers(headers -> headers
                        .add(HttpHeaders.AUTHORIZATION,
                        "Bearer " + authorizedClientDO.getAccessToken().getTokenValue()));
                }).build();
            })
            .defaultIfEmpty(exchange)
            .flatMap(chain::filter);
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    public static class Config {
        // Configuration properties, if any
    }
}
