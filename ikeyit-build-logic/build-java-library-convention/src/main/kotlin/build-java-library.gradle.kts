plugins {
    id("build-java")
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    // 添加常用的库依赖
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
}