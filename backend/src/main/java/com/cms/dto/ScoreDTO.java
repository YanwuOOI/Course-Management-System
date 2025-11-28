package com.cms.dto;

import lombok.Data;

/**
 * 成绩DTO
 * 用于成绩数据传输
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Data
public class ScoreDTO {
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
     * 录入教师ID
     */
    private String inputTeacherId;
    
    /**
     * 状态（未提交/已提交/已锁定）
     */
    private String status;
}