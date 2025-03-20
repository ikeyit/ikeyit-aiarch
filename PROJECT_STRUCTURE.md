# The codebase uses Gradle as the build tool.
## Gradle conventions
- Always use Kotlin DSL for Gradle scripts.
- Use pre-compiled script plugins as convention plugins. FYI, refer to https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html

## Codebase Structure
The codebase is structured as a monorepo that contains multiple projects.
Each specific project is placed in a top-level folder as a composite build, meaning it can be treated as an individual Gradle project.
Within each composite build, there may be multiple Gradle subprojects.
```
├── ikeyit-build-logic // composite build. Impliment convention plugins
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-build-platform // composite build. Manage dependencies and align the versions
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-common // composite build. Define common utilities and supports
│    ├── ...
│    └── ikeyit-common-data // subproject. Utilities for data access
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-common-exception // subproject. Utilities for exception handling
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
├── ikeyit-account // composite build. User management, identity provider, and SSO
│    ├── ...
│    └── ikeyit-account-domain // subproject. Domain layer for account service
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-application // subproject. Applicaiton layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-infrastructure // subproject. Infrastructure layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-interfaces-api // subproject. Interface layer for rest api
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
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
FYI, refer to https://docs.gradle.org/current/userguide/composite_builds.html

Special composite builds:
### ikeyit-build-logic
Stores our own Gradle convention plugins for sharing build logic. 
- build-java plugin. Defines the conventions for building java, e.g. Java version, JUnit Test as test framework etc.
- build-java-library plugin. Apply the above build-java and define extra conventions for building java library.
- build-spring-boot plugin. Apply the above build-java-library and define extra conventions for building spring boot applications.
### ikeyit-build-platform
It's like BOM in the Maven world and ensures that all dependencies in a project align with a consistent set of versions.
- ikeyit-java-platform is used to define the versions for all java-related dependencies.

# Code structure conventions
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
- java package name starts with com.ikeyit