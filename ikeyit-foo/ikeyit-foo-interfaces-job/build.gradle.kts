plugins {
    // AI-NOTE: Use this convention plugin for spring boot project
    id("com.ikeyit.build-spring-boot")
}

dependencies {
    implementation(project(":ikeyit-foo-infrastructure"))
}
