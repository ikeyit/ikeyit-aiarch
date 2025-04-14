package com.ikeyit.foo.infrastructure.config;

import com.ikeyit.common.data.spring.domain.JdbcDomainEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * <pre>
 * === AI-NOTE ===
 * - Configure a dedicated data source for the project instead of using the global or default data source.
 * === AI-NOTE-END ===
 * </pre>
 * Configure datasource
 */
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

    // AI-NOTE: Configure this repository to persist domain events
    @Bean
    public JdbcDomainEventRepository fooDomainEventRepository(@Qualifier("fooJdbcTemplate") NamedParameterJdbcTemplate fooJdbcTemplate) {
        return new JdbcDomainEventRepository(fooJdbcTemplate);
    }
}
