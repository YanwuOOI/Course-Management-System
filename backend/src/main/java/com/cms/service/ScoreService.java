package com.cms.service;

import com.cms.entity.Score;
import com.cms.dto.ScoreDTO;
import com.cms.dto.ScoreStatDTO;
import com.cms.dto.PageResult;

import java.util.List;

/**
 * 成绩Service接口
 * 定义成绩管理的业务逻辑方法
 * @author 许祖嘉
 * @date 2025-11-28
 */
public interface ScoreService {
    /**
     * 录入成绩
     * @param scoreDTO 成绩DTO
     * @return 成绩实体
     */
    Score inputScore(ScoreDTO scoreDTO);
    
    /**
     * 批量录入成绩
     * @param scoreDTOs 成绩DTO列表
     * @return 成功录入的数量
     */
    int batchInputScores(List<ScoreDTO> scoreDTOs);
    
    /**
     * 修改成绩
     * @param scoreDTO 成绩DTO
     * @return 成绩实体
     */
    Score updateScore(ScoreDTO scoreDTO);
    
    /**
     * 删除成绩
     * @param scoreId 成绩ID
     * @return 是否成功
     */
    boolean deleteScore(Integer scoreId);
    
    /**
     * 根据成绩ID查询成绩
     * @param scoreId 成绩ID
     * @return 成绩实体
     */
    Score getScoreById(Integer scoreId);
    
    /**
     * 根据学号和课程号查询成绩
     * @param studentId 学号
     * @param courseId 课程号
     * @return 成绩实体
     */
    Score getScoreByStudentAndCourse(String studentId, String courseId);
    
    /**
     * 根据课程号查询成绩列表
     * @param courseId 课程号
     * @return 成绩列表
     */
    List<Score> getScoresByCourseId(String courseId);
    
    /**
     * 根据学号查询成绩列表
     * @param studentId 学号
     * @return 成绩列表
     */
    List<Score> getScoresByStudentId(String studentId);
    
    /**
     * 根据教师ID查询所授课程的成绩列表
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    List<Score> getScoresByTeacherId(String teacherId);
    
    /**
     * 提交成绩
     * @param courseId 课程号
     * @return 是否成功
     */
    boolean submitScores(String courseId);
    
    /**
     * 锁定成绩
     * @param courseId 课程号
     * @return 是否成功
     */
    boolean lockScores(String courseId);
    
    /**
     * 计算成绩等级
     * @param score 成绩
     * @return 成绩等级
     */
    String calculateGrade(Double score);
    
    /**
     * 统计课程成绩
     * @param courseId 课程号
     * @return 成绩统计DTO
     */
    ScoreStatDTO statCourseScores(String courseId);
    
    /**
     * 归档成绩
     * @param semester 学期
     * @return 是否成功
     */
    boolean archiveScores(String semester);
    
    /**
     * 导出课程成绩到Excel
     * @param courseId 课程号
     * @return Excel文件的字节数组
     * @throws Exception 导出异常
     */
    byte[] exportCourseScores(String courseId) throws Exception;
    
    /**
     * 导出所有成绩到Excel
     * @return Excel文件的字节数组
     * @throws Exception 导出异常
     */
    byte[] exportAllScores() throws Exception;
    
    /**
     * 多条件查询成绩（分页）
     * @param studentId 学号
     * @param courseId 课程号
     * @param keyword 关键词（课程名、学生名）
     * @param minScore 最低分
     * @param maxScore 最高分
     * @param status 状态
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Score> searchScores(String studentId, String courseId, String keyword, Double minScore, Double maxScore, String status, int page, int pageSize);
}