package com.ikeyit.foo.interfaces.api.config;

import com.ikeyit.common.data.spring.converter.StringToEnumWithIntConverter;
import com.ikeyit.common.web.exception.EnableRestErrorController;
import com.ikeyit.common.web.exception.IncludedFieldsConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableRestErrorController
public class FooAdminApiWebConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public StringToEnumWithIntConverter stringToEnumWithIntConverter() {
        return new StringToEnumWithIntConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public IncludedFieldsConverter includedFieldsConverter() {
        return new IncludedFieldsConverter();
    }
}

