plugins {
    // NOTE: use this convention plugin for java library project
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-foo-domain"))
    api("org.springframework:spring-tx")
}
