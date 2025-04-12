## 什么是IKEYIT AiArch
IKEYIT AiArch是一组样板工程（后端）用来搭建大型互联网项目。代码由LLM和人类共同编写。尝试一种方便AI理解，编写代码的方式来实现一个大型项目， 探索如何让AI成为高质量的编程助手。

### 架构
架构方面会涵盖：
- 微服务架构
- 领域驱动设计DDD， 整洁架构
- 事件驱动架构EDA

### 分布式系统技术
- 分布式事务，最终一致性
- 分布式锁
- 多级缓存
- 数据一致性
- 数据分片，读写分离
- 容错处理

### 业务场景
- 认证与授权
- 权限管理
- SAAS多租户
- 电商
- 大数据处理

### 理念
- 减少依赖，过多依赖增加了复杂度，AI更容易犯错，人类学习成本也高
- 更加清晰的分层，分模块，以便AI更容易聚焦问题
- 使用对AI友好的编程方式
  - 用清晰更具有业务语义的方式来命名变量，方法和类，比如：优先用calculateTotalPrice()而非缩写calcTotPr()
  - 提供清晰丰富的注释
  - 使用自带语义的数据格式：比如,枚举使用字符串，而不是使用数字表示。使用{status:"ACTIVE"}而非{status:1}
- AI能办的事情，就不要使用其它工具/类库来做。比如Lombok，MapStruct
  ，Swagger UI，Yeoman、JHipster，Lodash / Underscore.js，ORM
- 选择主流的技术和方案，避免冷门。越主流，AI训练时获得相关资料越多，越容易产出好的结果
- 使用纯原生开发（如Swift/Kotlin）而非跨平台框架（如Flutter/React Native），AI大大降低了原生开发的门槛，没必要再增加一层抽象，也能使用原生开发的所有能力。

### 其它文档
- 代码仓库结构说明：[PROJECT_STRUCTURE.zh-CN.md](docs/PROJECT_STRUCTURE.zh-CN.md)
- 使用AI编程的最佳实践收集与分享：[BEST_PRACTICE.zh-CN.md](docs/BEST_PRACTICE.zh-CN.md)
- 技术学习路线：[LEARNING_ROADMAP.zh-CN.md](docs/LEARNING_ROADMAP.zh-CN.md)