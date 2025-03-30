plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-foo-infrastructure"))
    implementation(project(":ikeyit-foo-sdk"))
    implementation("net.devh:grpc-server-spring-boot-starter")
}
