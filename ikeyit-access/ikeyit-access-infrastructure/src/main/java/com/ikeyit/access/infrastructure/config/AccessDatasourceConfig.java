package com.ikeyit.access.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikeyit.common.data.JsonUtils;
import com.ikeyit.common.data.spring.domain.DomainEventRepository;
import com.ikeyit.common.data.spring.domain.EnablePersistDomainEvent;
import com.ikeyit.common.data.spring.domain.EnablePublishDomainEvent;
import com.ikeyit.common.data.spring.domain.JdbcDomainEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class AccessDatasourceConfig {
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return JsonUtils.mapper();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.access")
    public DataSourceProperties accessDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("accessDataSource")
    @ConditionalOnMissingBean(name = "accessDataSource")
    @ConfigurationProperties("spring.datasource.access.cp")
    public DataSource accessDataSource(@Qualifier("accessDataSourceProperties")
                                      DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public NamedParameterJdbcTemplate accessJdbcTemplate(@Qualifier("accessDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager accessTransactionManager(@Qualifier("accessDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    public TransactionTemplate accessTransactionTemplate(@Qualifier("accessTransactionManager") PlatformTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return transactionTemplate;
    }

    @Bean
    public DomainEventRepository accessDomainEventRepository(@Qualifier("accessJdbcTemplate") NamedParameterJdbcTemplate accessJdbcTemplate) {
        return new JdbcDomainEventRepository(accessJdbcTemplate);
    }


    @Bean
    @ConfigurationProperties("spring.datasource.access-global")
    public DataSourceProperties accessGlobalDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("spring.datasource.access-global.cp")
    public DataSource accessGlobalDataSource(@Qualifier("accessGlobalDataSourceProperties")
                                       DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public NamedParameterJdbcTemplate accessGlobalJdbcTemplate(@Qualifier("accessGlobalDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager accessGlobalTransactionManager(@Qualifier("accessGlobalDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
