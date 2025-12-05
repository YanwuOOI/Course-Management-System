-- 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS cms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE cms_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    user_id VARCHAR(10) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(10) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(64) NOT NULL COMMENT '密码（SHA-256加密）',
    role ENUM('学生', '教师', '管理员') NOT NULL COMMENT '角色',
    name VARCHAR(10) NOT NULL COMMENT '姓名',
    dept VARCHAR(20) NOT NULL COMMENT '院系',
    phone VARCHAR(11) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    status TINYINT DEFAULT 1 COMMENT '状态（1：正常，0：禁用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建课程表
CREATE TABLE IF NOT EXISTS course (
    course_id VARCHAR(8) PRIMARY KEY COMMENT '课程号',
    course_name VARCHAR(50) NOT NULL COMMENT '课程名',
    credit INT NOT NULL COMMENT '学分（1-5）',
    course_type ENUM('必修课', '选修课', '通识课') NOT NULL COMMENT '课程类型',
    teacher_id VARCHAR(10) NOT NULL COMMENT '授课教师ID',
    class_time VARCHAR(30) NOT NULL COMMENT '上课时间',
    class_location VARCHAR(20) NOT NULL COMMENT '上课地点',
    capacity INT NOT NULL COMMENT '容量（10-100）',
    selected_num INT DEFAULT 0 COMMENT '已选人数',
    intro TEXT DEFAULT NULL COMMENT '简介',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (teacher_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 创建选课记录表
CREATE TABLE IF NOT EXISTS course_selection (
    selection_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '选课记录ID',
    student_id VARCHAR(10) NOT NULL COMMENT '学号',
    course_id VARCHAR(8) NOT NULL COMMENT '课程号',
    selection_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status ENUM('成功', '失败') NOT NULL COMMENT '状态',
    fail_reason VARCHAR(100) DEFAULT NULL COMMENT '失败原因',
    FOREIGN KEY (student_id) REFERENCES user(user_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    UNIQUE KEY uk_student_course (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- 创建成绩表
CREATE TABLE IF NOT EXISTS score (
    score_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '成绩ID',
    student_id VARCHAR(10) NOT NULL COMMENT '学号',
    course_id VARCHAR(8) NOT NULL COMMENT '课程号',
    score DECIMAL(4,1) DEFAULT NULL COMMENT '成绩（0-100，保留1位小数）',
    grade VARCHAR(10) DEFAULT NULL COMMENT '成绩等级',
    input_teacher_id VARCHAR(10) NOT NULL COMMENT '录入教师ID',
    input_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status ENUM('未提交', '已提交', '已锁定') DEFAULT '未提交' COMMENT '状态',
    FOREIGN KEY (student_id) REFERENCES user(user_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (input_teacher_id) REFERENCES user(user_id),
    UNIQUE KEY uk_student_course_score (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 创建操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id VARCHAR(10) NOT NULL COMMENT '操作人ID',
    user_name VARCHAR(10) NOT NULL COMMENT '操作人姓名',
    operation_type VARCHAR(20) NOT NULL COMMENT '操作类型',
    operation_content TEXT NOT NULL COMMENT '操作内容',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    ip_address VARCHAR(15) NOT NULL COMMENT 'IP地址',
    result ENUM('成功', '失败') NOT NULL COMMENT '操作结果',
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 创建特殊申请记录表
CREATE TABLE IF NOT EXISTS special_application (
    apply_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '申请ID',
    student_id VARCHAR(10) NOT NULL COMMENT '学号',
    course_id VARCHAR(8) NOT NULL COMMENT '课程号',
    apply_type ENUM('特殊退课', '补选') NOT NULL COMMENT '申请类型',
    apply_reason TEXT NOT NULL COMMENT '申请理由',
    attachment VARCHAR(100) DEFAULT NULL COMMENT '附件路径',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    audit_status ENUM('待审核', '通过', '驳回') DEFAULT '待审核' COMMENT '审核状态',
    audit_opinion TEXT DEFAULT NULL COMMENT '审核意见',
    audit_user_id VARCHAR(10) DEFAULT NULL COMMENT '审核人ID',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    FOREIGN KEY (student_id) REFERENCES user(user_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (audit_user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='特殊申请记录表';

-- 创建成绩异议表
CREATE TABLE IF NOT EXISTS score_dispute (
    dispute_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '异议ID',
    student_id VARCHAR(10) NOT NULL COMMENT '学号',
    course_id VARCHAR(8) NOT NULL COMMENT '课程号',
    reason TEXT NOT NULL COMMENT '异议理由',
    attachment VARCHAR(100) DEFAULT NULL COMMENT '附件路径',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    audit_status ENUM('待审核', '通过', '驳回') DEFAULT '待审核' COMMENT '审核状态',
    audit_opinion TEXT DEFAULT NULL COMMENT '审核意见',
    audit_user_id VARCHAR(10) DEFAULT NULL COMMENT '审核人ID',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    FOREIGN KEY (student_id) REFERENCES user(user_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (audit_user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩异议表';

-- 插入初始数据
-- 插入管理员用户
INSERT INTO user (user_id, username, password, role, name, dept) VALUES 
('A001', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '管理员', '管理员', '软件学院');

-- 插入教师用户
INSERT INTO user (user_id, username, password, role, name, dept) VALUES 
('T001', 'T001', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '教师', '张老师', '软件学院'),
('T002', 'T002', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '教师', '李老师', '计算机学院');

-- 插入学生用户
INSERT INTO user (user_id, username, password, role, name, dept) VALUES 
('2024001', '2024001', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '学生', '张三', '软件学院'),
('2024002', '2024002', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '学生', '李四', '软件学院'),
('2024003', '2024003', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '学生', '王五', '计算机学院');

-- 插入课程数据
INSERT INTO course (course_id, course_name, credit, course_type, teacher_id, class_time, class_location, capacity) VALUES 
('CS202501', '软件工程导论', 3, '必修课', 'T001', '周一 1-2节', '教学楼A301', 50),
('CS202502', '数据结构', 4, '必修课', 'T001', '周三 3-4节', '教学楼A301', 50),
('CS202503', '人工智能', 3, '选修课', 'T002', '周二 5-6节', '实验楼B205', 40),
('CS202504', '机器学习', 3, '选修课', 'T002', '周四 7-8节', '实验楼B205', 40);

-- 插入选课记录
INSERT INTO course_selection (student_id, course_id, status) VALUES 
('2024001', 'CS202501', '成功'),
('2024001', 'CS202502', '成功'),
('2024001', 'CS202503', '成功'),
('2024002', 'CS202501', '成功'),
('2024002', 'CS202502', '成功'),
('2024003', 'CS202503', '成功'),
('2024003', 'CS202504', '成功');

-- 插入成绩数据
INSERT INTO score (student_id, course_id, score, grade, input_teacher_id, status) VALUES 
('2024001', 'CS202501', 85.5, '良好', 'T001', '已提交'),
('2024001', 'CS202502', 92.0, '优秀', 'T001', '已提交'),
('2024002', 'CS202501', 78.0, '中等', 'T001', '已提交'),
('2024002', 'CS202502', 65.5, '及格', 'T001', '已提交'),
('2024003', 'CS202503', 88.0, '良好', 'T002', '已提交'),
('2024003', 'CS202504', 95.0, '优秀', 'T002', '已提交');
