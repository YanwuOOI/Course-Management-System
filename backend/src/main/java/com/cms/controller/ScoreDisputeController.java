package com.cms.controller;

import com.cms.entity.ScoreDispute;
import com.cms.dto.ScoreDisputeDTO;
import com.cms.service.ScoreDisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成绩异议Controller
 * 处理成绩异议的HTTP请求
 * @author 许祖嘉
 * @date 2025-11-28
 */
@RestController
@RequestMapping("/score-disputes")
public class ScoreDisputeController {
    
    @Autowired
    private ScoreDisputeService scoreDisputeService;
    
    /**
     * 提交成绩异议
     * @param scoreDisputeDTO 成绩异议DTO
     * @return 成绩异议实体
     */
    @PostMapping
    public ResponseEntity<ScoreDispute> submitDispute(@RequestBody ScoreDisputeDTO scoreDisputeDTO) {
        try {
            ScoreDispute scoreDispute = scoreDisputeService.submitDispute(scoreDisputeDTO);
            return new ResponseEntity<>(scoreDispute, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 审核成绩异议
     * @param disputeId 异议ID
     * @param scoreDisputeDTO 成绩异议DTO
     * @return 成绩异议实体
     */
    @PutMapping("/{disputeId}")
    public ResponseEntity<ScoreDispute> auditDispute(@PathVariable Integer disputeId, @RequestBody ScoreDisputeDTO scoreDisputeDTO) {
        try {
            scoreDisputeDTO.setDisputeId(disputeId);
            ScoreDispute scoreDispute = scoreDisputeService.auditDispute(scoreDisputeDTO);
            return new ResponseEntity<>(scoreDispute, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 删除成绩异议
     * @param disputeId 异议ID
     * @return 是否成功
     */
    @DeleteMapping("/{disputeId}")
    public ResponseEntity<Boolean> deleteDispute(@PathVariable Integer disputeId) {
        try {
            boolean success = scoreDisputeService.deleteDispute(disputeId);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据异议ID查询成绩异议
     * @param disputeId 异议ID
     * @return 成绩异议实体
     */
    @GetMapping("/{disputeId}")
    public ResponseEntity<ScoreDispute> getDisputeById(@PathVariable Integer disputeId) {
        try {
            ScoreDispute scoreDispute = scoreDisputeService.getDisputeById(disputeId);
            if (scoreDispute != null) {
                return new ResponseEntity<>(scoreDispute, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据学号查询成绩异议列表
     * @param studentId 学号
     * @return 成绩异议列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ScoreDispute>> getDisputesByStudentId(@PathVariable String studentId) {
        try {
            List<ScoreDispute> disputes = scoreDisputeService.getDisputesByStudentId(studentId);
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据课程号查询成绩异议列表
     * @param courseId 课程号
     * @return 成绩异议列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ScoreDispute>> getDisputesByCourseId(@PathVariable String courseId) {
        try {
            List<ScoreDispute> disputes = scoreDisputeService.getDisputesByCourseId(courseId);
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据教师ID查询成绩异议列表
     * @param teacherId 教师ID
     * @return 成绩异议列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ScoreDispute>> getDisputesByTeacherId(@PathVariable String teacherId) {
        try {
            List<ScoreDispute> disputes = scoreDisputeService.getDisputesByTeacherId(teacherId);
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 查询所有成绩异议列表
     * @return 成绩异议列表
     */
    @GetMapping
    public ResponseEntity<List<ScoreDispute>> getAllDisputes() {
        try {
            List<ScoreDispute> disputes = scoreDisputeService.getAllDisputes();
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据审核状态查询成绩异议列表
     * @param auditStatus 审核状态
     * @return 成绩异议列表
     */
    @GetMapping("/status/{auditStatus}")
    public ResponseEntity<List<ScoreDispute>> getDisputesByAuditStatus(@PathVariable String auditStatus) {
        try {
            List<ScoreDispute> disputes = scoreDisputeService.getDisputesByAuditStatus(auditStatus);
            return new ResponseEntity<>(disputes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}