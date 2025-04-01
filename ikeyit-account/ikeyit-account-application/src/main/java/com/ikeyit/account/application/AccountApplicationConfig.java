package com.ikeyit.account.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = AccountApplicationConfig.class)
public class AccountApplicationConfig {
}
