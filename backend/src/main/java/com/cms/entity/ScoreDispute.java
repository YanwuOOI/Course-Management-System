package com.cms.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成绩异议实体类
 * 对应数据库中的score_dispute表
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Data
public class ScoreDispute {
    /**
     * 异议ID
     */
    private Integer disputeId;
    
    /**
     * 学号
     */
    private String studentId;
    
    /**
     * 课程号
     */
    private String courseId;
    
    /**
     * 异议理由
     */
    private String reason;
    
    /**
     * 附件路径
     */
    private String attachment;
    
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    
    /**
     * 审核状态
     */
    private String auditStatus;
    
    /**
     * 审核意见
     */
    private String auditOpinion;
    
    /**
     * 审核人ID
     */
    private String auditUserId;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
}