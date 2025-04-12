package com.ikeyit.account.interfaces.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.account.interfaces.api.auth.PrincipalIdIndexResolver;
import com.ikeyit.account.interfaces.api.auth.jackson.AuthJacksonModule;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.IndexResolver;
import org.springframework.session.Session;

@Configuration(proxyBeanMethods = false)
public class AccountSessionConfig implements BeanClassLoaderAware {

    private ClassLoader loader;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
//        mapper. registerModule(new CoreJackson2Module());
        // specify mixins for our own classes, otherwise they can't be serialized or deserialized into redis
        objectMapper.registerModules(new AuthJacksonModule());
        return objectMapper;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

    @Bean
    public IndexResolver<Session> indexResolver() {
        return new PrincipalIdIndexResolver<>();
    }
}