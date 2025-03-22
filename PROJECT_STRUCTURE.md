# 本代码库使用Gradle作为构建工具。
## Gradle约定
- 始终使用Kotlin DSL编写Gradle脚本。
- 使用“预编译脚本插件”(Pre-compiled Script Plugins)作为约定插件。参考：https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html
- 使用Gradle Wrapper来执行构建任务
- 新建项目时version应设置为1.0.0-SNAPSHOT，group为com.ikeyit

## 代码库结构
本代码库是一个包含多个项目的超级项目（Monorepo）。
每个具体的项目都放置在顶层文件夹中作为一个组合构建（composite build），这意味着它可以被视为一个独立的Gradle项目。
在每个组合构建中，可能包含多个Gradle子项目。
```
├── ikeyit-build-logic // 组合构建。实现约定插件
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-build-platform // 组合构建。管理依赖并统一版本
│   ...
│   ├── settings.gradle.kts
│   └── build.gradle.kts
├── ikeyit-common // 组合构建。定义通用工具和支持
│    ├── ...
│    └── ikeyit-common-data // 子项目。数据访问工具
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-common-exception // 子项目。异常处理工具
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
├── ikeyit-account // 组合构建。用户管理、身份提供者和单点登录
│    ├── ...
│    └── ikeyit-account-domain // 子项目。账户服务的领域层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-application // 子项目。应用层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-infrastructure // 子项目。基础设施层
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-account-interfaces-api // 子项目。REST API接口层
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
includeBuild("ikeyit-common")
```
ikeyit-common-data和ikeyit-common-exception是它的子项目。它们通过`include`方法包含在"/ikeyit-common/settings.gradle.kts"中。
```kotlin
// /ikeyit-common/settings.gradle.kts
include("ikeyit-common-data")
include("ikeyit-common-exception")
```
参考：https://docs.gradle.org/current/userguide/composite_builds.html

特殊的组合构建：
### ikeyit-build-logic
存储我们自己的Gradle约定插件，用于共享构建逻辑。
- build-java插件。定义构建Java的约定，例如设置Java版本为21、确保编译，测试，运行时编码使用UTF8，编译时使用-parameters参数，使用JUnit Test作为测试框架等。
- build-java-library插件。应用上述build-java插件并定义构建Java库的额外约定。
- build-spring-boot插件。应用上述build-java-library插件并定义构建Spring Boot应用的额外约定。
### ikeyit-build-platform
类似于Maven世界中的BOM，确保项目中的所有依赖都使用一致的版本集。
- ikeyit-java-platform用于定义所有Java相关依赖的版本。

# 实施DDD时的项目结构
当项目适合应用DDD时，项目应使用以下结构
```
├── ikeyit-foo // 组合构建
│    ├── ...
│    └── ikeyit-foo-domain // 领域层子项目
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-application // 应用层子项目
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-infrastructure // 基础设施层子项目
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-xxx-api // 接口层子项目，提供REST API
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-comsumer // 接口层子项目，用于消费其他微服务的消息
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-job // 接口层子项目，用于运行定时任务
│    │    │   ...
│    │    └── build.gradle.kts
│    └── ikeyit-foo-interfaces-grpc // 接口层子项目，提供gRPC API
│    │    │   ...
│    │    └── build.gradle.kts
│    ├── settings.gradle.kts
│    └── build.gradle.kts
```