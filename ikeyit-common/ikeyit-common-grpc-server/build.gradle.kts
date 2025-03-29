plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api(project(":ikeyit-common-exception"))
    api("net.devh:grpc-server-spring-boot-starter")
}

