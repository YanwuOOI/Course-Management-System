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

## 本地部署指南

### 环境准备

#### 1. 安装 JDK
- 版本要求：JDK 17 或以上
- 下载地址：[Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 或 [OpenJDK](https://adoptium.net/)
- 配置环境变量：
  - Windows：设置 `JAVA_HOME` 环境变量指向 JDK 安装目录
  - Linux/Mac：在 `~/.bashrc` 或 `~/.zshrc` 中添加 `export JAVA_HOME=你的JDK路径`

#### 2. 安装 Maven
- 版本要求：Maven 3.6 或以上
- 下载地址：[Apache Maven](https://maven.apache.org/download.cgi)
- 配置环境变量：将 Maven 的 `bin` 目录添加到 `PATH`

#### 3. 安装 Node.js
- 版本要求：Node.js 16 或以上
- 下载地址：[Node.js](https://nodejs.org/)

#### 4. 安装 MySQL
- 版本要求：MySQL 8.0 或以上
- 下载地址：[MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- 安装后记住用户名和密码（默认用户名为 `root`）

#### 5. 安装 Navicat
- 下载地址：[Navicat for MySQL](https://www.navicat.com.cn/products/navicat-for-mysql)
- 用于可视化管理 MySQL 数据库

### 数据库初始化（使用 Navicat）

#### 1. 连接 MySQL 数据库
1. 打开 Navicat，点击「连接」->「MySQL」
2. 填写连接信息：
   - 连接名：任意（如 `CMS_DB`）
   - 主机名或 IP 地址：`localhost`
   - 端口：`3306`
   - 用户名：`root`
   - 密码：你的 MySQL 密码
3. 点击「测试连接」，连接成功后点击「确定」

#### 2. 创建数据库
1. 展开左侧连接列表，右键点击刚创建的连接，选择「新建数据库」
2. 填写数据库信息：
   - 数据库名：`cms_db`
   - 字符集：`utf8mb4`
   - 排序规则：`utf8mb4_unicode_ci`
3. 点击「确定」

#### 3. 执行数据库初始化脚本
1. 双击打开 `cms_db` 数据库
2. 点击「查询」->「新建查询」
3. 在查询编辑器中，点击「打开文件」，选择 `database/cms_db.sql`
4. 点击「运行」按钮执行脚本，初始化基础表结构

#### 4. 执行成绩异议表脚本
1. 再次点击「查询」->「新建查询」
2. 在查询编辑器中，点击「打开文件」，选择 `database/create_score_dispute_table.sql`
3. 点击「运行」按钮执行脚本，创建成绩异议表

### 后端部署

#### 1. 配置数据库连接
1. 打开 `backend/src/main/resources/application.properties` 文件
2. 检查并修改数据库连接配置（默认配置如下，如需修改请根据实际情况调整）：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cms_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=123456
   ```

#### 2. 启动后端服务
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

- 后端服务默认运行在 `http://localhost:8081/api`
- 查看日志输出，确认服务启动成功

### 前端部署

#### 1. 安装依赖
```bash
cd frontend
npm install
```

#### 2. 启动前端服务
```bash
npm run serve
```

- 前端服务默认运行在 `http://localhost:8080`
- 启动成功后，会在控制台显示访问地址

### 访问应用

1. 打开浏览器，访问 `http://localhost:8080`
2. 使用以下默认账号登录（根据角色选择）：
   - 管理员：用户名 `admin`，密码 `123456`
   - 教师：用户名 `teacher1`，密码 `123456`
   - 学生：用户名 `student1`，密码 `123456`

## 开发说明

### 后端项目结构
- `controller/`：控制器层，处理 HTTP 请求
- `service/`：业务逻辑层
- `dao/`：数据访问层
- `entity/`：实体类
- `dto/`：数据传输对象
- `security/`：安全认证相关
- `utils/`：工具类

### 前端项目结构
- `components/`：Vue 组件
- `views/`：页面视图
- `api/`：API 调用
- `router/`：路由配置

### 常用命令

#### 后端
- 编译打包：`mvn clean package`
- 运行测试：`mvn test`

#### 前端
- 开发模式：`npm run serve`
- 构建生产版本：`npm run build`
- 代码检查：`npm run lint`



## 注意事项

1. 确保 MySQL 服务正常运行
2. 数据库连接配置正确
3. 后端服务启动成功后再启动前端服务
4. 首次运行前端可能需要等待依赖下载完成
5. 如遇端口冲突，可修改配置文件中的端口号

### 修改端口号

- 后端端口：修改 `backend/src/main/resources/application.properties` 中的 `server.port`
- 前端端口：修改 `frontend/vue.config.js` 中的 `devServer.port`

## 文档

详细的功能说明和开发文档请参考 `文档/` 目录下的相关文档：
- 需求规格文档.md
- 概要设计文档.md
- 详细设计文档.md
- 项目开发计划.md

## 功能特性

- 用户认证与授权（学生、教师、管理员）
- 课程管理
- 选课管理
- 成绩录入与查询
- 成绩统计
- 成绩异议处理

## 开发说明

详细的功能说明和开发文档请参考 `文档/` 目录下的相关文档。


