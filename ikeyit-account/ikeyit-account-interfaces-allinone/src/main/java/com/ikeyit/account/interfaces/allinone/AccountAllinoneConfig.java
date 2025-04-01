package com.ikeyit.account.interfaces.allinone;

import com.ikeyit.account.interfaces.consumer.AccountConsumerConfig;
import com.ikeyit.account.interfaces.api.AccountApiConfig;
import com.ikeyit.account.interfaces.grpc.AccountGrpcConfig;
import com.ikeyit.account.interfaces.job.AccountJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
    AccountApiConfig.class,
    AccountGrpcConfig.class,
    AccountConsumerConfig.class,
    AccountJobConfig.class
})
public class AccountAllinoneConfig {
}
