package com.cms.service;

import com.cms.entity.Course;
import com.cms.dto.CourseDTO;
import com.cms.dto.PageResult;

import java.util.List;

/**
 * 课程Service接口
 * 定义课程管理的业务逻辑方法
 * @author 张嘉欢
 * @date 2025-11-30
 */
public interface CourseService {
    /**
     * 创建课程
     * @param courseDTO 课程DTO
     * @return 课程实体
     */
    Course createCourse(CourseDTO courseDTO);
    
    /**
     * 修改课程
     * @param courseDTO 课程DTO
     * @return 课程实体
     */
    Course updateCourse(CourseDTO courseDTO);
    
    /**
     * 删除课程
     * @param courseId 课程号
     * @return 是否成功
     */
    boolean deleteCourse(String courseId);
    
    /**
     * 根据课程号查询课程
     * @param courseId 课程号
     * @return 课程实体
     */
    Course getCourseById(String courseId);
    
    /**
     * 查询所有课程
     * @return 课程列表
     */
    List<Course> getAllCourses();
    
    /**
     * 根据教师ID查询课程
     * @param teacherId 教师ID
     * @return 课程列表
     */
    List<Course> getCoursesByTeacherId(String teacherId);
    
    /**
     * 根据课程类型查询课程
     * @param courseType 课程类型
     * @return 课程列表
     */
    List<Course> getCoursesByType(String courseType);
    
    /**
     * 根据课程名称模糊查询课程
     * @param courseName 课程名称
     * @return 课程列表
     */
    List<Course> getCoursesByName(String courseName);
    
    /**
     * 多条件查询课程（分页）
     * @param keyword 关键词（课程名、教师名）
     * @param courseType 课程类型
     * @param dept 院系
     * @param teacherId 教师ID
     * @param sortBy 排序字段
     * @param order 排序方式（asc/desc）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Course> searchCourses(String keyword, String courseType, String dept, String teacherId, String sortBy, String order, int page, int pageSize);
    
    /**
     * 多条件查询课程（不分页）
     * @param keyword 关键词（课程名、教师名）
     * @param courseType 课程类型
     * @param dept 院系
     * @param teacherId 教师ID
     * @param sortBy 排序字段
     * @param order 排序方式（asc/desc）
     * @return 课程列表
     */
    List<Course> searchCourses(String keyword, String courseType, String dept, String teacherId, String sortBy, String order);
    
    /**
     * 检查课程是否已满
     * @param courseId 课程号
     * @return 是否已满
     */
    boolean isCourseFull(String courseId);
    
    /**
     * 更新课程已选人数
     * @param courseId 课程号
     * @param changeNum 变化数量
     * @return 是否成功
     */
    boolean updateSelectedNum(String courseId, Integer changeNum);
}
