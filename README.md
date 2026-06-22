为了补充知识点，并提高架构能力（AI 时代必须要有的能力），决定自主开发一个 RPC 框架

## 技术选型（暂定）

后端技术以 Java 为主，但所有的思想和设计都是可以复用到其他语言的，代码不同罢了。

⭐️ Vert.x 框架

⭐️ Etcd 云原生存储中间件（jetcd 客户端）

ZooKeeper 分布式协调工具（curator 客户端）

⭐️ SPI 机制

⭐️ 多种序列化器
- JSON 序列化
- Kryo 序列化
- Hessian 序列化

⭐️ Spring Boot Starter 开发

⭐️ 负载均衡、重试和容错机制

⭐️ 反射、动态代理和注解驱动

Guava Retrying 重试库

JUnit 单元测试

Logback 日志库

Hutool、Lombok 工具库

