package com.ikeyit.classroom.infrastructure.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration(proxyBeanMethods = false)
public class ClassroomDatasourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.classroom")
    public DataSourceProperties classroomDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.classroom.cp")
    public DataSource classroomDataSource(@Qualifier("classroomDataSourceProperties") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public NamedParameterJdbcTemplate classroomJdbcTemplate(@Qualifier("classroomDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager classroomTransactionManager(@Qualifier("classroomDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}