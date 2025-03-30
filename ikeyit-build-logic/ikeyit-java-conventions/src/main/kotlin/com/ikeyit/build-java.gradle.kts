package com.ikeyit

plugins {
    java
}

group = "com.ikeyit"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

dependencies {
    implementation(platform("com.ikeyit:ikeyit-java-platform"))
    implementation("org.slf4j:slf4j-api")
    compileOnly("com.google.code.findbugs:jsr305")
    testCompileOnly("com.google.code.findbugs:jsr305")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}