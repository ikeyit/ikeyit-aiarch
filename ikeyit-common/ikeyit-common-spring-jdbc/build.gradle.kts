plugins {
    id("com.ikeyit.build-java-library")
}

dependencies {
    api(project(":ikeyit-common-data"))
    api(project(":ikeyit-common-data-spring"))
    api("org.springframework:spring-jdbc")
    testImplementation("com.h2database:h2")
}

