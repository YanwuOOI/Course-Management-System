<template>
  <div class="score-stat-container">
    <el-card class="score-stat-card">
      <template #header>
        <div class="card-header">
          <h2>成绩统计</h2>
        </div>
      </template>
      
      <!-- 课程选择 -->
      <el-form :model="queryForm" label-width="80px" class="query-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="课程号">
              <el-input v-model="queryForm.courseId" placeholder="请输入课程号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="课程名">
              <el-input v-model="queryForm.courseName" placeholder="请输入课程名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作">
              <el-button type="primary" @click="handleQuery">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 统计结果 -->
      <el-row :gutter="20" class="stat-result" v-if="statData">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">课程名称</div>
              <div class="stat-value">{{ statData.courseName }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">平均分</div>
              <div class="stat-value">{{ statData.averageScore }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">最高分</div>
              <div class="stat-value">{{ statData.highestScore }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">最低分</div>
              <div class="stat-value">{{ statData.lowestScore }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="stat-result" v-if="statData">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">及格率</div>
              <div class="stat-value">{{ statData.passRate }}%</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">优秀人数</div>
              <div class="stat-value">{{ statData.excellentCount }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">良好人数</div>
              <div class="stat-value">{{ statData.goodCount }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">中等人数</div>
              <div class="stat-value">{{ statData.mediumCount }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="stat-result" v-if="statData">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">及格人数</div>
              <div class="stat-value">{{ statData.passCount }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">不及格人数</div>
              <div class="stat-value">{{ statData.failCount }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 成绩分布图表 -->
      <el-card class="chart-card" v-if="statData">
        <template #header>
          <div class="chart-header">
            <h3>成绩分布</h3>
          </div>
        </template>
        <div class="chart-content">
          <div class="chart-item">
            <div class="chart-label">优秀 (90-100)</div>
            <el-progress :percentage="excellentPercentage" color="#409EFF" />
          </div>
          <div class="chart-item">
            <div class="chart-label">良好 (80-89)</div>
            <el-progress :percentage="goodPercentage" color="#67C23A" />
          </div>
          <div class="chart-item">
            <div class="chart-label">中等 (70-79)</div>
            <el-progress :percentage="mediumPercentage" color="#E6A23C" />
          </div>
          <div class="chart-item">
            <div class="chart-label">及格 (60-69)</div>
            <el-progress :percentage="passPercentage" color="#909399" />
          </div>
          <div class="chart-item">
            <div class="chart-label">不及格 (0-59)</div>
            <el-progress :percentage="failPercentage" color="#F56C6C" />
          </div>
        </div>
      </el-card>
      
      <!-- 无数据提示 -->
      <el-empty v-else description="暂无数据" />
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { statCourseScores } from '../../api/score'

export default {
  name: 'ScoreStat',
  setup() {
    const queryForm = reactive({
      courseId: '',
      courseName: ''
    })
    
    const statData = ref(null)
    
    // 模拟统计数据
    const mockStatData = {
      courseId: 'CS202501',
      courseName: '软件工程导论',
      averageScore: 82.5,
      highestScore: 95.0,
      lowestScore: 60.0,
      passRate: 95.0,
      excellentCount: 5,
      goodCount: 15,
      mediumCount: 10,
      passCount: 5,
      failCount: 2
    }
    
    // 计算各等级百分比
    const totalCount = computed(() => {
      if (!statData.value) return 0
      return statData.value.excellentCount + statData.value.goodCount + 
             statData.value.mediumCount + statData.value.passCount + 
             statData.value.failCount
    })
    
    const excellentPercentage = computed(() => {
      if (!statData.value || totalCount.value === 0) return 0
      return Math.round((statData.value.excellentCount / totalCount.value) * 100)
    })
    
    const goodPercentage = computed(() => {
      if (!statData.value || totalCount.value === 0) return 0
      return Math.round((statData.value.goodCount / totalCount.value) * 100)
    })
    
    const mediumPercentage = computed(() => {
      if (!statData.value || totalCount.value === 0) return 0
      return Math.round((statData.value.mediumCount / totalCount.value) * 100)
    })
    
    const passPercentage = computed(() => {
      if (!statData.value || totalCount.value === 0) return 0
      return Math.round((statData.value.passCount / totalCount.value) * 100)
    })
    
    const failPercentage = computed(() => {
      if (!statData.value || totalCount.value === 0) return 0
      return Math.round((statData.value.failCount / totalCount.value) * 100)
    })
    
    // 处理查询
    const handleQuery = async () => {
      try {
        // 调用后端API获取统计数据
        const response = await statCourseScores(queryForm.courseId)
        statData.value = response.data
        ElMessage.success('查询成功')
      } catch (error) {
        ElMessage.error('查询失败：' + error.message)
        // 出错时使用模拟数据
        statData.value = mockStatData
      }
    }
    
    // 处理重置
    const handleReset = () => {
      Object.keys(queryForm).forEach(key => {
        queryForm[key] = ''
      })
      statData.value = null
    }
    
    return {
      queryForm,
      statData,
      excellentPercentage,
      goodPercentage,
      mediumPercentage,
      passPercentage,
      failPercentage,
      handleQuery,
      handleReset
    }
  }
}
</script>

<style scoped>
.score-stat-container {
  padding: 20px;
}

.score-stat-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.query-form {
  margin-bottom: 20px;
}

.stat-result {
  margin-bottom: 20px;
}

.stat-card {
  height: 100%;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.chart-card {
  margin-top: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-content {
  padding: 20px 0;
}

.chart-item {
  margin-bottom: 20px;
}

.chart-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}
</style>