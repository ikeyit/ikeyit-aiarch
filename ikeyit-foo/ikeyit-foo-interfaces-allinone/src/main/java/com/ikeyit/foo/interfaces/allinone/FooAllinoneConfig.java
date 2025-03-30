package com.ikeyit.foo.interfaces.allinone;

import com.ikeyit.foo.interfaces.consumer.FooConsumerConfig;
import com.ikeyit.foo.interfaces.api.FooApiConfig;
import com.ikeyit.foo.interfaces.grpc.FooGrpcConfig;
import com.ikeyit.foo.interfaces.job.FooJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
    FooApiConfig.class,
    FooGrpcConfig.class,
    FooConsumerConfig.class,
    FooJobConfig.class
})
public class FooAllinoneConfig {
}
