# The codebase uses Gradle as the build tool.
## Gradle conventions
- Always use Kotlin DSL for Gradle scripts.
- Use pre-compiled script plugins as convention plugins. Refer to https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html
- Always use Gradle Wrapper to execute build tasks.

## Codebase Structure
The codebase is structured as a monorepo that contains multiple projects.
Each specific project is placed in a top-level folder as a composite build, meaning it can be treated as an individual Gradle project.
Within each composite build, there may be multiple Gradle subprojects.
```
├── ikeyit-build-logic // composite build. Implement convention plugins
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
├── ikeyit-foo // composite build. DDD style project
│    ├── ...
│    └── ikeyit-foo-domain // subproject. Domain layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-application // subproject. Applicaiton layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-infrastructure // subproject. Infrastructure layer
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-api // subproject. Interface layer for rest APIs
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-consumer // subproject. Interface layer for MQ consumers
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-job // subproject. Interface layer for background jobs
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-grpc // subproject. Interface layer for gRPC APIs
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-allinone // subproject. Includes all the above interfaces in one application
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-sdk // subproject. Client SDK for this service
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
includeBuild("ikeyit-foo")
includeBuild("ikeyit-common")
```
ikeyit-common-data and ikeyit-common-exception are its subprojects. They are included in the "/ikeyit-common/settings.gradle.kts"using `include` method.
```kotlin
// /ikeyit-common/settings.gradle.kts
include("ikeyit-common-data")
include("ikeyit-common-exception")
```
Refer to https://docs.gradle.org/current/userguide/composite_builds.html

### Build Logic: ikeyit-build-logic
Implement our own Gradle convention plugins for sharing build logic. 
- "com.ikeyit.build-java" plugin. Define the conventions for building java, e.g. Java version, encoding, compilation parameters, JUnit Test as test framework etc.
- "com.ikeyit.build-java-library" plugin. Apply "com.ikeyit.build-java" plugin, and define conventions for building java library.
- "com.ikeyit.build-spring-boot" plugin. Apply "com.ikeyit.build-java-library", and define  conventions for building spring boot applications.
- "com.ikeyit.build-grpc" plugin. Apply "com.ikeyit.build-java-library", and define conventions for compiling protocol buffer, generating grpc java codes.

### Dependency Management: ikeyit-build-platform
It's like BOM in the Maven world and ensures that all dependencies in a project align with a consistent set of versions.
- ikeyit-java-platform is used to define the versions for all java-related dependencies.