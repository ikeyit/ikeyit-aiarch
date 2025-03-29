plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api(project(":ikeyit-common-exception"))
    api(project(":ikeyit-common-storage"))
    api("org.springframework:spring-context")
    api("org.springframework.boot:spring-boot")
    implementation("org.slf4j:slf4j-api")
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:s3-transfer-manager")
    implementation("software.amazon.awssdk:sts")
}

