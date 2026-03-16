# NGS AI Code Mother

<div align="center">

**企业级 AI 代码生成平台**

基于 Spring Boot 3 + LangChain4j + Vue 3 开发，对标大厂的企业级 AI 代码生成平台

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.x-blue.svg)](https://baomidou.com/)
[![Vue](https://img.shields.io/badge/Vue-3.x-brightgreen.svg)](https://vuejs.org/)
[![LangChain4j](https://img.shields.io/badge/LangChain4j-Latest-purple.svg)](https://github.com/langchain4j/langchain4j)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

</div>

---

## 📖 项目简介

**NGS AI Code Mother** 是一套以 **AI 开发实战 + 后端架构设计** 为核心的企业级项目。

本项目基于 **Spring Boot 3** + **LangChain4j** + **Vue 3** 技术栈开发，对标大厂标准，致力于打造专业的 **企业级 AI 代码生成平台**。

> 💡 **当前状态**: 后端核心功能已实现，前端开发中...

## ✨ 核心特性

- 🚀 **AI 驱动**: 集成 LangChain4j，支持智能代码生成与辅助开发
- 🏗️ **企业级架构**: 规范的分层设计，符合大厂开发标准
- 🔐 **安全可靠**: 完善的权限控制与安全机制
- 📦 **开箱即用**: 标准化配置，快速部署启动
- 🎯 **高效开发**: 模块化设计，支持灵活扩展

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本     | 说明 |
|------|--------|------|
| Spring Boot | 3.5.12 | 核心框架 |
| Java | 21     | 开发语言 |
| MyBatis Plus | 3.5.16 | ORM 框架 |
| MySQL | 8.0+   | 数据库 |
| LangChain4j | Latest | AI 大模型集成框架 |

### 前端技术（开发中）

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Vite | Latest | 构建工具 |
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
- Maven 3.6+
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

- [x] 用户管理（登录、注册、权限控制）
- [x] 统一响应封装
- [x] 全局异常处理
- [x] 跨域配置
- [x] MyBatis Plus 集成
- [x] 健康检查接口

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