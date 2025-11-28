package com.cms.dao;

import com.cms.entity.ScoreDispute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成绩异议DAO接口
 * 定义成绩异议表的数据库操作方法
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Mapper
public interface ScoreDisputeDao {
    /**
     * 插入成绩异议记录
     * @param scoreDispute 成绩异议实体
     * @return 影响行数
     */
    int insertScoreDispute(ScoreDispute scoreDispute);
    
    /**
     * 更新成绩异议记录
     * @param scoreDispute 成绩异议实体
     * @return 影响行数
     */
    int updateScoreDispute(ScoreDispute scoreDispute);
    
    /**
     * 根据异议ID删除成绩异议记录
     * @param disputeId 异议ID
     * @return 影响行数
     */
    int deleteScoreDisputeById(@Param("disputeId") Integer disputeId);
    
    /**
     * 根据异议ID查询成绩异议记录
     * @param disputeId 异议ID
     * @return 成绩异议实体
     */
    ScoreDispute selectScoreDisputeById(@Param("disputeId") Integer disputeId);
    
    /**
     * 根据学号查询成绩异议列表
     * @param studentId 学号
     * @return 成绩异议列表
     */
    List<ScoreDispute> selectScoreDisputesByStudentId(@Param("studentId") String studentId);
    
    /**
     * 根据课程号查询成绩异议列表
     * @param courseId 课程号
     * @return 成绩异议列表
     */
    List<ScoreDispute> selectScoreDisputesByCourseId(@Param("courseId") String courseId);
    
    /**
     * 根据教师ID查询成绩异议列表
     * @param teacherId 教师ID
     * @return 成绩异议列表
     */
    List<ScoreDispute> selectScoreDisputesByTeacherId(@Param("teacherId") String teacherId);
    
    /**
     * 查询所有成绩异议列表
     * @return 成绩异议列表
     */
    List<ScoreDispute> selectAllScoreDisputes();
    
    /**
     * 根据审核状态查询成绩异议列表
     * @param auditStatus 审核状态
     * @return 成绩异议列表
     */
    List<ScoreDispute> selectScoreDisputesByAuditStatus(@Param("auditStatus") String auditStatus);
}