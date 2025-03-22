# ikeyit-demo

这是一个Spring Boot示例项目，展示了基本的Spring Boot应用程序结构和功能。

## 项目结构

- **ikeyit-demo-app**: Spring Boot应用程序主模块

## 功能特性

- 基本的REST API
- Spring Boot Actuator监控
- 配置管理

## 构建和运行

### 构建项目

```bash
./gradlew :ikeyit-demo:ikeyit-demo-app:build
```

### 运行应用

```bash
./gradlew :ikeyit-demo:ikeyit-demo-app:bootRun
```

## API接口

- `GET /api/hello`: 返回问候消息

## 监控端点

- `GET /actuator/health`: 健康检查
- `GET /actuator/info`: 应用信息
- `GET /actuator/metrics`: 应用指标