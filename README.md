# NGS AI Code Mother

<div align="center">

**企业级 AI 代码生成平台**

基于 Spring Boot 3 + LangChain4j + Vue 3 开发，支持 HTML、多文件、Vue 项目等多种代码生成模式

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

本项目基于 **Spring Boot 3** + **LangChain4j** + **Vue 3** 技术栈开发，致力于打造专业的 **AI 代码生成平台**。用户可以通过自然语言描述需求，AI 自动生成 HTML、多文件项目或 Vue 项目，并支持在线预览、一键部署和代码下载。

> 💡 **当前状态**: 核心功能已完成，持续优化中...

## ✨ 核心特性

### 🤖 AI 代码生成
- 🚀 **智能代码生成**: 集成 LangChain4j，支持流式输出，实时展示生成过程
- 🎯 **多种生成模式**: 支持 HTML 单页面、多文件项目、Vue 项目三种代码生成类型
- 🧠 **智能路由**: AI 自动识别用户需求，智能选择合适的代码生成类型
- 💬 **上下文对话**: 支持多轮对话，基于历史对话持续优化代码
- 🔧 **工具调用**: 集成文件系统操作等工具，实现代码的自动生成与保存

### 🏗️ 企业级架构
- 📦 **分层设计**: 规范的分层架构（Controller-Service-Mapper），符合大厂开发标准
- 🔐 **权限控制**: 基于自定义注解 + AOP 的权限校验，支持用户/管理员角色
- 🛡️ **安全防护**: 数据脱敏、参数校验、全局异常处理
- ⚡ **性能优化**: Caffeine 本地缓存 + Redis 分布式缓存，聊天记忆持久化
- 🌐 **跨域支持**: 全局 CORS 配置，支持前后端分离部署

### 🎨 应用管理
- 📱 **应用创建**: 基于初始提示词自动创建应用，智能识别代码生成类型
- 👁️ **实时预览**: 生成的代码可在线实时预览，支持可视化编辑
- 🚀 **一键部署**: 支持将生成的应用部署到服务器，自动生成访问链接
- 📥 **代码下载**: 支持将生成的项目代码打包下载为 ZIP 文件
- 🖼️ **自动截图**: 部署后自动生成应用封面截图

### 💡 用户体验
- 🎭 **流式响应**: SSE 流式输出，实时展示 AI 生成过程
- 📊 **分页查询**: 支持我的应用、精选应用分页查询
- 🔍 **搜索过滤**: 支持按条件搜索应用
- 🎪 **精选推荐**: 管理员可设置应用优先级，展示精选应用

## 🛠️ 技术栈

### 后端技术

| 技术           | 版本         | 说明              |
|--------------|------------|-----------------|
| Spring Boot  | 3.5.12     | 核心框架            |
| Java         | 21         | 开发语言            |
| MyBatis Plus | 3.5.16     | ORM 框架          |
| MySQL        | 8.0+       | 关系型数据库          |
| Redis        | Latest     | 缓存 + Session 存储 |
| LangChain4j  | 1.12.2     | AI 大模型集成框架     |
| Reactor      | Latest     | 响应式编程，支持流式输出   |
| Caffeine     | Latest     | 本地缓存             |
| Selenium     | 4.33.0     | 网页截图            |
| 腾讯云 COS     | 5.6.227    | 对象存储（图片上传）     |
| Redisson     | 3.50.0     | Redis 客户端       |

### 前端技术

| 技术         | 版本     | 说明       |
|------------|--------|----------|
| Vue        | 3.x    | 前端框架    |
| Vite       | Latest | 构建工具    |
| TypeScript | Latest | 类型安全    |
| Ant Design Vue | 4.x | UI 组件库 |
| Pinia      | Latest | 状态管理    |

## 📁 项目结构

```
ngs-ai-code-mother/
├── src/main/java/com/ngsliuji/ngsaicodemother/
│   ├── NgsAiCodeMotherApplication.java    # 启动类
│   ├── ai/                                # AI 相关服务
│   │   ├── AiCodeGeneratorService.java    # AI 代码生成服务接口
│   │   ├── AiCodeGeneratorServiceFactory.java  # AI 服务工厂
│   │   ├── AiCodeGenTypeRoutingService.java    # 智能路由服务
│   │   └── model/                         # AI 模型定义
│   ├── annotation/                        # 自定义注解
│   │   └── AuthCheck.java                 # 权限校验注解
│   ├── aop/                               # AOP 切面
│   │   └── AuthInterceptor.java           # 权限拦截器
│   ├── common/                            # 通用模块
│   │   ├── BaseResponse.java              # 统一响应封装
│   │   ├── ResultUtils.java               # 结果工具类
│   │   └── PageRequest.java               # 分页请求
│   ├── config/                            # 配置类
│   │   ├── CorsConfig.java                # 跨域配置
│   │   ├── MyBatisPlusConfig.java         # MyBatis Plus 配置
│   │   ├── RedisChatMemoryStoreConfig.java # Redis 聊天记忆配置
│   │   └── ...                            # 其他配置
│   ├── constant/                          # 常量定义
│   ├── controller/                        # 控制器
│   │   ├── UserController.java            # 用户控制器
│   │   ├── AppController.java             # 应用控制器
│   │   ├── ChatHistoryController.java     # 聊天历史控制器
│   │   └── StaticResourceController.java  # 静态资源控制器
│   ├── core/                              # 核心逻辑
│   │   ├── AiCodeGeneratorFacade.java     # AI 代码生成门面
│   │   ├── handler/                       # 流式处理器
│   │   └── builder/                       # 项目构建器
│   ├── exception/                         # 异常处理
│   │   ├── BusinessException.java         # 业务异常
│   │   ├── ErrorCode.java                 # 错误码
│   │   └── GlobalExceptionHandler.java    # 全局异常处理器
│   ├── mapper/                            # Mapper 接口
│   ├── model/                             # 数据模型
│   │   ├── dto/                          # DTO 对象
│   │   ├── entity/                       # 实体类
│   │   ├── enums/                        # 枚举
│   │   └── vo/                           # VO 对象
│   ├── ratelimiter/                       # 限流模块
│   │   ├── annotation/                    # 限流注解
│   │   └── aspect/                        # 限流切面
│   ├── service/                           # 服务层
│   │   ├── UserService.java               # 用户服务
│   │   ├── AppService.java                # 应用服务
│   │   ├── ChatHistoryService.java        # 聊天历史服务
│   │   └── impl/                          # 服务实现
│   └── utils/                             # 工具类
├── src/main/resources/
│   ├── mapper/                            # MyBatis XML
│   ├── prompt/                            # AI Prompt 模板
│   ├── application.yml                    # 主配置文件
│   ├── application-local.yml              # 本地环境配置（需自行创建）
│   └── application-prod.yml               # 生产环境配置（需自行创建）
├── ngs-ai-code-mother-frontend/           # 前端项目
│   └── ngs-ai-code-mother-frontend/
│       ├── src/
│       │   ├── api/                       # API 接口
│       │   ├── components/                # 组件
│       │   ├── pages/                     # 页面
│       │   ├── stores/                    # 状态管理
│       │   └── utils/                     # 工具函数
│       └── ...
├── sql/                                   # 数据库脚本
│   └── create_table.sql                   # 建表语句
└── tmp/                                   # 临时目录（代码生成/部署）
    ├── code_output/                       # 代码生成目录
    └── code_deploy/                       # 代码部署目录
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+ (前端开发)
- 阿里云 DashScope API Key（用于 AI 模型调用）

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/Ngs-liuji/ngs-ai-code-mother.git
cd ngs-ai-code-mother
```

#### 2. 创建数据库

```sql
CREATE DATABASE ngs_ai_code_mother DEFAULT CHARACTER SET utf8mb4;
```

#### 3. 导入数据表

```bash
mysql -u root -p ngs_ai_code_mother < sql/create_table.sql
```

#### 4. 配置环境变量

项目使用环境变量管理敏感配置，请设置以下环境变量：

```bash
# 阿里云 DashScope API Key
export DASHSCOPE_API_KEY=your_api_key_here

# 腾讯云 COS 配置（可选，用于图片上传）
export TENCENT_SECRET_ID=your_secret_id
export TENCENT_SECRET_KEY=your_secret_key
```

#### 5. 创建本地配置文件

复制 `src/main/resources/application.yml` 作为基础，创建 `application-local.yml`（该文件已在 `.gitignore` 中，不会被提交）：

```yaml
# langchain4j 相关模型配置
langchain4j:
  openai:
    chat-model:
      model-name: "qwen3-vl-flash-2026-01-22"
      api-key: ${DASHSCOPE_API_KEY}
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      max-tokens: 8192
      logRequests: true
      logResponses: true
    
    streaming-chat-model:
      model-name: "qwen3.5-plus-2026-02-15"
      api-key: ${DASHSCOPE_API_KEY}
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      max-tokens: 32768
      temperature: 0.1

# COS 对象存储配置
cos:
  client:
    host: https://your-domain.com
    secretId: ${TENCENT_SECRET_ID}
    secretKey: ${TENCENT_SECRET_KEY}
    region: ap-beijing
    bucket: your-bucket-name
```

修改数据库和 Redis 配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ngs_ai_code_mother?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

#### 6. 启动后端

```bash
mvn spring-boot:run
```

访问 API 文档：http://localhost:8123/api/doc.html

#### 7. 启动前端

```bash
cd ngs-ai-code-mother-frontend/ngs-ai-code-mother-frontend
npm install
npm run dev
```

访问前端页面：http://localhost:5173

## 📋 功能模块

### ✅ 已完成

#### 1. 用户管理模块
- [x] **用户注册** - 支持账号密码注册，包含参数校验
- [x] **用户登录** - Session 方式登录，返回脱敏后的用户信息
- [x] **用户注销** - 退出登录，清除 Session
- [x] **获取当前用户** - 获取登录用户的详细信息
- [x] **用户 CRUD** - 管理员可创建、查询、更新、删除用户
- [x] **分页查询** - 支持按条件分页获取用户列表（仅管理员）
- [x] **数据脱敏** - 用户敏感信息自动脱敏处理

#### 2. 应用管理模块
- [x] **创建应用** - 基于初始提示词创建应用，AI 智能识别代码生成类型
- [x] **我的应用** - 分页查询当前用户创建的应用
- [x] **精选应用** - 分页查询精选应用列表（支持缓存）
- [x] **应用详情** - 查看应用详细信息
- [x] **应用更新** - 用户可更新应用名称，管理员可更新所有字段
- [x] **应用删除** - 用户可删除自己的应用，管理员可删除任意应用
- [x] **管理员后台** - 管理员可查看所有应用并进行管理

#### 3. AI 代码生成模块
- [x] **HTML 代码生成** - 生成单页面 HTML 代码
- [x] **多文件代码生成** - 生成包含 HTML/CSS/JS 的多文件项目
- [x] **Vue 项目生成** - 生成完整的 Vue 3 项目（含构建）
- [x] **智能路由** - AI 自动识别用户需求，选择合适的生成类型
- [x] **流式输出** - SSE 流式响应，实时展示生成过程
- [x] **上下文对话** - 支持多轮对话，基于历史对话持续优化
- [x] **聊天记忆** - 对话历史持久化到 MySQL，支持游标分页查询
- [x] **代码保存** - 自动将生成的代码保存到文件系统

#### 4. 应用部署模块
- [x] **一键部署** - 将生成的应用部署到服务器
- [x] **Vue 项目构建** - 自动执行 npm install 和 npm run build
- [x] **部署预览** - 通过静态资源服务在线预览部署的应用
- [x] **自动截图** - 部署后使用 Selenium 自动生成应用封面
- [x] **部署标识** - 每个应用生成唯一的 deployKey

#### 5. 代码下载模块
- [x] **ZIP 打包** - 将生成的项目代码打包为 ZIP 文件
- [x] **权限校验** - 仅应用创建者可下载代码
- [x] **多种格式** - 支持 HTML、多文件、Vue 项目下载

#### 6. 聊天历史模块
- [x] **历史记录** - 保存用户与 AI 的对话历史
- [x] **游标分页** - 基于 createTime 的游标分页查询
- [x] **消息分段** - 支持长消息分段存储
- [x] **历史加载** - 对话时自动加载历史上下文

#### 7. 系统功能模块
- [x] **统一响应** - BaseResponse 通用响应类
- [x] **全局异常** - GlobalExceptionHandler 统一异常处理
- [x] **错误码** - ErrorCode 统一错误码定义
- [x] **跨域配置** - 全局 CORS 配置
- [x] **权限注解** - @AuthCheck 注解鉴权
- [x] **限流注解** - @RateLimit 接口限流
- [x] **逻辑删除** - MyBatis Plus 全局逻辑删除
- [x] **雪花算法** - ASSIGN_ID 生成主键
- [x] **本地缓存** - Caffeine 缓存应用列表
- [x] **分布式 Session** - Redis 存储 Session

### 🚧 开发中

- [ ] 现在生成网页使用占位图片，后期将引入工作流，使用ai查找相关图片替换
- [ ] 代码质量检查
- [ ] 版本管理
- [ ] 团队协作功能
- [ ] 更多 AI 辅助功能

## 🔑 核心架构设计

### AI 代码生成流程

```
用户输入需求
    ↓
智能路由识别类型（HTML/Multi-file/Vue）
    ↓
创建应用记录
    ↓
加载历史对话上下文
    ↓
调用 AI 模型生成代码（流式）
    ↓
解析并保存代码到文件系统
    ↓
记录对话历史到数据库
    ↓
返回给用户（可预览/部署/下载）
```

### 缓存策略

- **Caffeine 本地缓存**: 精选应用列表（减少数据库查询）
- **Redis 分布式缓存**: 
  - Session 存储
  - 聊天记忆（MessageWindowChatMemory）
- **MySQL 持久化**: 聊天历史、应用信息、用户数据

### 并发处理

- **StreamingChatModel 多例模式**: 解决 LangChain4j 并发问题
- **响应式编程**: Reactor 处理流式输出
- **异步截图**: 部署后异步生成应用封面

## 📝 API 文档

启动项目后访问：http://localhost:8123/api/doc.html

主要接口：

| 模块 | 接口路径 | 说明 |
|------|---------|------|
| 用户 | `/api/user/*` | 注册、登录、注销、CRUD |
| 应用 | `/api/app/*` | 创建、查询、更新、删除、部署、下载 |
| 聊天 | `/api/app/chat/gen/code` | AI 代码生成（SSE 流式） |
| 历史 | `/api/chatHistory/*` | 查询对话历史 |
| 静态资源 | `/api/static/*` | 访问部署的应用 |

## 🔒 安全说明

### 敏感信息管理

项目已将敏感配置文件加入 `.gitignore`，包括：
- `application-local.yml` - 本地环境配置
- `application-prod.yml` - 生产环境配置

**请勿将这些文件提交到版本控制系统！**

建议使用以下方式管理敏感信息：
1. 环境变量（推荐）
2. 配置中心（如 Nacos、Apollo）
3. 密钥管理服务（如 AWS Secrets Manager）

### 权限控制

- **普通用户**: 只能操作自己创建的应用
- **管理员**: 可管理所有用户和应用
- **接口限流**: AI 对话接口限制每分钟 5 次请求

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 开源协议

Apache License 2.0

## 👨‍💻 作者

**NGS_liuji**

GitHub: [@Ngs-liuji](https://github.com/Ngs-liuji)

## 📬 联系方式

如有问题，请通过 Issue 联系。

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [LangChain4j](https://github.com/langchain4j/langchain4j)
- [MyBatis Plus](https://baomidou.com/)
- [Vue.js](https://vuejs.org/)
- [Ant Design Vue](https://antdv.com/)

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐️ Star 支持！**

Made with ❤️ by NGS_liuji

</div>
