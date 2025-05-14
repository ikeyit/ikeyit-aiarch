package com.ikeyit.access.interfaces.admin.api;


import com.ikeyit.access.infrastructure.AccessInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({AccessInfraConfig.class})
@ComponentScan(basePackageClasses = {AccessAdminApiConfig.class})
public class AccessAdminApiConfig {

}
