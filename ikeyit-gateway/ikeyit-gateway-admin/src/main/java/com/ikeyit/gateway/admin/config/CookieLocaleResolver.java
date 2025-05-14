package com.ikeyit.gateway.admin.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpCookie;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

public class CookieLocaleResolver implements LocaleContextResolver {
    private static final Logger log = LoggerFactory.getLogger(CookieLocaleResolver.class);

    private final String cookieName = "locale";

    private final LocaleContextResolver defaultResolver = new AcceptHeaderLocaleContextResolver();

    public CookieLocaleResolver() {
    }

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        SimpleLocaleContext localeContext = parseLocale(exchange.getRequest().getCookies().getFirst(cookieName));
        if (localeContext != null) {
            return localeContext;
        }
        return defaultResolver.resolveLocaleContext(exchange);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        throw new UnsupportedOperationException();
    }

    private SimpleLocaleContext parseLocale(HttpCookie cookie) {
        if (cookie == null) {
            return null;
        }
        String cookieValue = cookie.getValue();
        if (!StringUtils.hasLength(cookieValue)) {
            return null;
        }
        try {
            return new SimpleLocaleContext(Locale.forLanguageTag(cookieValue));
        } catch (Exception e) {
            log.error("parse locale error", e);
            return null;
        }
    }
}