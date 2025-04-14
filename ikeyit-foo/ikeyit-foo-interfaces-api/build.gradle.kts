plugins {
    // AI-NOTE: Use this convention plugin for spring boot project
    id("com.ikeyit.build-spring-boot")
}

// AI-NOTE: Make sure all the following dependencies added
dependencies {
    implementation(project(":ikeyit-foo-infrastructure"))
    implementation("com.ikeyit:ikeyit-common-web")
    implementation("com.ikeyit:ikeyit-security-resource")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
