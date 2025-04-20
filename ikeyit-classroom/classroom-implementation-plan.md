# 大学课程管理系统（ikeyit-classroom）实施计划

## 1. 项目结构设置

### 1.1 创建项目目录结构
- 在根目录下创建`ikeyit-classroom`文件夹
- 创建以下子模块：
  - ikeyit-classroom-domain：领域模型层
  - ikeyit-classroom-application：应用服务层
  - ikeyit-classroom-infrastructure：基础设施层
  - ikeyit-classroom-interfaces-api：REST API接口层
  - ikeyit-classroom-interfaces-allinone：整合所有接口的启动模块
  - 其他必要的接口模块（consumer、job等）

### 1.2 配置构建文件
- 创建主项目的`settings.gradle.kts`和`build.gradle.kts`
- 创建各子模块的`build.gradle.kts`
- 在根项目的`settings.gradle.kts`中添加`includeBuild("ikeyit-classroom")`
- 设置`gradle.properties`，指定版本为`1.0.0-SNAPSHOT`和组为`com.ikeyit`

## 2. 领域模型设计

### 2.1 核心聚合根
- **Course**（课程）：课程聚合根
  - 属性：id, name, description, imageUrl, credits, teacherId, startDate, endDate, status等
  - 方法：createCourse, updateCourse, publishCourse, cancelCourse等
  - 状态：DRAFT, PUBLISHED, CANCELLED等

- **Student**（学生）：学生聚合根
  - 属性：id, name, studentId, faculty, className, userId, enrollments等
  - 方法：enrollCourse, dropCourse等

- **Enrollment**（选课记录）：选课聚合根
  - 属性：id, studentId, courseId, enrollDate, status等
  - 方法：approve, reject, complete等
  - 状态：PENDING, APPROVED, REJECTED, COMPLETED等

- **Teacher**（教师）：教师聚合根
  - 属性：id, name, faculty, title, userId等
  - 方法：createCourse, updateCourse, publishCourse等

- **ClassSchedule**（课程安排）：课程安排聚合根
  - 属性：id, courseId, classroom, dayOfWeek, startTime, endTime等
  - 方法：schedule, reschedule, cancel等

### 2.2 领域事件
- CourseCreatedEvent：课程创建事件
- CourseUpdatedEvent：课程更新事件
- CoursePublishedEvent：课程发布事件
- EnrollmentCreatedEvent：选课创建事件
- EnrollmentApprovedEvent：选课批准事件
- ClassScheduleCreatedEvent：课程安排创建事件

### 2.3 仓库接口
- CourseRepository：课程仓库
- StudentRepository：学生仓库
- TeacherRepository：教师仓库
- EnrollmentRepository：选课仓库
- ClassScheduleRepository：课程安排仓库

## 3. 应用服务设计

### 3.1 服务接口
- **CourseService**：课程管理服务
  - 方法：createCourse, updateCourse, publishCourse, findCourseById, findAllCourses等

- **EnrollmentService**：选课服务
  - 方法：enrollCourse, dropCourse, approveEnrollment, rejectEnrollment等

- **StudentService**：学生服务
  - 方法：registerStudent, updateStudent, findStudentById, findStudentByUserId等

- **TeacherService**：教师服务
  - 方法：registerTeacher, updateTeacher, findTeacherById, findTeacherByUserId等

- **ClassScheduleService**：课程安排服务
  - 方法：scheduleClass, rescheduleClass, cancelClass, findSchedulesByCourse等

### 3.2 命令和查询对象
- CreateCourseCMD：创建课程命令
- UpdateCourseCMD：更新课程命令
- EnrollCourseCMD：选课命令
- CourseQRY：课程查询参数
- EnrollmentQRY：选课查询参数

### 3.3 DTO对象
- CourseDTO：课程数据传输对象
- StudentDTO：学生数据传输对象
- TeacherDTO：教师数据传输对象
- EnrollmentDTO：选课数据传输对象
- ClassScheduleDTO：课程安排数据传输对象
- StudentCourseScheduleDTO：学生课程安排数据传输对象（包含下一次上课信息）

## 4. 基础设施层设计

### 4.1 数据库设计
- **course表**：存储课程信息
  - id：BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
  - name：VARCHAR(100) NOT NULL
  - description：TEXT
  - image_url：VARCHAR(255)
  - credits：SMALLINT NOT NULL
  - teacher_id：BIGINT NOT NULL REFERENCES teacher(id)
  - status：SMALLINT NOT NULL（枚举值）
  - created_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - updated_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()

- **student表**：存储学生信息
  - id：BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
  - name：VARCHAR(100) NOT NULL
  - student_id：VARCHAR(50) NOT NULL UNIQUE
  - faculty：VARCHAR(100) NOT NULL
  - class_name：VARCHAR(50) NOT NULL
  - user_id：BIGINT NOT NULL
  - created_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - updated_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()

- **enrollment表**：存储选课信息
  - id：BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
  - student_id：BIGINT NOT NULL REFERENCES student(id)
  - course_id：BIGINT NOT NULL REFERENCES course(id)
  - status：SMALLINT NOT NULL（枚举值）
  - enroll_date：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - created_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - updated_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()

- **teacher表**：存储教师信息
  - id：BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
  - name：VARCHAR(100) NOT NULL
  - faculty：VARCHAR(100) NOT NULL
  - title：VARCHAR(50) NOT NULL
  - user_id：BIGINT NOT NULL
  - created_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - updated_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()

- **class_schedule表**：存储课程安排信息
  - id：BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
  - course_id：BIGINT NOT NULL REFERENCES course(id)
  - classroom：VARCHAR(50) NOT NULL
  - day_of_week：SMALLINT NOT NULL
  - start_time：TIME NOT NULL
  - end_time：TIME NOT NULL
  - created_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
  - updated_at：TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()

### 4.2 仓库实现
- JdbcCourseRepository：课程仓库JDBC实现
- JdbcStudentRepository：学生仓库JDBC实现
- JdbcTeacherRepository：教师仓库JDBC实现
- JdbcEnrollmentRepository：选课仓库JDBC实现
- JdbcClassScheduleRepository：课程安排仓库JDBC实现

## 5. 接口层设计

### 5.1 REST API接口
- **/api/courses**：课程相关API
  - GET /api/courses：获取所有课程
  - POST /api/courses：创建课程（教师权限）
  - GET /api/courses/{id}：获取课程详情
  - PUT /api/courses/{id}：更新课程（教师权限）
  - POST /api/courses/{id}/publish：发布课程（教师权限）

- **/api/students**：学生相关API
  - GET /api/students/{id}：获取学生信息
  - PUT /api/students/{id}：更新学生信息
  - GET /api/students/user/{userId}：根据用户ID获取学生信息
  - POST /api/students/register：注册学生信息（首次登录）

- **/api/enrollments**：选课相关API
  - POST /api/enrollments：学生选课
  - DELETE /api/enrollments/{id}：学生退课
  - GET /api/students/{id}/enrollments：获取学生的选课记录
  - GET /api/courses/{id}/enrollments：获取课程的选课记录（教师权限）

- **/api/teachers**：教师相关API
  - GET /api/teachers/{id}：获取教师信息
  - PUT /api/teachers/{id}：更新教师信息
  - GET /api/teachers/user/{userId}：根据用户ID获取教师信息
  - POST /api/teachers/register：注册教师信息（首次登录）

- **/api/schedules**：课程安排相关API
  - POST /api/schedules：创建课程安排（教师权限）
  - PUT /api/schedules/{id}：更新课程安排（教师权限）
  - GET /api/courses/{id}/schedules：获取课程的安排
  - GET /api/students/{id}/schedules：获取学生的课程安排（包含下一次上课信息）

### 5.2 安全配置
- 基于Spring Security的认证和授权
- 角色：ROLE_TEACHER（教师）、ROLE_USER（学生）
- 权限控制：教师可以创建和管理课程，学生可以选课和查看课程

### 5.3 用户关联流程
- 用户通过认证系统登录后，系统检查是否存在对应的Teacher或Student实体
- 如果不存在，根据用户角色引导用户完善相应信息：
  - 学生用户：引导至学生信息注册页面，填写姓名、学号、学院、班级等信息
  - 教师用户：引导至教师信息注册页面，填写姓名、学院、职称等信息
- 注册完成后，创建对应的Teacher或Student实体，并与用户ID关联
- 后续登录时，系统自动识别用户角色并加载对应实体信息

## 6. 实施步骤

1. 创建项目结构和配置文件
2. 实现领域模型层
3. 实现应用服务层
4. 实现基础设施层（数据库和仓库）
5. 实现REST API接口层
6. 实现安全配置
7. 测试和调试

## 7. 技术栈

- Java 21
- Spring Boot
- Spring Security
- PostgreSQL 15+
- Gradle
- DDD架构