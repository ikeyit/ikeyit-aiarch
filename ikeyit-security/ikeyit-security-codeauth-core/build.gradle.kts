plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-security-code-core"))
    implementation("org.springframework.security:spring-security-core")
}