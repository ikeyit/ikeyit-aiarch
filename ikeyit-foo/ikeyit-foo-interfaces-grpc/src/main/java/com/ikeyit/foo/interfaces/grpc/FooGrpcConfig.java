package com.ikeyit.foo.interfaces.grpc;


import com.ikeyit.foo.infrastructure.FooInfraConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({FooInfraConfig.class})
@ComponentScan(basePackageClasses = {FooGrpcConfig.class})
public class FooGrpcConfig {

}
