plugins {
    id("com.ikeyit.build-grpc")
}

dependencies {
    api(project(":ikeyit-access-core"))
    api("net.devh:grpc-client-spring-boot-starter")
}

