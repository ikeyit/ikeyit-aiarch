plugins {
    // NOTE: use this convention plugin for java library project
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-foo-application"))
    api("com.ikeyit:ikeyit-common-data-spring")
    api("com.ikeyit:ikeyit-common-spring-jdbc")
    api("org.springframework.boot:spring-boot")
    api("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.slf4j:slf4j-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
