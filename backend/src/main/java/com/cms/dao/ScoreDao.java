package com.cms.dao;

import com.cms.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成绩DAO接口
 * 定义成绩表的数据库操作方法
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Mapper
public interface ScoreDao {
    /**
     * 插入成绩记录
     * @param score 成绩实体
     * @return 影响行数
     */
    int insertScore(Score score);
    
    /**
     * 更新成绩记录
     * @param score 成绩实体
     * @return 影响行数
     */
    int updateScore(Score score);
    
    /**
     * 根据成绩ID删除成绩记录
     * @param scoreId 成绩ID
     * @return 影响行数
     */
    int deleteScoreById(@Param("scoreId") Integer scoreId);
    
    /**
     * 根据成绩ID查询成绩记录
     * @param scoreId 成绩ID
     * @return 成绩实体
     */
    Score selectScoreById(@Param("scoreId") Integer scoreId);
    
    /**
     * 根据学号和课程号查询成绩记录
     * @param studentId 学号
     * @param courseId 课程号
     * @return 成绩实体
     */
    Score selectScoreByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);
    
    /**
     * 根据课程号查询成绩列表
     * @param courseId 课程号
     * @return 成绩列表
     */
    List<Score> selectScoresByCourseId(@Param("courseId") String courseId);
    
    /**
     * 根据学号查询成绩列表
     * @param studentId 学号
     * @return 成绩列表
     */
    List<Score> selectScoresByStudentId(@Param("studentId") String studentId);
    
    /**
     * 根据教师ID查询所授课程的成绩列表
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    List<Score> selectScoresByTeacherId(@Param("teacherId") String teacherId);
    
    /**
     * 更新成绩状态
     * @param courseId 课程号
     * @param status 状态
     * @return 影响行数
     */
    int updateScoreStatus(@Param("courseId") String courseId, @Param("status") String status);
    
    /**
     * 批量插入成绩记录
     * @param scores 成绩列表
     * @return 影响行数
     */
    int batchInsertScores(@Param("scores") List<Score> scores);
    
    /**
     * 批量更新成绩记录
     * @param scores 成绩列表
     * @return 影响行数
     */
    int batchUpdateScores(@Param("scores") List<Score> scores);
    
    /**
     * 查询所有成绩记录
     * @return 成绩列表
     */
    List<Score> selectAllScores();
    
    /**
     * 多条件查询成绩列表（分页）
     * @param studentId 学号
     * @param courseId 课程号
     * @param keyword 关键词（课程名、学生名）
     * @param minScore 最低分
     * @param maxScore 最高分
     * @param status 状态
     * @param offset 偏移量
     * @param limit 每页大小
     * @return 成绩列表
     */
    List<Score> selectScoresByMultipleConditions(
            @Param("studentId") String studentId,
            @Param("courseId") String courseId,
            @Param("keyword") String keyword,
            @Param("minScore") Double minScore,
            @Param("maxScore") Double maxScore,
            @Param("status") String status,
            @Param("offset") int offset,
            @Param("limit") int limit);
    
    /**
     * 多条件查询成绩总数
     * @param studentId 学号
     * @param courseId 课程号
     * @param keyword 关键词（课程名、学生名）
     * @param minScore 最低分
     * @param maxScore 最高分
     * @param status 状态
     * @return 成绩总数
     */
    int countScoresByMultipleConditions(
            @Param("studentId") String studentId,
            @Param("courseId") String courseId,
            @Param("keyword") String keyword,
            @Param("minScore") Double minScore,
            @Param("maxScore") Double maxScore,
            @Param("status") String status);
}