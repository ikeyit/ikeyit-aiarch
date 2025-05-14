package com.ikeyit.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import java.util.Locale;

public class LocaleRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<LocaleRelayGatewayFilterFactory.Config> {

    public LocaleRelayGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Locale locale = exchange.getLocaleContext().getLocale();
            if (locale != null) {
                exchange = exchange.mutate()
                        .request(r -> r.header(config.getLocaleHeader(), locale.toString()))
                        .build();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {

        private String localeHeader = "Accept-Language";

        public String getLocaleHeader() {
            return localeHeader;
        }

        public void setLocaleHeader(String localeHeader) {
            this.localeHeader = localeHeader;
        }
    }
}