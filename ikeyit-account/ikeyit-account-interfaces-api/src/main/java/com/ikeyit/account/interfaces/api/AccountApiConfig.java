package com.ikeyit.account.interfaces.api;

import com.ikeyit.account.application.AccountApplicationConfig;
import com.ikeyit.account.infrastructure.AccountInfraConfig;
import com.ikeyit.common.web.exception.EnableRestErrorController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({AccountApplicationConfig.class, AccountInfraConfig.class})
@ComponentScan(basePackageClasses = AccountApiConfig.class)
@EnableRestErrorController
public class AccountApiConfig{

}
