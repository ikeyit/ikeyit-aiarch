plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-foo-interfaces-api"))
    implementation(project(":ikeyit-foo-interfaces-consumer"))
    implementation(project(":ikeyit-foo-interfaces-job"))
    implementation(project(":ikeyit-foo-interfaces-grpc"))
}
