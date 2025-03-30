package com.ikeyit.foo.interfaces.api;


import com.ikeyit.common.data.spring.domain.EnablePersistDomainEvent;
import com.ikeyit.common.data.spring.domain.EnablePublishDomainEvent;
import com.ikeyit.foo.infrastructure.FooInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({FooInfraConfig.class})
@ComponentScan(basePackageClasses = {FooApiConfig.class})
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class FooApiConfig {

}
