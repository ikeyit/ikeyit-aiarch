package com.ikeyit.classroom.interfaces.allinone;

import com.ikeyit.classroom.interfaces.api.ClassroomApiConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
    ClassroomApiConfig.class,
})
public class ClassroomAllinoneConfig {
}
