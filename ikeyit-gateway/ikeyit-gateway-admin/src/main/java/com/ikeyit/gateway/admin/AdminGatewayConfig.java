package com.ikeyit.gateway.admin;

import com.ikeyit.gateway.filter.AccessTokenRelayGatewayFilterFactory;
import com.ikeyit.gateway.filter.LocaleRelayGatewayFilterFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = AdminGatewayConfig.class)
@Import({AccessTokenRelayGatewayFilterFactory.class, LocaleRelayGatewayFilterFactory.class})
public class AdminGatewayConfig {

}
