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
              <div class="stat-value">{{ statData.averageScore.toFixed(1) }}</div>
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
              <div class="stat-value">{{ statData.passRate.toFixed(1) }}%</div>
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
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-label">总人数</div>
              <div class="stat-value">{{ totalCount }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 成绩分布图表 -->
      <el-row :gutter="20" class="chart-row" v-if="statData">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <h3>成绩分布柱状图</h3>
              </div>
            </template>
            <div ref="barChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <h3>成绩分布饼图</h3>
              </div>
            </template>
            <div ref="pieChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 成绩分布图 -->
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
import { ref, reactive, computed, onMounted, watch, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { statCourseScores } from '../../api/score'

export default {
  name: 'ScoreStat',
  setup() {
    const queryForm = reactive({
      courseId: '',
      courseName: ''
    })
    
    const statData = ref(null)
    
    // 图表相关
    const barChartRef = ref(null)
    const pieChartRef = ref(null)
    let barChartInstance = null
    let pieChartInstance = null
    
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
    
    // 初始化柱状图
    const initBarChart = () => {
      if (barChartRef.value) {
        barChartInstance = echarts.init(barChartRef.value)
        
        // 监听窗口大小变化，调整图表大小
        window.addEventListener('resize', handleResize)
        
        updateBarChart()
      }
    }
    
    // 初始化饼图
    const initPieChart = () => {
      if (pieChartRef.value) {
        pieChartInstance = echarts.init(pieChartRef.value)
        
        // 监听窗口大小变化，调整图表大小
        window.addEventListener('resize', handleResize)
        
        updatePieChart()
      }
    }
    
    // 更新柱状图数据
    const updateBarChart = () => {
      if (!barChartInstance || !statData.value) return
      
      const option = {
        title: {
          text: '成绩分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['优秀', '良好', '中等', '及格', '不及格'],
          axisLabel: {
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '人数'
        },
        series: [
          {
            name: '人数',
            type: 'bar',
            data: [
              statData.value.excellentCount,
              statData.value.goodCount,
              statData.value.mediumCount,
              statData.value.passCount,
              statData.value.failCount
            ],
            itemStyle: {
              color: function(params) {
                const colors = ['#409EFF', '#67C23A', '#E6A23C', '#909399', '#F56C6C']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      
      barChartInstance.setOption(option)
    }
    
    // 更新饼图数据
    const updatePieChart = () => {
      if (!pieChartInstance || !statData.value) return
      
      const option = {
        title: {
          text: '成绩分布比例',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '成绩分布',
            type: 'pie',
            radius: '50%',
            center: ['50%', '60%'],
            data: [
              {
                value: statData.value.excellentCount,
                name: '优秀'
              },
              {
                value: statData.value.goodCount,
                name: '良好'
              },
              {
                value: statData.value.mediumCount,
                name: '中等'
              },
              {
                value: statData.value.passCount,
                name: '及格'
              },
              {
                value: statData.value.failCount,
                name: '不及格'
              }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            itemStyle: {
              color: function(params) {
                const colors = ['#409EFF', '#67C23A', '#E6A23C', '#909399', '#F56C6C']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      
      pieChartInstance.setOption(option)
    }
    
    // 窗口大小变化时调整图表大小
    const handleResize = () => {
      if (barChartInstance) {
        barChartInstance.resize()
      }
      if (pieChartInstance) {
        pieChartInstance.resize()
      }
    }
    
    // 销毁图表
    const destroyCharts = () => {
      if (barChartInstance) {
        barChartInstance.dispose()
        barChartInstance = null
      }
      if (pieChartInstance) {
        pieChartInstance.dispose()
        pieChartInstance = null
      }
      
      // 移除窗口大小变化监听
      window.removeEventListener('resize', handleResize)
    }
    
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
    
    // 监听统计数据变化，更新图表
    watch(statData, () => {
      if (statData.value) {
        if (!barChartInstance || !pieChartInstance) {
          initBarChart()
          initPieChart()
        } else {
          updateBarChart()
          updatePieChart()
        }
      }
    })
    
    // 组件挂载时初始化图表
    onMounted(() => {
      initBarChart()
      initPieChart()
    })
    
    // 组件卸载前销毁图表
    onBeforeUnmount(() => {
      destroyCharts()
    })
    
    return {
      queryForm,
      statData,
      totalCount,
      excellentPercentage,
      goodPercentage,
      mediumPercentage,
      passPercentage,
      failPercentage,
      barChartRef,
      pieChartRef,
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

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  margin-top: 20px;
  height: 100%;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 400px;
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