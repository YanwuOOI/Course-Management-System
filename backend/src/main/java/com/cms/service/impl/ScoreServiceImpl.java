package com.cms.service.impl;

import com.cms.entity.Score;
import com.cms.dao.ScoreDao;
import com.cms.dto.ScoreDTO;
import com.cms.dto.ScoreStatDTO;
import com.cms.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 成绩Service实现类
 * 实现成绩管理的业务逻辑
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Service
public class ScoreServiceImpl implements ScoreService {
    
    @Autowired
    private ScoreDao scoreDao;
    
    @Override
    @Transactional
    public Score inputScore(ScoreDTO scoreDTO) {
        // 检查成绩是否已存在
        Score existingScore = scoreDao.selectScoreByStudentAndCourse(
                scoreDTO.getStudentId(), scoreDTO.getCourseId());
        
        Score score = new Score();
        BeanUtils.copyProperties(scoreDTO, score);
        
        // 计算成绩等级
        score.setGrade(calculateGrade(score.getScore()));
        score.setInputTime(LocalDateTime.now());
        score.setUpdateTime(LocalDateTime.now());
        score.setStatus("未提交");
        
        if (existingScore != null) {
            // 更新现有成绩
            score.setScoreId(existingScore.getScoreId());
            scoreDao.updateScore(score);
        } else {
            // 插入新成绩
            scoreDao.insertScore(score);
        }
        
        return score;
    }
    
    @Override
    @Transactional
    public int batchInputScores(List<ScoreDTO> scoreDTOs) {
        int successCount = 0;
        
        for (ScoreDTO scoreDTO : scoreDTOs) {
            try {
                inputScore(scoreDTO);
                successCount++;
            } catch (Exception e) {
                // 记录日志，继续处理其他成绩
                e.printStackTrace();
            }
        }
        
        return successCount;
    }
    
    @Override
    @Transactional
    public Score updateScore(ScoreDTO scoreDTO) {
        // 检查成绩是否存在
        Score existingScore = scoreDao.selectScoreById(scoreDTO.getScoreId());
        if (existingScore == null) {
            throw new RuntimeException("成绩不存在");
        }
        
        // 检查成绩是否已锁定
        if ("已锁定".equals(existingScore.getStatus())) {
            throw new RuntimeException("成绩已锁定，无法修改");
        }
        
        Score score = new Score();
        BeanUtils.copyProperties(scoreDTO, score);
        
        // 计算成绩等级
        score.setGrade(calculateGrade(score.getScore()));
        score.setUpdateTime(LocalDateTime.now());
        
        scoreDao.updateScore(score);
        return score;
    }
    
    @Override
    @Transactional
    public boolean deleteScore(Integer scoreId) {
        // 检查成绩是否存在
        Score existingScore = scoreDao.selectScoreById(scoreId);
        if (existingScore == null) {
            return false;
        }
        
        // 检查成绩是否已锁定
        if ("已锁定".equals(existingScore.getStatus())) {
            throw new RuntimeException("成绩已锁定，无法删除");
        }
        
        return scoreDao.deleteScoreById(scoreId) > 0;
    }
    
    @Override
    public Score getScoreById(Integer scoreId) {
        return scoreDao.selectScoreById(scoreId);
    }
    
    @Override
    public Score getScoreByStudentAndCourse(String studentId, String courseId) {
        return scoreDao.selectScoreByStudentAndCourse(studentId, courseId);
    }
    
    @Override
    public List<Score> getScoresByCourseId(String courseId) {
        return scoreDao.selectScoresByCourseId(courseId);
    }
    
    @Override
    public List<Score> getScoresByStudentId(String studentId) {
        return scoreDao.selectScoresByStudentId(studentId);
    }
    
    @Override
    public List<Score> getScoresByTeacherId(String teacherId) {
        return scoreDao.selectScoresByTeacherId(teacherId);
    }
    
    @Override
    @Transactional
    public boolean submitScores(String courseId) {
        return scoreDao.updateScoreStatus(courseId, "已提交") > 0;
    }
    
    @Override
    @Transactional
    public boolean lockScores(String courseId) {
        return scoreDao.updateScoreStatus(courseId, "已锁定") > 0;
    }
    
    @Override
    public String calculateGrade(Double score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "中等";
        } else if (score >= 60) {
            return "及格";
        } else {
            return "不及格";
        }
    }
    
    @Override
    public ScoreStatDTO statCourseScores(String courseId) {
        List<Score> scores = scoreDao.selectScoresByCourseId(courseId);
        
        ScoreStatDTO statDTO = new ScoreStatDTO();
        statDTO.setCourseId(courseId);
        statDTO.setTotalStudents(scores.size());
        
        if (scores.isEmpty()) {
            return statDTO;
        }
        
        // 计算统计数据
        double sum = 0;
        double highest = scores.get(0).getScore();
        double lowest = scores.get(0).getScore();
        int excellent = 0;
        int good = 0;
        int medium = 0;
        int pass = 0;
        int fail = 0;
        
        for (Score score : scores) {
            double scoreValue = score.getScore();
            sum += scoreValue;
            
            // 更新最高分和最低分
            if (scoreValue > highest) {
                highest = scoreValue;
            }
            if (scoreValue < lowest) {
                lowest = scoreValue;
            }
            
            // 更新各分数段人数
            if (scoreValue >= 90) {
                excellent++;
            } else if (scoreValue >= 80) {
                good++;
            } else if (scoreValue >= 70) {
                medium++;
            } else if (scoreValue >= 60) {
                pass++;
            } else {
                fail++;
            }
        }
        
        // 计算平均分
        double average = sum / scores.size();
        
        // 计算及格率
        double passRate = (double) (excellent + good + medium + pass) / scores.size() * 100;
        
        // 设置统计结果
        statDTO.setAverageScore(Math.round(average * 10) / 10.0);
        statDTO.setHighestScore(highest);
        statDTO.setLowestScore(lowest);
        statDTO.setPassRate(Math.round(passRate * 10) / 10.0);
        statDTO.setExcellentCount(excellent);
        statDTO.setGoodCount(good);
        statDTO.setMediumCount(medium);
        statDTO.setPassCount(pass);
        statDTO.setFailCount(fail);
        
        return statDTO;
    }
    
    @Override
    @Transactional
    public boolean archiveScores(String semester) {
        // 这里简化实现，实际应该将成绩数据归档到历史表
        // 并更新当前成绩表的状态
        return true;
    }
}