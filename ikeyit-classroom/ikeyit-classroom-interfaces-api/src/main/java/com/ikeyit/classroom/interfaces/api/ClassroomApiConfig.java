package com.ikeyit.classroom.interfaces.api;

import com.ikeyit.classroom.infrastructure.ClassroomInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({ClassroomInfraConfig.class})
@ComponentScan(basePackageClasses = {ClassroomApiConfig.class})
public class ClassroomApiConfig {

}