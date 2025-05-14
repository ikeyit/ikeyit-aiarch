package com.ikeyit.access.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = {AccessApplicationConfig.class})
public class AccessApplicationConfig {
}
