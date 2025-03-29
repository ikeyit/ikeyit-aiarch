plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api("org.springframework:spring-context")
    api("org.springframework:spring-aop")
    api("org.springframework:spring-tx")
    api("org.springframework:spring-tx")
    api("org.aspectj:aspectjweaver")
    api("org.springframework:spring-jdbc")
    api("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.slf4j:slf4j-api")
}
