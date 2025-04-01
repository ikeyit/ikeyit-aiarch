plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-security-code-core"))
    api(project(":ikeyit-security-codeauth-core"))
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-config")
}