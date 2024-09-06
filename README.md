# lemon3-基于 Spring Boot 3 的微服务应用

## 项目简介

这是一个基于 Spring Boot 3.3.2 的demo 应用，旨在有新业务开发时可以快速搭建工程，而无需再关注烦乱的依赖。本项目集成了多种先进的技术和框架，旨在提供一个稳定且高效的服务平台。主要技术栈包括：

- **Spring Boot 3.3.2** — 基础框架
- **Springdoc** — 自动生成 RESTful API 文档
- **Druid** — 数据库连接池监控
- **Nacos** — 配置中心和注册中心
- **MyBatis-Plus** — 简化 MyBatis 操作
- **Redis** — 缓存和会话管理
- **Kafka** — 消息队列
- **OpenFeign** — 声明式 HTTP 客户端
- **Spring Actuator** — 应用健康检查和监控
- **AsyncTool** — 异步处理工具库
- **Netty** — 高性能网络应用框架

## 新增功能

我们最近引入了 **Netty** 框架，并实现了一个简单的消息发送和接收的业务处理 demo。Netty 的高性能网络通信能力将为您的应用带来更快的响应速度和更高的吞吐量。

## 快速开始

### 环境准备

- Java 17+
- Maven 3.6.0+
- Docker (可选)
- K8s (可选)

### 构建项目

1. 修改配置文件以适应您的环境。
2. 执行 `Makefile` 中的指令可以快速部署项目，例如 `make all` 可以构建 Docker 镜像。