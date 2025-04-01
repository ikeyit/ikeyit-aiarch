package com.ikeyit.account.interfaces.job;


import com.ikeyit.account.infrastructure.AccountInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import({AccountInfraConfig.class})
@ComponentScan(basePackageClasses = {AccountJobConfig.class})
public class AccountJobConfig {
}
