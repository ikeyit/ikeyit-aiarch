package com.ikeyit.access.interfaces.job;

import com.ikeyit.access.infrastructure.AccessInfraConfig;
import com.ikeyit.common.data.spring.domain.DomainTransactionalEventListenerFactory;
import com.ikeyit.common.data.spring.domain.MultiDomainEventManager;
import com.ikeyit.common.data.spring.domain.PostgreSQLDomainEventRepositoryFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import({AccessInfraConfig.class})
@ComponentScan(basePackageClasses = {AccessJobConfig.class})
public class AccessJobConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.access-event-retry")
    public DataSourceProperties accessEventRetryDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("spring.datasource.access-event-retry.cp")
    public DataSource accessEventRetryDataSource(@Qualifier("accessEventRetryDataSourceProperties")
                                                DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public MultiDomainEventManager accessMultiDomainEventManager(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                                                           @Qualifier("accessEventRetryDataSource") DataSource dataSource) {
        return new MultiDomainEventManager(
            domainTransactionalEventListenerFactory,
            new PostgreSQLDomainEventRepositoryFinder(dataSource),
            new DataSourceTransactionManager(dataSource),
            List.of());
    }
}
