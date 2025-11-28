<template>
  <div class="score-input-container">
    <el-card class="score-input-card">
      <template #header>
        <div class="card-header">
          <h2>成绩录入</h2>
        </div>
      </template>
      
      <!-- 课程选择 -->
      <el-form-item label="课程选择" class="course-select-item">
        <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="handleCourseChange">
          <el-option
            v-for="course in courseList"
            :key="course.courseId"
            :label="course.courseName"
            :value="course.courseId"
          >
            <div class="option-content">
              <div class="option-title">{{ course.courseName }}</div>
              <div class="option-desc">学分：{{ course.credit }} | 类型：{{ course.courseType }}</div>
            </div>
          </el-option>
        </el-select>
      </el-form-item>
      
      <!-- 成绩录入表格 -->
      <el-table
        v-if="scoreList.length > 0"
        :data="scoreList"
        border
        style="width: 100%"
        @cell-edit-complete="handleCellEditComplete"
      >
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="score" label="成绩" width="120">
          <template #default="scope">
            <el-input-number
              v-model="scope.row.score"
              :min="0"
              :max="100"
              :precision="1"
              @change="handleScoreChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="等级" width="120" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleSaveScore(scope.row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 无数据提示 -->
      <el-empty v-else description="请选择课程" />
      
      <!-- 底部操作按钮 -->
      <div class="score-input-footer">
        <el-button type="primary" @click="handleSubmitScores" :disabled="!selectedCourseId">提交成绩</el-button>
        <el-button type="warning" @click="handleLockScores" :disabled="!selectedCourseId">锁定成绩</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getScoresByCourseId, updateScore, submitScores, lockScores } from '../../api/score'

export default {
  name: 'ScoreInput',
  setup() {
    const selectedCourseId = ref('')
    const courseList = ref([])
    const scoreList = ref([])
    
    // 加载课程列表
    const loadCourseList = async () => {
      try {
        // 这里应该调用获取课程列表的API，暂时使用模拟数据
        // const response = await getCoursesByTeacherId('T001') // 实际应该从登录信息中获取
        courseList.value = [
          { courseId: 'CS202501', courseName: '软件工程导论', credit: 3, courseType: '必修课' },
          { courseId: 'CS202502', courseName: '数据结构', credit: 4, courseType: '必修课' },
          { courseId: 'CS202503', courseName: '人工智能', credit: 3, courseType: '选修课' },
          { courseId: 'CS202504', courseName: '机器学习', credit: 3, courseType: '选修课' }
        ]
      } catch (error) {
        ElMessage.error('获取课程列表失败：' + error.message)
      }
    }
    
    // 处理课程选择变化
    const handleCourseChange = async () => {
      if (selectedCourseId.value) {
        try {
          // 调用后端API获取成绩列表
          const response = await getScoresByCourseId(selectedCourseId.value)
          scoreList.value = response.data || []
        } catch (error) {
          ElMessage.error('获取成绩列表失败：' + error.message)
        }
      }
    }
    
    // 处理成绩变化
    const handleScoreChange = (row) => {
      // 自动计算成绩等级
      if (row.score >= 90) {
        row.grade = '优秀'
      } else if (row.score >= 80) {
        row.grade = '良好'
      } else if (row.score >= 70) {
        row.grade = '中等'
      } else if (row.score >= 60) {
        row.grade = '及格'
      } else {
        row.grade = '不及格'
      }
    }
    
    // 处理单元格编辑完成
    const handleCellEditComplete = (row, column, cellValue) => {
      console.log('Cell edit complete:', row, column, cellValue)
    }
    
    // 保存单个成绩
    const handleSaveScore = async (row) => {
      try {
        await updateScore(row.scoreId, {
          studentId: row.studentId,
          courseId: selectedCourseId.value,
          score: row.score,
          grade: row.grade,
          inputTeacherId: 'T001' // 实际项目中应从用户信息中获取
        })
        ElMessage.success('成绩保存成功')
      } catch (error) {
        ElMessage.error('成绩保存失败：' + error.message)
      }
    }
    
    // 提交成绩
    const handleSubmitScores = async () => {
      try {
        await submitScores(selectedCourseId.value)
        ElMessage.success('成绩提交成功')
        // 更新成绩列表中的状态
        scoreList.value.forEach(score => {
          score.status = '已提交'
        })
      } catch (error) {
        ElMessage.error('成绩提交失败：' + error.message)
      }
    }
    
    // 锁定成绩
    const handleLockScores = async () => {
      try {
        await lockScores(selectedCourseId.value)
        ElMessage.success('成绩锁定成功')
        // 更新成绩列表中的状态
        scoreList.value.forEach(score => {
          score.status = '已锁定'
        })
      } catch (error) {
        ElMessage.error('成绩锁定失败：' + error.message)
      }
    }
    
    // 页面加载时初始化数据
    onMounted(() => {
      loadCourseList()
    })
    
    return {
      selectedCourseId,
      courseList,
      scoreList,
      handleCourseChange,
      handleScoreChange,
      handleCellEditComplete,
      handleSaveScore,
      handleSubmitScores,
      handleLockScores
    }
  }
}
</script>

<style scoped>
.score-input-container {
  padding: 20px;
}

.score-input-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.course-select-item {
  margin-bottom: 20px;
}

.option-content {
  padding: 8px 0;
}

.option-title {
  font-weight: bold;
}

.option-desc {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.score-input-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
