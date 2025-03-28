rootProject.name = "ikeyit-build-logic"

pluginManagement {
    // gradle plugin repositories.
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        // gradle plugin repositories for compiling plugins
        gradlePluginPortal()
    }
}

include("ikeyit-java-conventions")
