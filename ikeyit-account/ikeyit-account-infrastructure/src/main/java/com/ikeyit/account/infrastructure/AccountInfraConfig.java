package com.ikeyit.account.infrastructure;


import com.ikeyit.account.application.AccountApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = AccountInfraConfig.class)
@Import({AccountApplicationConfig.class})
public class AccountInfraConfig {
}
