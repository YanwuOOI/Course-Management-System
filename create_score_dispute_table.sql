-- 使用数据库
USE cms_db;

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