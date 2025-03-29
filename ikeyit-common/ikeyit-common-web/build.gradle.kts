plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api(project(":ikeyit-common-exception"))
    api("org.springframework.boot:spring-boot-starter-web")
}

