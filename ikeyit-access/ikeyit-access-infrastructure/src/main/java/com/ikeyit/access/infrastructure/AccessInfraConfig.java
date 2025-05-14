package com.ikeyit.access.infrastructure;


import com.ikeyit.access.application.AccessApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = {AccessInfraConfig.class, AccessApplicationConfig.class})
public class AccessInfraConfig {
}
