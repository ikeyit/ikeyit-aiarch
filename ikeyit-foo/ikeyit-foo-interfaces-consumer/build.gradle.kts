plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-foo-infrastructure"))
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
}
