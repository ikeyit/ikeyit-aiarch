package com.ikeyit.account.infrastructure;


import com.ikeyit.account.application.AccountApplicationConfig;
import com.ikeyit.common.data.spring.domain.EnablePersistDomainEvent;
import com.ikeyit.common.data.spring.domain.EnablePublishDomainEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = AccountInfraConfig.class)
@Import({AccountApplicationConfig.class})
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class AccountInfraConfig {
}
