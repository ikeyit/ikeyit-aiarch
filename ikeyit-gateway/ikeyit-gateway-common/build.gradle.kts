plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation("com.ikeyit:ikeyit-common-exception")
    implementation("com.ikeyit:ikeyit-common-data")
    implementation("org.springframework.cloud:spring-cloud-gateway-server")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.security:spring-security-oauth2-client")
}