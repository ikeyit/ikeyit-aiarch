plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api(project(":ikeyit-common-exception"))
    api("net.devh:grpc-client-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-aop")
}

