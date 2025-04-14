package com.ikeyit.foo.interfaces.allinone;

import com.ikeyit.foo.interfaces.consumer.FooConsumerConfig;
import com.ikeyit.foo.interfaces.api.FooApiConfig;
import com.ikeyit.foo.interfaces.grpc.FooGrpcConfig;
import com.ikeyit.foo.interfaces.job.FooJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <pre>
 * === AI-NOTE ===
 * - Make sure to create the configuration to include all configurations of interfaces
 * - Import the configuration classes via @Import
 * === AI-NOTE-END ===
 * </pre>
 */
@Configuration(proxyBeanMethods = false)
@Import({
    FooApiConfig.class,
    FooGrpcConfig.class,
    FooConsumerConfig.class,
    FooJobConfig.class
})
public class FooAllinoneConfig {
}
