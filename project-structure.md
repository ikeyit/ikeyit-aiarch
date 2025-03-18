# The codebase uses Gradle as the build tool.
## Gradle conventions
- Always use Gradle wrapper.
- Always use Kotlin DSL for Gradle scripts.
- Always use Kotlin DSL to implement Gradle plugins.
## Codebase Structure
The codebase is structured like a super project that contains multiple projects.
Each specific project is placed in a top-level folder as a composite build, meaning it can be treated as an individual Gradle project.
Within each composite build, there may be multiple Gradle subprojects.
```
├── ikeyit-build-logic // composite build
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-build-platform // composite build
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-common // composite build
│    ├── ...
│    └── ikeyit-common-data // subproject
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-common-exception // subproject
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
├── lib
│   ...
│   └── build.gradle.kts
└── settings.gradle.kts
```
Composite builds are usually developed independently, making them suitable for multiple teams to work on the same codebase. Another benefit is that if a composite build grows too large, it can be moved out and as a standalone repository. 
For example, "ikeyit-common" is a composite build. It is included in the root settings.gradle.kts using `includeBuild` method.
```kotlin
// /settings.gradle.kts
includeBuild("ikeyit-build-logic")
includeBuild("ikeyit-build-platform")
includeBuild("ikeyit-common")
```
ikeyit-common-data and ikeyit-common-exception are its subprojects. They are included in the "/ikeyit-common/settings.gradle.kts"using `include` method.
```kotlin
// /ikeyit-common/settings.gradle.kts
include("ikeyit-common-data")
include("ikeyit-common-exception")
```
For more details, refer to https://docs.gradle.org/current/userguide/composite_builds.html

Special composite builds:
### ikeyit-build-logic
Stores our own Gradle convention plugins for sharing build logic. 
- build-java plugin. Defines java conventions, e.g. Java version, JUnit Test as test framework etc.
- build-java-library plugin. 
- build-spring-boot plugin. Defines spring boot conventions, e.g. Spring boot version, Spring boot starter parent etc.
### ikeyit-build-platform
It's like BOM in the Maven world and ensures that all dependencies in a project align with a consistent set of versions.
- ikeyit-java-platform is used to define the versions for all java-related dependencies.

# Tech stack
- Gradle 8.13+
- Java 21+
- Spring boot 3.4.3+
- Spring Security 6.4.4+
- Prefer using JdbcTemplate over JPA and Mybatis for database access
- PostgreSQL 15+
- Redis for distributed caching
- Caffeine for in-memory caching
- Spring Cloud Stream 4.2.0 for message-driven microservices
- JUnit 5.12.1+ and Mockit 5.16.1+ for unit testing
- Follow Domain Driven Design (DDD) principles

# Code conventions
- When a project or microservice is suitable for applying DDD, the project should use the following structure
```
├── ikeyit-foo // composite build
│    ├── ...
│    └── ikeyit-foo-domain // Subproject for domain layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-application // Subproject for application layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-infrastructure // Subproject for infrastructure layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-xxx-api // Subproject for interface layer to provide rest APIs
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-comsumer// Subproject for interface layer to consumer message from other microservices
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-job // Subproject for interface layer to run scheduled jobs
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-grpc // Subproject for interface layer to provide gRPC APIs
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
```