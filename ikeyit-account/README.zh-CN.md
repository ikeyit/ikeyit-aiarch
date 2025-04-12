# ikeyit-account

## 概述

`ikeyit-account` 模块是负责用户账户管理、认证、授权和单点登录(SSO)的核心组件。

## 主要功能

### 用户管理

- 用户注册（含邮箱/手机验证）
- 个人资料管理（头像、显示名称、性别、位置、语言偏好）
- 安全管理（密码、手机、邮箱）

### 认证与授权

- 多种认证方式（密码、验证码、社交登录）
- 记住我功能
- 身份提供者(IDP)支持OIDC(OpenId Connect)协议。为其他系统提供SSO(单点登录)服务。

## 架构

本项目遵循基于DDD原则的整洁架构，组织为以下几层：

### 领域层

核心业务逻辑和实体位于领域层：

- **实体**：`User`、`UserConnection`等
- **仓库**：`UserRepository`、`UserConnectionRepository`
- **领域事件**：各种用户相关事件，如`UserCreatedEvent`、`UserProfileUpdatedEvent`等

### 应用层

应用层编排领域对象以执行用例：

- **服务**：`UserService` - 处理用户注册、个人资料更新、密码管理等
- **DTO**：用于跨边界的数据传输对象
- **验证器**：`PasswordValidator`、`ContactInfoValidator`用于业务规则验证

### 基础设施层

实现技术能力和外部系统适配器：

- **仓库**：领域仓库的实现
- **安全**：认证和授权机制
- **外部服务**：邮件/短信集成等

### 接口层

多种部署选项，支持不同的通信协议：

- **REST API**（`ikeyit-account-interfaces-api`）：用于账户管理的RESTful端点
- **gRPC**（`ikeyit-account-interfaces-grpc`）：高性能RPC接口
- **消息消费者**（`ikeyit-account-interfaces-consumer`）：事件驱动接口
- **计划任务**（`ikeyit-account-interfaces-job`）：后台处理
- **一体化**（`ikeyit-account-interfaces-allinone`）：单体部署选项

## 设置和配置

### 前提条件

- Java 21+
- Gradle 8.13+
- PostgreSQL 14+
- Redis 6+
- S3兼容的对象存储（AWS S3、MinIO等）

### 配置
- 初始化数据库

运行SQL脚本
[account-schema.sql](ikeyit-account-infrastructure/src/main/resources/db/account-schema.sql)

- 配置属性

参考
[application.yml](ikeyit-account-interfaces-allinone/src/main/resources/application.yml)
[application-dev.yml](ikeyit-account-interfaces-allinone/src/main/resources/application-dev.yml)

关键点：数据库、Redis、对象存储

### 构建项目

```bash
# 构建所有模块
./gradlew :ikeyit-account:build

# 构建特定模块
./gradlew :ikeyit-account:ikeyit-account-interfaces-api:build
```

### 运行应用

```bash
# 运行一体化应用
./gradlew :ikeyit-account:ikeyit-account-interfaces-allinone:bootRun
```

## 集成
本项目仅包含后端。您需要将其与前端ikeyit-account-client集成才能使所有功能正常工作。
> **注意**
> 后端和前端应部署在同一域名下。在生产环境中使用反向代理（nginx、kubernetes ingress/gateway）。
> 在开发环境中，ikeyit-account-client项目已配置为使用代理（参见vite.config.js）
### 客户端SDK

`ikeyit-account-sdk`模块为其他内部服务提供了与账户服务集成的客户端库。

### API文档

REST API文档以OpenAPI格式提供，位于
[ikeyit-account-openapi.yml](ikeyit-account-interfaces-api/ikeyit-account-openapi.yml)