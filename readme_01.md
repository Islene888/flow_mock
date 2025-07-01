# Flow Mock 微服务 Demo - 云原生开发与部署实践

## 项目简介

本项目基于 Spring Boot，演示了一个后端微服务从本地开发到云原生交付的完整链路，包括容器化、镜像仓库、Kubernetes 部署、多副本自动扩缩容和可视化运维。适合云原生开发与 DevOps 实践入门。

---

## 已完成内容

### 1. **Spring Boot 微服务开发**

* 使用 [Spring Initializr](https://start.spring.io/) 快速生成项目骨架（在线配置依赖后下载 zip 包）
* 使用 Spring Boot 搭建基础 RESTful 服务
* 实现 `/api/hello` 健康检查接口

### 2. **Docker 容器化**

* 编写 Dockerfile，将应用打包为标准容器镜像
* 本地构建、运行和调试 Docker 镜像

### 3. **推送镜像到 Docker Hub**

* 注册并登录 Docker Hub
* 用 `docker tag` 和 `docker push` 命令将镜像上传到远程仓库
  示例镜像地址：`islene/flow-mock:0.1`

### 4. **Kubernetes 本地集群部署与弹性管理**

* 利用 Docker Desktop 集成的 Kubernetes 集群，实现本地多副本自动编排
* 编写 `k8s-deployment.yaml`，完成 Deployment（支持弹性扩缩容）和 NodePort Service（主机端口暴露）
* 用 `kubectl` 命令发布和管理应用，验证高可用和弹性能力

### 5. **K8s 可视化运维**

* 使用 IntelliJ IDEA Kubernetes 插件，图形化管理集群资源与监控

---

## 技术栈与工具

* **Java 17** / Spring Boot 3.x
* **Maven**
* **Docker** / Docker Desktop
* **Docker Hub**
* **Kubernetes**（docker-desktop 本地集群）
* **kubectl**
* **IntelliJ IDEA** + Kubernetes 插件

---

## 目录结构

```
.
├── src/                  # Spring Boot 源码
├── Dockerfile            # 容器镜像构建文件
├── k8s-deployment.yaml   # Kubernetes 应用部署配置
├── README.md
└── ...
```

---

## 快速体验流程

1. **初始化项目（可选）**

    * 访问 [https://start.spring.io/](https://start.spring.io/) 选择依赖生成并下载 Spring Boot 项目 zip 包

2. **本地构建镜像**

   ```bash
   ./mvnw clean package -DskipTests
   docker build -t islene/flow-mock:0.1 .
   ```

3. **推送到 Docker Hub**

   ```bash
   docker push islene/flow-mock:0.1
   ```

4. **Kubernetes 集群部署**

   ```bash
   kubectl apply -f k8s-deployment.yaml
   kubectl get pods,svc
   # 浏览器访问：http://localhost:30080/api/hello
   ```

5. **K8s 自动扩缩容 & 滚动升级体验**

    * 扩缩容：

      ```bash
      kubectl scale deployment flow-mock --replicas=5
      kubectl get pods
      ```
    * 滚动升级（新版本镜像）：

      ```bash
      kubectl set image deployment/flow-mock flow-mock=islene/flow-mock:0.2
      kubectl rollout status deployment/flow-mock
      ```
    * 回滚：

      ```bash
      kubectl rollout undo deployment/flow-mock
      ```

---


## 本地 MySQL & Docker MySQL 环境配置与排障实录（实用技巧）

开发微服务通常会遇到**本机数据库与容器数据库端口冲突、权限设置、连接失败**等问题。以下是本项目开发过程真实排障记录，帮助你少踩坑：

### 1. **端口冲突检测与本地服务停用**

* **现象**：容器 MySQL 启动正常，但 `mysql -uroot -p123456 -h127.0.0.1 -P3306` 连接报 “Access denied”。
* **排查**：

   * 用 `lsof -i :3306` 检查，发现本机有 MySQL 服务占用 3306 端口。
   * 停掉本地 MySQL 服务（macOS 用户用 `brew services stop mysql`）。
   * 再次 `lsof -i :3306`，确保只有 Docker 的 mysqld 占用端口。

### 2. **容器 MySQL 权限初始化**

* 进入容器：

  ```bash
  docker exec -it mysql-demo bash
  mysql -uroot -p123456
  ```
* 创建开发专用账户，并授权远程访问：

  ```sql
  CREATE USER 'flowuser'@'%' IDENTIFIED BY 'flowpass';
  GRANT ALL PRIVILEGES ON *.* TO 'flowuser'@'%' WITH GRANT OPTION;
  FLUSH PRIVILEGES;
  ```
* 检查所有用户、插件、权限：

  ```sql
  SELECT host, user, plugin FROM mysql.user;
  SHOW PLUGINS;
  ```

### 3. **账户登录与连通性验证**

* 验证账户（主机运行）：

  ```bash
  mysql -uflowuser -pflowpass -h127.0.0.1 -P3306
  ```

  应该可以正常进入。
* 验证端口监听状态：

  ```bash
  nc -vz 127.0.0.1 3306
  ```

### 4. **开发常见问题与解决经验总结**

* 多个 MySQL 实例/端口冲突是最常见本地开发困扰，优先确认端口归属。
* 若遇到权限相关、插件（如 caching\_sha2\_password, mysql\_native\_password）报错，务必确认 Docker 镜像版本与插件实际支持。
* 强烈建议开发、测试与线上使用“非 root 专属账号”，方便团队协作与安全运维。
