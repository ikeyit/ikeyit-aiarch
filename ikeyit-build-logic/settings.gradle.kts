rootProject.name = "ikeyit-build-logic"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("build-java-convention")
include("build-java-library-convention")
include("build-spring-boot-convention")