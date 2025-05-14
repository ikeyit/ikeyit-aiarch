plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-access-infrastructure"))
    implementation(project(":ikeyit-access-sdk"))
    implementation("com.ikeyit:ikeyit-common-grpc-server")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("net.devh:grpc-server-spring-boot-starter")
}
