package com.ikeyit.common.grpc.server;

import com.ikeyit.common.grpc.server.component.GlobalGrpcExceptionAdvice;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = GlobalGrpcExceptionAdvice.class)
public class CommonGrpcServerAutoConfig {

}
