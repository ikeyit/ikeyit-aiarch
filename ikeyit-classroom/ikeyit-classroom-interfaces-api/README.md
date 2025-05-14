# ikeyit-classroom-interfaces-api

## API文档

本模块提供课堂管理系统的REST API接口，包括课程、教师、学生、课程表和报名等功能。

### OpenAPI文档

REST API文档以OpenAPI格式提供，可以在以下位置访问：
[ikeyit-classroom-openapi.yml](ikeyit-classroom-openapi.yml)

### 访问Swagger UI

当应用运行时，可以通过以下URL访问Swagger UI界面：
```
http://localhost:8080/swagger-ui/index.html
```

通过Swagger UI，您可以：
- 浏览所有可用的API端点
- 查看请求和响应模型
- 测试API调用（需要提供有效的认证令牌）

### API认证

大多数API端点需要JWT Bearer认证。在调用API时，需要在请求头中添加以下内容：
```
Authorization: Bearer {your_jwt_token}
```

### 集成说明

如需集成本API，请参考以下步骤：

1. 查阅OpenAPI文档了解可用的端点
2. 获取有效的JWT令牌用于认证
3. 使用适当的HTTP客户端发起请求

## 开发说明

### 依赖

本模块使用SpringDoc自动生成OpenAPI文档，主要依赖如下：

```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
```

### 自定义配置

OpenAPI文档的配置在`OpenApiConfig.java`类中定义，包括：
- API信息（标题、描述、版本等）
- 联系人信息
- 安全配置

如需修改API文档的基本信息，请编辑该配置类。