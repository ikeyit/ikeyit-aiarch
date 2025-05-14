package com.ikeyit.access.interfaces.consumer;

import com.ikeyit.access.infrastructure.AccessInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = {AccessConsumerConfig.class})
@Import({AccessInfraConfig.class})
public class AccessConsumerConfig {
}
