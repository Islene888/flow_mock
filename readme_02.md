# Flow Mock 微服务 Demo - 第二阶段：功能完备的后端服务

## 项目简介

本项目基于第一阶段“云原生部署与运维”实践，已从简单 Demo 演进为**具备真实业务能力的后端微服务系统**。它不仅拥有清晰的数据模型和复杂业务逻辑，还集成了数据库持久化、Redis 缓存、幂等业务保障等关键特性，贴合真实线上服务设计。

核心目标：既可本地一键开发调试，也能平滑容器化、Kubernetes 云原生部署。

---

## 主要特性与功能亮点

### 1. 复杂数据模型与 JPA 持久化

* 设计了 User、Prompt、UserPromptAction 三大核心实体。
* 使用 Spring Data JPA + Hibernate，数据持久化到 MySQL。

### 2. 完整 RESTful API 体系

* 支持用户、Prompt、用户行为（点赞、收藏）等全量 API。
* 支持多条件查询和分页检索。

### 3. 业务逻辑分层（Service Layer）

* 所有核心业务由 @Service 层实现，确保 Controller 简洁、业务聚合。
* 关键操作如点赞、收藏保证事务原子性和幂等性。

### 4. Redis 分布式缓存

* 集成 Spring Cache + Redis，对热点数据（如用户信息）加速访问。
* 支持缓存失效与更新机制。

---

## API 端点速查表

| 模块     | Method | Endpoint                                | 说明                |
| ------ | ------ | --------------------------------------- | ----------------- |
| User   | POST   | `/api/user/save`                        | 创建或更新用户           |
|        | GET    | `/api/user/get/{id}`                    | 根据ID获取用户（优先读缓存）   |
| Prompt | POST   | `/api/prompt/create`                    | 新建 Prompt         |
|        | GET    | `/api/prompt/get/{id}`                  | 获取 Prompt 详情      |
|        | GET    | `/api/prompt/list`                      | 查询 Prompt（分页/多条件） |
| Action | POST   | `/api/prompts/{promptId}/like`          | 点赞 Prompt         |
|        | DELETE | `/api/prompts/{promptId}/like`          | 取消点赞              |
|        | POST   | `/api/prompts/{promptId}/favorite`      | 收藏 Prompt         |
|        | DELETE | `/api/prompts/{promptId}/favorite`      | 取消收藏              |
| Query  | GET    | `/api/users/{userId}/liked-prompts`     | 获取用户点赞的 Prompt 列表 |
|        | GET    | `/api/users/{userId}/favorited-prompts` | 获取用户收藏的 Prompt 列表 |

> 更多参数和过滤逻辑可查 API 源码或 Swagger 文档（如已集成）。

---

## 技术栈与依赖

* **Java 17+ / Spring Boot 3.x**
* **Spring Data JPA** （MySQL 持久化）
* **Spring Cache**（缓存抽象层）
* **MySQL 8.x** （推荐使用 Docker 启动）
* **Redis 6.x+** （推荐使用 Docker 启动）
* **Maven** （项目构建）
* **Docker / Docker Compose**
* **Kubernetes (docker-desktop 或 minikube 本地集群)**
* **IntelliJ IDEA / VSCode / Postman / curl**

---

## 本地开发与一键启动

### 1. 启动本地依赖服务（MySQL + Redis）

在项目根目录下新建 `docker-compose.yml`：

```yaml
version: '3.8'
services:
  mysql-db:
    image: mysql:8.0
    container_name: mysql-local
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=rootpass
      - MYSQL_DATABASE=flow_mock_db
      - MYSQL_USER=flowuser
      - MYSQL_PASSWORD=flowpass
    volumes:
      - mysql_data:/var/lib/mysql

  redis-cache:
    image: redis:6.2-alpine
    container_name: redis-local
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

启动依赖服务：

```bash
docker-compose up -d
```

---

### 2. 配置 Spring Boot

编辑 `src/main/resources/application.properties`：

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/flow_mock_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=flowuser
spring.datasource.password=flowpass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

---

### 3. 启动 Spring Boot 应用

**IDE 启动**
直接运行 `FlowMockApplication` 主类。

**命令行启动**

```bash
./mvnw clean package -DskipTests
java -jar target/flow_mock-0.0.1-SNAPSHOT.jar
```

---

### 4. 测试 API

推荐使用 Postman 或 curl。所有端点见上表。

---

## 云原生部署（与第一阶段一致）

### 构建与推送镜像

```bash
docker build -t your-dockerhub-id/flow-mock:0.2 .
docker push your-dockerhub-id/flow-mock:0.2
```

### 部署到 Kubernetes

```bash
kubectl apply -f k8s-deployment.yaml
```