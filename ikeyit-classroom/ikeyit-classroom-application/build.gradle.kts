plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-classroom-domain"))
    api("org.springframework:spring-tx")
}