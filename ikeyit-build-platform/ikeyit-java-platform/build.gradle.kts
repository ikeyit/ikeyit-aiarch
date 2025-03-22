plugins {
    `java-platform`
}

group = "com.ikeyit"
version = "1.0.0-SNAPSHOT"

javaPlatform {
    allowDependencies()
}

dependencies {
    // Spring Boot BOM
    api(platform("org.springframework.boot:spring-boot-dependencies:3.4.0"))
    // Spring Cloud BOM
    api(platform("org.springframework.cloud:spring-cloud-dependencies:2024.0.1"))
    // Spring Security BOM
    api(platform("org.springframework.security:spring-security-bom:6.4.4"))
    
    // Testing
    api(platform("org.junit:junit-bom:5.12.1"))
    api(platform("org.mockito:mockito-bom:5.16.1"))
    
    // Database
    constraints {
        api("org.postgresql:postgresql:42.7.3")
        
        // Redis
        api("org.redisson:redisson:3.28.1")
        api("com.github.ben-manes.caffeine:caffeine:3.1.8")
        
        // Kafka
        api("org.apache.kafka:kafka-clients:3.7.0")
        
        // gRPC
        api("io.grpc:grpc-netty-shaded:1.64.0")
        api("io.grpc:grpc-protobuf:1.64.0")
        api("io.grpc:grpc-stub:1.64.0")
        
        // JWT
        api("io.jsonwebtoken:jjwt-api:0.12.5")
        api("io.jsonwebtoken:jjwt-impl:0.12.5")
        api("io.jsonwebtoken:jjwt-jackson:0.12.5")
    }
}