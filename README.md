## What is IKEYIT AiArch
IKEYIT AiArch is a collection of boilerplate projects (backend) for building large-scale internet applications. The code is collaboratively written by LLMs and humans. It attempts to implement a large project in a way that is easy for AI to understand and write code, exploring how to make AI a high-quality programming assistant.

### Architecture
The architecture covers:
- Microservice Architecture
- Domain-Driven Design (DDD), Clean Architecture
- Event-Driven Architecture (EDA)

### Distributed System Technologies
- Distributed Transactions, Eventually Consistency
- Distributed Locks
- Multi-level Caching
- Data Consistency
- Data Sharding, Read/Write Separation
- Fault Tolerance

### Business Scenarios
- Authentication and Authorization
- Permission Management
- SAAS Multi-tenancy
- E-commerce
- Big Data Processing

### Philosophy
- Reduce dependencies, as excessive dependencies increase complexity, making it easier for AI to make mistakes and increasing the learning curve for humans
- Clearer layering and modularization to help AI focus on specific issues
- Use AI-friendly programming practices
  - Name variables, methods, and classes with clear business semantics, for example: prefer calculateTotalPrice() over abbreviated calcTotPr()
  - Provide clear and comprehensive comments
  - Use self-descriptive data formats: for example, use strings for enums instead of numbers. Use {status:"ACTIVE"} instead of {status:1}
- Don't use other tools/libraries for tasks that AI can handle. For example: Lombok, MapStruct, Swagger UI, Yeoman, JHipster, Lodash / Underscore.js, ORM
- Choose mainstream technologies and solutions, avoid niche ones. The more mainstream, the more training data AI has, the better the results
- Use native development (like Swift/Kotlin) instead of cross-platform frameworks (like Flutter/React Native). AI greatly reduces the barrier to native development, making an additional abstraction layer unnecessary while retaining all native development capabilities.

### Other Documentation
- Project Repository Structure: [PROJECT_STRUCTURE.zh-CN.md](docs/PROJECT_STRUCTURE.md)
- AI Programming Best Practices Collection and Sharing: [BEST_PRACTICE.zh-CN.md](docs/BEST_PRACTICE.md)