plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-classroom-interfaces-api"))
    implementation(project(":ikeyit-classroom-interfaces-consumer"))
    implementation(project(":ikeyit-classroom-interfaces-job"))
    implementation(project(":ikeyit-classroom-interfaces-grpc"))
}