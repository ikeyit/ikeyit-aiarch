plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-account-interfaces-api"))
    implementation(project(":ikeyit-account-interfaces-consumer"))
    implementation(project(":ikeyit-account-interfaces-job"))
    implementation(project(":ikeyit-account-interfaces-grpc"))
}
