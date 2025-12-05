# 课程管理系统 (CMS)

一个基于 Spring Boot + Vue 3 的课程管理系统，支持学生、教师和管理员三种角色的成绩管理功能。

## 项目结构

```
软件工程实验/
├── backend/              # Spring Boot 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/     # Java 源代码
│   │   │   └── resources/ # 配置文件
│   │   └── test/         # 测试代码
│   └── pom.xml           # Maven 依赖配置
│
├── frontend/             # Vue 3 前端项目
│   ├── src/              # Vue 源代码
│   ├── public/           # 静态资源
│   ├── dist/             # 构建输出目录
│   └── package.json      # npm 依赖配置
│
├── database/             # 数据库脚本
│   ├── cms_db.sql        # 数据库初始化脚本
│   └── create_score_dispute_table.sql  # 成绩异议表创建脚本
│
└── 文档/                 # 项目文档
    ├── 需求规格文档.md
    ├── 概要设计文档.md
    ├── 详细设计文档.md
    └── 项目开发计划.md
```

## 技术栈

### 后端
- Spring Boot
- MyBatis
- MySQL
- JWT 认证

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios
- ECharts

## 快速开始

### 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8081`

### 前端启动

```bash
cd frontend
npm install
npm run serve
```

前端服务默认运行在 `http://localhost:8080`

### 数据库初始化

1. 创建 MySQL 数据库
2. 执行 `database/cms_db.sql` 初始化数据库
3. 执行 `database/create_score_dispute_table.sql` 创建成绩异议表（如需要）

## 功能特性

- 用户认证与授权（学生、教师、管理员）
- 课程管理
- 选课管理
- 成绩录入与查询
- 成绩统计
- 成绩异议处理

## 开发说明

详细的功能说明和开发文档请参考 `文档/` 目录下的相关文档。


