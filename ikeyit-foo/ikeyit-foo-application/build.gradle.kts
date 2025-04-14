plugins {
    // AI-NOTE: Use this convention plugin for java library project
    id("com.ikeyit.build-java-library")
}

// AI-NOTE: Make sure all the following dependencies added
dependencies {
    api(project(":ikeyit-foo-domain"))
    api("org.springframework:spring-tx")
}
