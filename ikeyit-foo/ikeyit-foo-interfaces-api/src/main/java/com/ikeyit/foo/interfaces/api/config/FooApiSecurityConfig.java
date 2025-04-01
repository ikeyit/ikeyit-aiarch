package com.ikeyit.foo.interfaces.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration(proxyBeanMethods = false)
public class FooApiSecurityConfig {
    @Bean
    public SecurityFilterChain fooApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(c -> c
                .anyRequest()
                .permitAll())
            .anonymous(AbstractHttpConfigurer::disable);
        return http.build();
    }


}