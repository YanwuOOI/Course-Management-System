package com.cms.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成绩实体类
 * 对应数据库中的score表
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Data
public class Score {
    /**
     * 成绩ID
     */
    private Integer scoreId;
    
    /**
     * 学号
     */
    private String studentId;
    
    /**
     * 课程号
     */
    private String courseId;
    
    /**
     * 成绩（0-100，保留1位小数）
     */
    private Double score;
    
    /**
     * 成绩等级
     */
    private String grade;
    
    /**
     * 录入教师ID
     */
    private String inputTeacherId;
    
    /**
     * 录入时间
     */
    private LocalDateTime inputTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 状态（未提交/已提交/已锁定）
     */
    private String status;
}