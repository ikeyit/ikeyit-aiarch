plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation("com.ikeyit:ikeyit-common-exception")
    implementation("com.ikeyit:ikeyit-common-data")
    implementation(project(":ikeyit-gateway-common"))
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("io.grpc:grpc-netty")
}