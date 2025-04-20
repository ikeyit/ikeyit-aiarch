plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-classroom-application"))
    api("com.ikeyit:ikeyit-common-data-spring")
    api("com.ikeyit:ikeyit-common-spring-jdbc")
    api("org.springframework.boot:spring-boot")
    api("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.slf4j:slf4j-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}