package com.ikeyit.account.interfaces.grpc;

import com.ikeyit.account.application.AccountApplicationConfig;
import com.ikeyit.account.infrastructure.AccountInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({AccountApplicationConfig.class, AccountInfraConfig.class})
@ComponentScan(basePackageClasses = AccountGrpcConfig.class)
public class AccountGrpcConfig {

}
