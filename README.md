# 🎓 毕业记录管理系统 (Graduation Records Management System)

基于Java LinkedList数据结构的毕业记录管理系统，使用Spring Boot和H2数据库构建的现代化Web应用。

## 📋 项目概述

这是一个完整的毕业记录管理系统，实现了学生信息管理、课程管理、成绩记录和统计分析等功能。系统采用Java的LinkedList数据结构作为核心数据存储，同时提供了RESTful API和现代化的Web界面。

## 🎯 教学活动要求

### Activity 1: 毕业记录管理系统 (Portfolio Task)
基于Java的LinkedList数据结构实现毕业记录管理系统，包含以下核心要求：

#### **Student类要求**
- ✅ 存储学生ID、成绩列表和平均成绩
- ✅ 实现`toString()`方法提供人类友好的表示
- ✅ 实现`Comparable`接口支持总排序（按平均成绩降序）

#### **Course类要求**
- ✅ 存储课程ID、学年和学生列表
- ✅ 允许学生插入到学生列表（添加新学生）
- ✅ 允许学生从学生列表移除（删除现有学生）
- ✅ 保持学生列表按平均成绩降序排序
- ✅ 允许更新学生成绩（成绩变化反映在排序中）
- ✅ 每次更改后打印学生列表

#### **GraduationRecords类要求**
- ✅ 包含main函数驱动事件
- ✅ 创建课程实例
- ✅ 随后插入、更新、移除学生

#### **演示方式**
运行 `start2.bat` 启动控制台演示，完整展示Activity 1的所有要求。

### Activity 3: 数据结构实现 (Optional Task)
实现自定义的链表数据结构，使用String类而不是泛型：

#### **SinglyLinkedList (单链表)**
- ✅ 使用String类存储节点数据
- ✅ 实现基本的链表操作（添加、删除、查找等）
- ✅ 包含详细的时间复杂度注释

#### **DoublyLinkedList (双链表)**
- ✅ 使用String类存储节点数据
- ✅ 实现双向链表的所有操作
- ✅ 优化查找算法（根据索引位置选择遍历方向）

## ✨ 主要功能

### 学生管理
- ✅ 添加新学生
- ✅ 查看学生列表（按平均成绩降序排列）
- ✅ 查看学生成绩详情和统计信息
- ✅ 删除学生记录

### 课程管理
- ✅ 创建新课程
- ✅ 管理课程信息（课程ID、学年）
- ✅ 将学生添加到课程
- ✅ 从课程移除学生
- ✅ 更新学生成绩（自动重新排序）

### 成绩管理
- ✅ 为学生录入成绩
- ✅ 实时计算平均成绩
- ✅ 成绩统计分析（最高分、最低分、平均分等）
- ✅ 按课程查看成绩

### 系统特性
- ✅ 自动排序（按平均成绩降序）
- ✅ 实时数据更新
- ✅ 响应式Web界面
- ✅ RESTful API设计
- ✅ 双语界面支持（中文/英文）

## 🛠️ 技术栈

### 后端
- **Java 17** - 编程语言
- **Spring Boot 3.2.0** - Web框架
- **Spring Data JPA** - 数据访问层
- **H2 Database** - 嵌入式数据库
- **Maven** - 项目管理工具

### 前端
- **HTML5/CSS3** - 页面结构和样式
- **JavaScript (ES6+)** - 前端交互
- **Thymeleaf** - 模板引擎

### 数据结构
- **LinkedList** - 核心数据存储
- **单链表 (SinglyLinkedList)** - 自定义实现
- **双链表 (DoublyLinkedList)** - 自定义实现

## 🚀 快速开始

### 环境要求
- **JDK 17+** - Java开发环境
- **Maven 3.9+** - 项目构建工具
- **现代浏览器** - 支持ES6+的浏览器

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/Frisk239/practical12.git
   cd practical12
   ```

2. **选择启动方式**

   #### **方式一：完整Web应用（推荐）**
   - 双击运行 `start.bat` 文件
   - 启动完整的Spring Boot Web应用
   - 包含数据库、REST API和现代化Web界面
   - 访问地址：http://localhost:8080

   #### **方式二：控制台演示（Activity 1要求）**
   - 双击运行 `start2.bat` 文件
   - 运行纯控制台应用，演示Activity 1的所有要求
   - 展示课程管理、学生排序、成绩更新等核心功能
   - 在命令行窗口显示所有操作结果

   #### **方式三：手动启动**
   ```bash
   # 设置Maven环境（Windows）
   set MAVEN_HOME=apache-maven-3.9.11
   set PATH=%MAVEN_HOME%\bin;%PATH%

   # 启动完整Web应用
   mvn spring-boot:run

   # 或者运行控制台演示
   mvn compile -q
   java -cp "target/classes" com.graduation.GraduationRecords
   ```

3. **访问系统**
   - **Web应用**：打开浏览器访问 http://localhost:8080
   - **控制台演示**：直接查看命令行输出

## 📁 项目结构

```
practical12/
├── src/main/java/com/graduation/
│   ├── controller/           # REST控制器
│   ├── service/             # 业务逻辑层
│   ├── repository/          # 数据访问层
│   ├── entity/              # 实体类
│   └── datastructures/      # 数据结构实现
├── src/main/resources/
│   ├── static/              # 静态资源
│   │   ├── css/            # 样式文件
│   │   └── js/             # JavaScript文件
│   ├── templates/           # Thymeleaf模板
│   ├── application.properties # 应用配置
│   ├── schema.sql          # 数据库初始化脚本
│   └── data.sql            # 初始数据
├── apache-maven-3.9.11/     # 本地Maven环境
├── start.bat               # Windows启动脚本
├── pom.xml                 # Maven项目配置
├── .gitignore             # Git忽略文件
└── README.md              # 项目说明
```

## 🔧 配置说明

### 数据库配置
系统使用H2嵌入式数据库，默认配置如下：
- **数据库URL**: `jdbc:h2:./graduation`
- **用户名**: `sa`
- **密码**: (空)

### 端口配置
- **服务器端口**: 8080
- **上下文路径**: /

## 📊 数据结构实现

### 单链表 (SinglyLinkedList)
```java
public class SinglyLinkedList<T> {
    private Node<T> head;
    private int size;

    // 基本操作：添加、删除、查找等
}
```

### 双链表 (DoublyLinkedList)
```java
public class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    // 基本操作：添加、删除、查找等
}
```

## 🎯 核心业务逻辑

### 学生排序算法
学生列表按平均成绩降序排列，实现逻辑：
```java
// 成绩更新时自动重新排序
public void updateStudentGrades(String courseId, String studentId, List<Double> grades) {
    // 1. 更新成绩
    // 2. 重新计算平均分
    // 3. 重新排序学生列表
}
```

### 成绩统计
提供详细的成绩统计信息：
- 成绩数量
- 平均分
- 最高分
- 最低分
- 各科成绩分布

## 🌟 特色功能

### 实时排序
- 成绩更新后自动重新排序
- 保持列表按平均成绩降序排列
- 实时反映最新数据

### 双语支持
- 中文界面为主
- 重要提示信息提供英文对照
- 支持国际化扩展

### 响应式设计
- 适配不同屏幕尺寸
- 移动端友好界面
- 现代化UI设计

## 🔍 API接口

### 学生相关
- `GET /api/students` - 获取所有学生
- `POST /api/students` - 创建新学生
- `DELETE /api/students/{id}` - 删除学生
- `GET /api/students/{id}/grades` - 获取学生成绩

### 课程相关
- `GET /api/courses` - 获取所有课程
- `POST /api/courses` - 创建新课程
- `DELETE /api/courses/{id}` - 删除课程
- `POST /api/courses/{courseId}/students/{studentId}` - 添加学生到课程
- `PUT /api/courses/{courseId}/students/{studentId}/grades` - 更新学生成绩

## 🧪 测试

运行单元测试：
```bash
mvn test
```

## 📝 开发说明

### 构建项目
```bash
mvn clean compile
```

### 打包应用
```bash
mvn clean package
```

### 运行打包的应用
```bash
java -jar target/practical12-1.0.0.jar
```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👥 作者

- **开发者**: Practical12 Team
- **项目地址**: https://github.com/Frisk239/practical12

## 🙏 致谢

- Spring Boot 框架
- H2 数据库
- Maven 项目管理工具
- 所有开源贡献者

---

**注意**: 本项目为教学实践项目，主要用于演示数据结构和Web开发技术。
