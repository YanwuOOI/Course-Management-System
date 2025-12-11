package com.cms.controller;

import com.cms.entity.Score;
import com.cms.dto.ScoreDTO;
import com.cms.dto.ScoreStatDTO;
import com.cms.dto.PageResult;
import com.cms.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成绩Controller
 * 处理成绩管理的HTTP请求
 * @author 许祖嘉
 * @since 2025-11-30
 * @module 成绩管理模块
 */
@RestController
@RequestMapping("/scores")
public class ScoreController {
    
    @Autowired
    private ScoreService scoreService;
    
    /**
     * 录入成绩
     * @param scoreDTO 成绩DTO
     * @return 成绩实体
     */
    @PostMapping
    public ResponseEntity<Score> inputScore(@RequestBody ScoreDTO scoreDTO) {
        try {
            Score score = scoreService.inputScore(scoreDTO);
            return new ResponseEntity<>(score, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 批量录入成绩
     * @param scoreDTOs 成绩DTO列表
     * @return 成功录入的数量
     */
    @PostMapping("/batch")
    public ResponseEntity<Integer> batchInputScores(@RequestBody List<ScoreDTO> scoreDTOs) {
        try {
            int successCount = scoreService.batchInputScores(scoreDTOs);
            return new ResponseEntity<>(successCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 修改成绩
     * @param scoreId 成绩ID
     * @param scoreDTO 成绩DTO
     * @return 成绩实体
     */
    @PutMapping("/{scoreId}")
    public ResponseEntity<Score> updateScore(@PathVariable Integer scoreId, @RequestBody ScoreDTO scoreDTO) {
        try {
            scoreDTO.setScoreId(scoreId);
            Score score = scoreService.updateScore(scoreDTO);
            return new ResponseEntity<>(score, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 删除成绩
     * @param scoreId 成绩ID
     * @return 是否成功
     */
    @DeleteMapping("/{scoreId}")
    public ResponseEntity<Boolean> deleteScore(@PathVariable Integer scoreId) {
        try {
            boolean success = scoreService.deleteScore(scoreId);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据成绩ID查询成绩
     * @param scoreId 成绩ID
     * @return 成绩实体
     */
    @GetMapping("/{scoreId}")
    public ResponseEntity<Score> getScoreById(@PathVariable Integer scoreId) {
        try {
            Score score = scoreService.getScoreById(scoreId);
            if (score != null) {
                return new ResponseEntity<>(score, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据学号和课程号查询成绩
     * @param studentId 学号
     * @param courseId 课程号
     * @return 成绩实体
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Score> getScoreByStudentAndCourse(
            @PathVariable String studentId, @PathVariable String courseId) {
        try {
            Score score = scoreService.getScoreByStudentAndCourse(studentId, courseId);
            if (score != null) {
                return new ResponseEntity<>(score, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据课程号查询成绩列表
     * @param courseId 课程号
     * @return 成绩列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Score>> getScoresByCourseId(@PathVariable String courseId) {
        try {
            List<Score> scores = scoreService.getScoresByCourseId(courseId);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据学号查询成绩列表
     * @param studentId 学号
     * @return 成绩列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable String studentId) {
        try {
            List<Score> scores = scoreService.getScoresByStudentId(studentId);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据教师ID查询所授课程的成绩列表
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Score>> getScoresByTeacherId(@PathVariable String teacherId) {
        try {
            List<Score> scores = scoreService.getScoresByTeacherId(teacherId);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 提交成绩
     * @param courseId 课程号
     * @return 是否成功
     */
    @PutMapping("/course/{courseId}/submit")
    public ResponseEntity<Boolean> submitScores(@PathVariable String courseId) {
        try {
            boolean success = scoreService.submitScores(courseId);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 锁定成绩
     * @param courseId 课程号
     * @return 是否成功
     */
    @PutMapping("/course/{courseId}/lock")
    public ResponseEntity<Boolean> lockScores(@PathVariable String courseId) {
        try {
            boolean success = scoreService.lockScores(courseId);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 统计课程成绩
     * @param courseId 课程号
     * @return 成绩统计DTO
     */
    @GetMapping("/course/{courseId}/stat")
    public ResponseEntity<ScoreStatDTO> statCourseScores(@PathVariable String courseId) {
        try {
            ScoreStatDTO statDTO = scoreService.statCourseScores(courseId);
            return new ResponseEntity<>(statDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 归档成绩
     * @param semester 学期
     * @return 是否成功
     */
    @PostMapping("/archive/{semester}")
    public ResponseEntity<Boolean> archiveScores(@PathVariable String semester) {
        try {
            boolean success = scoreService.archiveScores(semester);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 导出课程成绩到Excel
     * @param courseId 课程号
     * @return Excel文件
     */
    @GetMapping("/course/{courseId}/export")
    public ResponseEntity<byte[]> exportCourseScores(@PathVariable String courseId) {
        try {
            byte[] excelBytes = scoreService.exportCourseScores(courseId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=scores_" + courseId + ".xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelBytes);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 导出所有成绩到Excel
     * @return Excel文件
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAllScores() {
        try {
            byte[] excelBytes = scoreService.exportAllScores();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=all_scores.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelBytes);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
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
    @GetMapping("/search")
    public ResponseEntity<PageResult<Score>> searchScores(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minScore,
            @RequestParam(required = false) Double maxScore,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            PageResult<Score> scores = scoreService.searchScores(studentId, courseId, keyword, minScore, maxScore, status, page, pageSize);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}