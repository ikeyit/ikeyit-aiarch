package com.ikeyit.staff.interfaces.allinone;

import com.ikeyit.access.interfaces.admin.api.AccessAdminApiConfig;
import com.ikeyit.access.interfaces.consumer.AccessConsumerConfig;
import com.ikeyit.access.interfaces.grpc.AccessGrpcConfig;
import com.ikeyit.access.interfaces.job.AccessJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
    AccessAdminApiConfig.class,
    AccessGrpcConfig.class,
    AccessConsumerConfig.class,
    AccessJobConfig.class
})
public class AccessAllinoneConfig {
}
