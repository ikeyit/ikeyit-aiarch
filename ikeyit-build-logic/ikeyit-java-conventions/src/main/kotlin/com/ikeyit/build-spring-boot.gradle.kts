package com.ikeyit

plugins {
    id("com.ikeyit.build-java-library")
    id("org.springframework.boot")
}

dependencies {
    // Spring Boot 核心依赖
    implementation("org.springframework.boot:spring-boot-starter")
    // Spring Boot 测试依赖
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// 配置Spring Boot插件
springBoot {
    buildInfo()
}

// 配置打包任务
tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        ))
    }
}