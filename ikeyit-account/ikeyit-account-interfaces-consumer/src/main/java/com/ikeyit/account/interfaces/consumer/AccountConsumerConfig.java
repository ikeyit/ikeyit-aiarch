package com.ikeyit.account.interfaces.consumer;


import com.ikeyit.account.infrastructure.AccountInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({AccountInfraConfig.class})
@ComponentScan(basePackageClasses = {AccountConsumerConfig.class})
public class AccountConsumerConfig {

}
