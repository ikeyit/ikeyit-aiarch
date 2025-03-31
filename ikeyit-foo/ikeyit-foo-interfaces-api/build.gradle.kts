plugins {
    // NOTE: use this convention plugin for spring boot project
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-foo-infrastructure"))
    implementation("com.ikeyit:ikeyit-common-web")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
