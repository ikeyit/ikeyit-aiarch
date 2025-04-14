plugins {
    // AI-NOTE: Use this convention plugin for java library project
    id("com.ikeyit.build-java-library")
}

// AI-NOTE: Make sure all the following dependencies added
dependencies {
    api("com.ikeyit:ikeyit-common-data")
    api("com.ikeyit:ikeyit-common-exception")
    api("org.springframework:spring-context")
    api("javax.annotation:javax.annotation-api")
}
