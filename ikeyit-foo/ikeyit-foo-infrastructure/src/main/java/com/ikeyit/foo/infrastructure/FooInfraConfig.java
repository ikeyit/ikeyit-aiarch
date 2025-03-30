package com.ikeyit.foo.infrastructure;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = FooInfraConfig.class,
    basePackages = {"com.ikeyit.foo.application", "com.ikeyit.foo.domain"})
public class FooInfraConfig {

}
