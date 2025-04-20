plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    implementation("org.slf4j:slf4j-api")
    api("com.ikeyit:ikeyit-common-exception")
    api("com.ikeyit:ikeyit-common-data")
    api("jakarta.servlet:jakarta.servlet-api")
    api("org.springframework.security:spring-security-core")
    api("org.springframework.security:spring-security-web")
    api("org.springframework.security:spring-security-config")
    api("org.springframework.security:spring-security-oauth2-core")
    api("org.springframework.security:spring-security-oauth2-jose")
    api("org.springframework.security:spring-security-oauth2-resource-server")
    api("org.springframework.boot:spring-boot-autoconfigure")
}