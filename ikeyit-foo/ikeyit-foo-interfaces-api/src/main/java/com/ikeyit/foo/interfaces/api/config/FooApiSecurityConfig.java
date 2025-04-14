package com.ikeyit.foo.interfaces.api.config;


import com.ikeyit.security.resource.ResourceSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <pre>
 * === AI-NOTE ===
 * Use spring security and ResourceSecurityConfigurer to secure the project as a resource server.
 * Define only security-related properties here.
 * === AI-NOTE-END ===
 * </pre>
 */
@Configuration(proxyBeanMethods = false)
public class FooApiSecurityConfig {
    @Bean
    public SecurityFilterChain fooApiSecurityFilterChain(HttpSecurity http,
                                                         ResourceSecurityConfigurer resourceSecurityConfigurer) throws Exception {
        resourceSecurityConfigurer.configure(http)
            .securityMatcher("/api/foo/v1/**")
            .authorizeHttpRequests(c -> c
                .anyRequest()
                .permitAll());
        return http.build();
    }
}