package com.ikeyit.foo.infrastructure;


import com.ikeyit.common.data.spring.domain.EnablePersistDomainEvent;
import com.ikeyit.common.data.spring.domain.EnablePublishDomainEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * === AI-NOTE ===
 * - Make sure to create this configuration to include all dependencies.
 * - Make sure add @EnablePublishDomainEvent an @EnablePersistDomainEvent
 * === AI-NOTE-END ===
 * </pre>
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = FooInfraConfig.class,
    basePackages = {"com.ikeyit.foo.application", "com.ikeyit.foo.domain"})
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class FooInfraConfig {

}
