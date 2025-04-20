package com.ikeyit.classroom.infrastructure;

import com.ikeyit.common.data.spring.domain.EnablePersistDomainEvent;
import com.ikeyit.common.data.spring.domain.EnablePublishDomainEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = ClassroomInfraConfig.class,
    basePackages = {"com.ikeyit.classroom.application", "com.ikeyit.classroom.domain"})
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class ClassroomInfraConfig {

}