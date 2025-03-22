# ikeyit-common

通用工具和支持模块，提供整个系统中可复用的基础功能。

## 子模块

- **ikeyit-common-data**: 数据访问工具，提供通用的数据访问相关功能
- **ikeyit-common-exception**: 异常处理工具，提供通用的异常处理相关功能

## 使用方法

在需要使用这些通用功能的项目中，通过Gradle依赖引入相应的模块：

```kotlin
dependencies {
    implementation("com.ikeyit:ikeyit-common-data")
    implementation("com.ikeyit:ikeyit-common-exception")
}
```