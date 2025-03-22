plugins {
    `kotlin-dsl`
}

group = "com.ikeyit"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation(project(":build-java-library-convention"))
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.4.0")
}