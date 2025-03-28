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

    constraints {
        api("org.postgresql:postgresql:42.7.4")
        api("com.google.guava:guava:31.1-jre")
        api("org.apache.commons:commons-lang3:3.14.0")
        api("javax.annotation:javax.annotation-api:1.3.2")
        api("com.google.code.findbugs:jsr305:3.0.2")
    }
}