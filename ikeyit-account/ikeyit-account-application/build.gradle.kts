plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-account-domain"))
    api("com.ikeyit:ikeyit-common-data-spring")
    api("com.ikeyit:ikeyit-common-storage")
    api("com.ikeyit:ikeyit-security-code-core")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.security:spring-security-core")
} 