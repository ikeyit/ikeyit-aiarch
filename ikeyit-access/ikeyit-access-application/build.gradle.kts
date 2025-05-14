plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-access-domain"))
    api(project(":ikeyit-access-core"))
    api("org.springframework:spring-tx")
    api("com.ikeyit:ikeyit-common-storage")
    implementation("com.google.guava:guava")
    implementation("org.slf4j:slf4j-api")
}
