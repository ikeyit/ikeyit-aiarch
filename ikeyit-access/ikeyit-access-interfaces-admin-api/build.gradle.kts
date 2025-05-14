plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-access-infrastructure"))
    implementation(project(":ikeyit-access-web"))
    implementation("com.ikeyit:ikeyit-common-web")
    implementation("com.ikeyit:ikeyit-security-resource")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("io.micrometer:micrometer-observation")
    implementation("io.micrometer:micrometer-tracing")
//    implementation("io.micrometer:micrometer-tracing-bridge-otel")
//    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
//    implementation("com.netflix.archaius:archaius-core")
}
