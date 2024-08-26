# [lemin3-基于 springboot3的 demo工程]

## 介绍

这是一个基于 Spring Boot 3.3.2 的微服务应用，集成了多种技术和框架，旨在提供一个高性能、可扩展的服务平台。本项目主要技术栈包括：

- **Spring Boot 3.3.2**
- **Springdoc** — 用于生成 RESTful API 文档。
- **Druid** — 数据库连接池监控。
- **Nacos** — 作为配置中心和注册中心。
- **MyBatis-Plus** — 简化 MyBatis 的 CRUD 操作。
- **Redis** — 用于缓存和会话管理。
- **Kafka** — 消息队列。
- **OpenFeign** — 声明式的 HTTP 客户端。
- **Spring Actuator** — 提供健康检查和指标监控。
- **AsyncTool** — 京东提供的异步工具库。

## 快速开始
### 环境准备

- Java 17+
- Maven 3.6.0+
- Docker (可选)
- K8s (可选)

### 构建项目
- 1、修改配置文件
- 2、执行Makefile中指令可以实现快速部署，如 make all可以快速打出 docker镜像

