plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-account-application"))
    api("com.ikeyit:ikeyit-common-spring-jdbc")
    api("com.ikeyit:ikeyit-security-code-redis")
    api("com.ikeyit:ikeyit-security-codeauth-core")
    api("org.springframework.security:spring-security-core")
    api("org.springframework.security:spring-security-oauth2-core")
    api("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.data:spring-data-redis")
    runtimeOnly("org.postgresql:postgresql")
    implementation("com.ikeyit:ikeyit-common-storage-s3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
} 