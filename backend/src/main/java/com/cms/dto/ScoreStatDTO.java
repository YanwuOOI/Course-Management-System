package com.cms.dto;

import lombok.Data;

/**
 * 成绩统计DTO
 * 用于课程成绩统计数据传输
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Data
public class ScoreStatDTO {
    /**
     * 课程号
     */
    private String courseId;
    
    /**
     * 课程名
     */
    private String courseName;
    
    /**
     * 总人数
     */
    private Integer totalStudents;
    
    /**
     * 平均分
     */
    private Double averageScore;
    
    /**
     * 最高分
     */
    private Double highestScore;
    
    /**
     * 最低分
     */
    private Double lowestScore;
    
    /**
     * 及格率
     */
    private Double passRate;
    
    /**
     * 优秀人数（90-100分）
     */
    private Integer excellentCount;
    
    /**
     * 良好人数（80-89分）
     */
    private Integer goodCount;
    
    /**
     * 中等人数（70-79分）
     */
    private Integer mediumCount;
    
    /**
     * 及格人数（60-69分）
     */
    private Integer passCount;
    
    /**
     * 不及格人数（<60分）
     */
    private Integer failCount;
}