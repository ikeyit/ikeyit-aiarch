package com.ikeyit.foo.infrastructure.config;

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
public class FooDatasourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.foo")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.foo.cp")
    public DataSource fooDataSource(@Qualifier("fooDataSourceProperties") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public NamedParameterJdbcTemplate fooJdbcTemplate(@Qualifier("fooDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager fooTransactionManager(@Qualifier("fooDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
