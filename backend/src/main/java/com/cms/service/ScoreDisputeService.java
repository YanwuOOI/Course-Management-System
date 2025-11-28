package com.cms.service;

import com.cms.entity.ScoreDispute;
import com.cms.dto.ScoreDisputeDTO;

import java.util.List;

/**
 * 成绩异议Service接口
 * 定义成绩异议处理的业务逻辑方法
 * @author 许祖嘉
 * @date 2025-11-28
 */
public interface ScoreDisputeService {
    /**
     * 提交成绩异议
     * @param scoreDisputeDTO 成绩异议DTO
     * @return 成绩异议实体
     */
    ScoreDispute submitDispute(ScoreDisputeDTO scoreDisputeDTO);
    
    /**
     * 审核成绩异议
     * @param scoreDisputeDTO 成绩异议DTO
     * @return 成绩异议实体
     */
    ScoreDispute auditDispute(ScoreDisputeDTO scoreDisputeDTO);
    
    /**
     * 删除成绩异议
     * @param disputeId 异议ID
     * @return 是否成功
     */
    boolean deleteDispute(Integer disputeId);
    
    /**
     * 根据异议ID查询成绩异议
     * @param disputeId 异议ID
     * @return 成绩异议实体
     */
    ScoreDispute getDisputeById(Integer disputeId);
    
    /**
     * 根据学号查询成绩异议列表
     * @param studentId 学号
     * @return 成绩异议列表
     */
    List<ScoreDispute> getDisputesByStudentId(String studentId);
    
    /**
     * 根据课程号查询成绩异议列表
     * @param courseId 课程号
     * @return 成绩异议列表
     */
    List<ScoreDispute> getDisputesByCourseId(String courseId);
    
    /**
     * 根据教师ID查询成绩异议列表
     * @param teacherId 教师ID
     * @return 成绩异议列表
     */
    List<ScoreDispute> getDisputesByTeacherId(String teacherId);
    
    /**
     * 查询所有成绩异议列表
     * @return 成绩异议列表
     */
    List<ScoreDispute> getAllDisputes();
    
    /**
     * 根据审核状态查询成绩异议列表
     * @param auditStatus 审核状态
     * @return 成绩异议列表
     */
    List<ScoreDispute> getDisputesByAuditStatus(String auditStatus);
}