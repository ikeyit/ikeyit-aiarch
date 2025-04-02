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
    api(platform("io.grpc:grpc-bom:1.63.0"))
    api(platform("software.amazon.awssdk:bom:2.29.9"))
    constraints {
        api("org.springframework.security:spring-security-oauth2-authorization-server:1.4.2")
        api("org.postgresql:postgresql:42.7.4")
        api("com.google.guava:guava:31.1-jre")
        api("org.apache.commons:commons-lang3:3.14.0")
        api("javax.annotation:javax.annotation-api:1.3.2")
        api("com.google.code.findbugs:jsr305:3.0.2")
        api("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
        api("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
    }
}