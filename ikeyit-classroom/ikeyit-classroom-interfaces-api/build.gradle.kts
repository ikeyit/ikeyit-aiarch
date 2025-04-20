plugins {
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-classroom-infrastructure"))
    implementation("com.ikeyit:ikeyit-common-web")
    implementation("com.ikeyit:ikeyit-security-resource")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}