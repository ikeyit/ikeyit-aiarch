plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-access-interfaces-admin-api"))
    implementation(project(":ikeyit-access-interfaces-grpc"))
    implementation(project(":ikeyit-access-interfaces-consumer"))
    implementation(project(":ikeyit-access-interfaces-job"))
}
