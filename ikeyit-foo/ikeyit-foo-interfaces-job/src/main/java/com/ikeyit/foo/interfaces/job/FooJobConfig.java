package com.ikeyit.foo.interfaces.job;


import com.ikeyit.foo.infrastructure.FooInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import({FooInfraConfig.class})
@ComponentScan(basePackageClasses = {FooJobConfig.class})
public class FooJobConfig {
}
