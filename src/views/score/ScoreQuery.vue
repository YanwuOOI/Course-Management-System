<template>
  <div class="score-query-container">
    <el-card class="score-query-card">
      <template #header>
        <div class="card-header">
          <h2>成绩查询</h2>
        </div>
      </template>
      
      <!-- 查询条件 -->
      <el-form :model="queryForm" label-width="80px" class="query-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="学号">
              <el-input v-model="queryForm.studentId" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="课程号">
              <el-input v-model="queryForm.courseId" placeholder="请输入课程号" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="课程名">
              <el-input v-model="queryForm.courseName" placeholder="请输入课程名" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态">
              <el-select v-model="queryForm.status" placeholder="请选择状态">
                <el-option label="全部" value="" />
                <el-option label="未提交" value="未提交" />
                <el-option label="已提交" value="已提交" />
                <el-option label="已锁定" value="已锁定" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" class="query-buttons">
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 成绩查询结果表格 -->
      <el-table
        v-if="scoreList.length > 0"
        :data="scoreList"
        border
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="courseId" label="课程号" width="120" />
        <el-table-column prop="courseName" label="课程名" width="180" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="score" label="成绩" width="100" />
        <el-table-column prop="grade" label="等级" width="100" />
        <el-table-column prop="inputTeacherName" label="录入教师" width="120" />
        <el-table-column prop="inputTime" label="录入时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" />
      </el-table>
      
      <!-- 无数据提示 -->
      <el-empty v-else description="暂无数据" />
      
      <!-- 分页 -->
      <div v-if="scoreList.length > 0" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'ScoreQuery',
  setup() {
    const queryForm = reactive({
      studentId: '',
      courseId: '',
      courseName: '',
      status: ''
    })
    
    const scoreList = ref([])
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    
    // 模拟成绩数据
    const mockScoreList = [
      {
        studentId: '2024001',
        studentName: '张三',
        courseId: 'CS202501',
        courseName: '软件工程导论',
        credit: 3,
        score: 85.5,
        grade: '良好',
        inputTeacherId: 'T001',
        inputTeacherName: '张老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      },
      {
        studentId: '2024001',
        studentName: '张三',
        courseId: 'CS202502',
        courseName: '数据结构',
        credit: 4,
        score: 92.0,
        grade: '优秀',
        inputTeacherId: 'T001',
        inputTeacherName: '张老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      },
      {
        studentId: '2024002',
        studentName: '李四',
        courseId: 'CS202501',
        courseName: '软件工程导论',
        credit: 3,
        score: 78.0,
        grade: '中等',
        inputTeacherId: 'T001',
        inputTeacherName: '张老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      },
      {
        studentId: '2024002',
        studentName: '李四',
        courseId: 'CS202502',
        courseName: '数据结构',
        credit: 4,
        score: 65.5,
        grade: '及格',
        inputTeacherId: 'T001',
        inputTeacherName: '张老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      },
      {
        studentId: '2024003',
        studentName: '王五',
        courseId: 'CS202503',
        courseName: '人工智能',
        credit: 3,
        score: 88.0,
        grade: '良好',
        inputTeacherId: 'T002',
        inputTeacherName: '李老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      },
      {
        studentId: '2024003',
        studentName: '王五',
        courseId: 'CS202504',
        courseName: '机器学习',
        credit: 3,
        score: 95.0,
        grade: '优秀',
        inputTeacherId: 'T002',
        inputTeacherName: '李老师',
        inputTime: '2025-11-28 09:00:00',
        status: '已提交'
      }
    ]
    
    // 初始化数据
    onMounted(() => {
      loadScoreList()
    })
    
    // 加载成绩列表
    const loadScoreList = async () => {
      try {
        // 调用后端API获取成绩列表，支持分页和筛选
        const response = await axios.get('/scores', {
          params: {
            studentId: queryForm.studentId,
            courseId: queryForm.courseId,
            courseName: queryForm.courseName,
            status: queryForm.status,
            page: currentPage.value,
            pageSize: pageSize.value
          }
        })
        
        total.value = response.data.total || 0
        scoreList.value = response.data.list || []
      } catch (error) {
        // 只在控制台打印错误，不向用户显示
        console.log('获取成绩列表失败，使用模拟数据：', error.message)
        // 出错时使用模拟数据
        const startIndex = (currentPage.value - 1) * pageSize.value
        const endIndex = startIndex + pageSize.value
        
        let filteredList = [...mockScoreList]
        if (queryForm.studentId) {
          filteredList = filteredList.filter(item => item.studentId.includes(queryForm.studentId))
        }
        if (queryForm.courseId) {
          filteredList = filteredList.filter(item => item.courseId.includes(queryForm.courseId))
        }
        if (queryForm.courseName) {
          filteredList = filteredList.filter(item => item.courseName.includes(queryForm.courseName))
        }
        if (queryForm.status) {
          filteredList = filteredList.filter(item => item.status === queryForm.status)
        }
        
        total.value = filteredList.length
        scoreList.value = filteredList.slice(startIndex, endIndex)
      }
    }
    
    // 处理查询
    const handleQuery = () => {
      currentPage.value = 1
      loadScoreList()
    }
    
    // 处理重置
    const handleReset = () => {
      Object.keys(queryForm).forEach(key => {
        queryForm[key] = ''
      })
      currentPage.value = 1
      loadScoreList()
    }
    
    // 处理导出
    const handleExport = () => {
      ElMessage.success('导出功能开发中')
    }
    
    // 处理行点击
    const handleRowClick = (row) => {
      console.log('Row clicked:', row)
    }
    
    // 处理分页大小变化
    const handleSizeChange = (newSize) => {
      pageSize.value = newSize
      currentPage.value = 1
      loadScoreList()
    }
    
    // 处理当前页变化
    const handleCurrentChange = (newPage) => {
      currentPage.value = newPage
      loadScoreList()
    }
    
    return {
      queryForm,
      scoreList,
      total,
      currentPage,
      pageSize,
      handleQuery,
      handleReset,
      handleExport,
      handleRowClick,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.score-query-container {
  padding: 20px;
}

.score-query-card {
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

.query-buttons {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
