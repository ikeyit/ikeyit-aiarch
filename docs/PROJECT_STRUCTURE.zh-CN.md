# 本代码库使用Gradle作为构建工具。
## Gradle约定
- 始终使用Kotlin DSL编写Gradle脚本。
- 使用“预编译脚本插件”(Pre-compiled Script Plugins)作为约定插件。参考：https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html
- 使用Gradle Wrapper来执行构建任务

## 代码库结构
本代码库是一个包含多个项目的超级项目（Monorepo）。
每个具体的项目都放置在顶层文件夹中作为一个组合构建（composite build），这意味着它可以被视为一个独立的Gradle项目。
在每个组合构建中，可能包含多个Gradle子项目。
```
├── ikeyit-build-logic // 复合构建。实现约定插件
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-build-platform // 复合构建。管理依赖并统一版本
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-common // 复合构建。定义通用工具和支持
│    ├── ...
│    └── ikeyit-common-data // 子项目。数据访问工具
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-common-exception // 子项目。异常处理工具
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
├── ikeyit-foo // 复合构建, DDD风格的项目
│    ├── ...
│    └── ikeyit-foo-domain // 子项目。领域层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-application // 子项目。应用层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-infrastructure // 子项目。基础设施层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-api // 子项目。REST API 接口层, 可独立部署
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-consumer // 子项目。消息队列消费者接口层, 可独立部署
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-job // 子项目。后台任务接口层, 可独立部署
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-grpc // 子项目。gRPC API 接口层, 可独立部署
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-allinone // 子项目。包含上述所有接口的单体应用
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-sdk // 子项目。该服务的客户端 SDK
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
└── settings.gradle.kts
```
### 组合构建
组合构建通常是独立开发的，这使得多个团队可以在同一代码库上工作。另一个好处是，如果组合构建变得太大，可以将其移出作为独立的代码库。
例如，"ikeyit-common"是一个组合构建。它通过`includeBuild`方法包含在根目录的settings.gradle.kts中。
```kotlin
// /settings.gradle.kts
includeBuild("ikeyit-build-logic")
includeBuild("ikeyit-build-platform")
includeBuild("ikeyit-foo")
includeBuild("ikeyit-common")
```
ikeyit-common-data和ikeyit-common-exception是它的子项目。它们通过`include`方法包含在"/ikeyit-common/settings.gradle.kts"中。
```kotlin
// /ikeyit-common/settings.gradle.kts
include("ikeyit-common-data")
include("ikeyit-common-exception")
```
参考：https://docs.gradle.org/current/userguide/composite_builds.html

### 构建逻辑 ikeyit-build-logic
实现我们自己的Gradle约定插件，用于共享构建逻辑。
- com.ikeyit.build-java插件。定义构建Java的约定，例如设置Java版本为21、确保编译，测试，运行时编码使用UTF8，编译时使用-parameters参数，使用JUnit Test作为测试框架等。
- com.ikeyit.build-java-library插件。应用com.ikeyit.build-java插件，定义构建Java库的约定。
- com.ikeyit.build-spring-boot插件。应用com.ikeyit.build-java-library插件，定义构建Spring Boot应用的约定。
- com.ikeyit.build-grpc插件。应用com.ikeyit.build-java-library插件，定义编译Protocol Buffer，生成grpc java代码的约定。

### 依赖管理 ikeyit-build-platform
类似于Maven世界中的BOM，确保项目中的所有依赖都使用一致的版本。
- ikeyit-java-platform用于定义所有Java相关依赖的版本。