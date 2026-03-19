# NGS AI Code Mother

<div align="center">

**企业级 AI 代码生成平台**

基于 Spring Boot 3 + LangChain4j + Vue 3 开发，对标大厂的企业级 AI 代码生成平台

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.12-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.16-blue.svg)](https://baomidou.com/)
[![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen.svg)](https://vuejs.org/)
[![LangChain4j](https://img.shields.io/badge/LangChain4j-1.12.2-purple.svg)](https://github.com/langchain4j/langchain4j)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

</div>

---

## 📖 项目简介

**NGS AI Code Mother** 是一套以 **AI 开发实战 + 后端架构设计** 为核心的企业级项目。

本项目基于 **Spring Boot 3** + **LangChain4j** + **Vue 3** 技术栈开发，致力于打造专业的 ** AI 代码生成平台**。

> 💡 **当前状态**: 后端核心功能已实现，前端开发中...

## ✨ 核心特性

- 🚀 **AI 驱动**: 集成 LangChain4j，支持智能代码生成与辅助开发
- 🏗️ **企业级架构**: 规范的分层设计，符合大厂开发标准
- 🔐 **安全可靠**: 完善的权限控制与安全机制
- 📦 **开箱即用**: 标准化配置，快速部署启动
- 🎯 **高效开发**: 模块化设计，支持灵活扩展
- 🔒 **数据脱敏**: 用户敏感信息自动脱敏处理
- 🛡️ **注解鉴权**: 基于自定义注解 + AOP 的权限校验

## 🛠️ 技术栈

### 后端技术

| 技术           | 版本     | 说明         |
|--------------|--------|------------|
| Spring Boot  | 3.5.12 | 核心框架       |
| Java         | 21     | 开发语言       |
| MyBatis Plus | 3.5.16 | ORM 框架     |
| MySQL        | 8.0+   | 数据库        |
| LangChain4j  | 1.12.2 | AI 大模型集成框架 |

### 前端技术（开发中）

| 技术         | 版本     | 说明   |
|------------|--------|------|
| Vue        | 3.x    | 前端框架 |
| Vite       | Latest | 构建工具 |
| TypeScript | Latest | 类型安全 |

## 📁 项目结构

```
ngs-ai-code-mother/
├── src/main/java/com/ngsliuji/ngsaicodemother/
│   ├── NgsAiCodeMotherApplication.java    # 启动类
│   ├── annotation/                         # 自定义注解
│   ├── aop/                               # AOP 切面
│   ├── common/                            # 通用模块
│   ├── config/                            # 配置类
│   ├── constant/                          # 常量定义
│   ├── controller/                        # 控制器
│   ├── exception/                         # 异常处理
│   ├── mapper/                            # Mapper 接口
│   ├── model/                             # 数据模型
│   │   ├── dto/                          # DTO 对象
│   │   ├── entity/                       # 实体类
│   │   ├── enums/                        # 枚举
│   │   └── vo/                           # VO 对象
│   └── service/                           # 服务层
├── src/main/resources/
│   ├── mapper/                            # MyBatis XML
│   └── application.yml                    # 配置文件
├── ngs-ai-code-mother-frontend/           # 前端项目（开发中）
└── sql/                                   # 数据库脚本
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Node.js 24+ (前端开发)

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/Ngs-liuji/ngs-ai-code-mother.git
   cd ngs-ai-code-mother
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE ngs_ai_code_mother DEFAULT CHARACTER SET utf8mb4;
   ```

3. **导入数据表**
   ```bash
   mysql -u root -p ngs_ai_code_mother < sql/create_table.sql
   ```

4. **配置数据库连接**

   修改 `src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/ngs_ai_code_mother?useUnicode=true&characterEncoding=utf-8&useSSL=false
       username: your_username
       password: your_password
   ```

5. **启动项目**
   ```bash
   mvn spring-boot:run
   ```

6. **访问接口**
   ```
   http://localhost:8080/api/health
   ```

## 📋 功能模块

### 已完成 ✅

#### 1. 用户管理模块
- [x] **用户注册** - 支持账号密码注册，包含参数校验
- [x] **用户登录** - Session 方式登录，返回脱敏后的用户信息
- [x] **用户注销** - 退出登录，清除 Session
- [x] **获取当前用户** - 获取登录用户的详细信息
- [x] **用户 CRUD** - 管理员可创建、查询、更新、删除用户
- [x] **分页查询** - 支持按条件分页获取用户列表（仅管理员）


#### 2. 统一响应封装
- [x] **BaseResponse** - 通用响应类，包含 code、data、message
- [x] **ResultUtils** - 统一结果工具类，简化响应返回
- [x] **DeleteRequest** - 通用删除请求参数
- [x] **PageRequest** - 通用分页请求参数

#### 3. 全局异常处理
- [x] **BusinessException** - 自定义业务异常
- [x] **ErrorCode** - 统一错误码定义
- [x] **GlobalExceptionHandler** - 全局异常处理器
- [x] **ThrowUtils** - 异常抛出工具类

#### 4. 跨域配置
- [x] **CorsConfig** - 全局跨域配置，支持所有 HTTP 方法

#### 5. MyBatis Plus 集成
- [x] **MyBatisPlusConfig** - MyBatis Plus 配置类
- [x] **逻辑删除** - 全局逻辑删除配置
- [x] **雪花算法 ID** - 使用 ASSIGN_ID 生成主键
- [x] **Mapper XML** - User/App/ChatHistory Mapper 配置

### 开发中 🚧

- [ ] AI 代码生成功能
- [ ] 大模型集成
- [ ] 前端界面
- [ ] 更多实用功能

## 🔗 相关资源

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [LangChain4j 文档](https://docs.langchain4j.dev/)
- [MyBatis Plus 文档](https://baomidou.com/)
- [Vue 3 官方文档](https://vuejs.org/)

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 开源协议

Apache License 2.0

## 👨‍💻 作者

**NGS_liuji**

GitHub: [@Ngs-liuji](https://github.com/Ngs-liuji)

## 📬 联系方式

如有问题，请通过 Issue 或邮件联系。

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐️ Star 支持！**

Made with ❤️ by NGS_liuji

</div>