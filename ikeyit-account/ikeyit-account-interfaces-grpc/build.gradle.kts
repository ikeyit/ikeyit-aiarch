plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-account-infrastructure"))
    implementation(project(":ikeyit-account-sdk"))
    implementation("com.ikeyit:ikeyit-common-grpc-server")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("net.devh:grpc-server-spring-boot-starter")
}