plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-security-code-core"))
    implementation("org.springframework.data:spring-data-redis")
}