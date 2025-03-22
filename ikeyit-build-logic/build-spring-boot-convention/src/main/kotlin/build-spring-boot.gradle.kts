plugins {
    id("build-java-library")
    id("org.springframework.boot")
}

dependencies {
    // Spring Boot 核心依赖
    implementation("org.springframework.boot:spring-boot-starter")
    
    // Spring Boot Web 依赖
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Boot 测试依赖
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    // Spring Boot Actuator 依赖
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // Spring Boot DevTools 依赖（开发环境）
    developmentOnly("org.springframework.boot:spring-boot-devtools")
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