plugins {
    // NOTE: use this convention plugin for java library project
    id("com.ikeyit.build-java-library")
}

dependencies {
    api("com.ikeyit:ikeyit-common-data")
    api("com.ikeyit:ikeyit-common-exception")
    api("org.springframework:spring-context")
    api("javax.annotation:javax.annotation-api")
}
