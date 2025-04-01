plugins {
    // NOTE: use this convention plugin for spring boot project
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-account-infrastructure"))
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
}
