plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    api(project(":ikeyit-access-sdk"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

