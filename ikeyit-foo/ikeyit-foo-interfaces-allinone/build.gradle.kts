plugins {
    // AI-NOTE: Use this convention plugin for spring boot project
    id("com.ikeyit.build-spring-boot")
}

// AI-NOTE: Include all interfaces projects into dependencies
dependencies {
    implementation(project(":ikeyit-foo-interfaces-api"))
    implementation(project(":ikeyit-foo-interfaces-consumer"))
    implementation(project(":ikeyit-foo-interfaces-job"))
    implementation(project(":ikeyit-foo-interfaces-grpc"))
}
