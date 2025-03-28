package com.ikeyit

plugins {
    id("com.ikeyit.build-java")
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
}