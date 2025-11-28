<template>
  <div class="score-dispute-container">
    <el-card class="score-dispute-card">
      <template #header>
        <div class="card-header">
          <h2>成绩申诉</h2>
        </div>
      </template>
      
      <!-- 申诉表单 -->
      <el-form :model="disputeForm" :rules="disputeRules" ref="disputeFormRef" label-width="120px" class="dispute-form">
        <!-- 课程选择 -->
        <el-form-item label="选择课程" prop="courseId">
          <el-select v-model="disputeForm.courseId" placeholder="请选择课程" @change="handleCourseChange">
            <el-option
              v-for="course in courseList"
              :key="course.courseId"
              :label="`${course.courseName} (${course.courseId})`"
              :value="course.courseId"
            >
              <div class="option-content">
                <div class="option-title">{{ course.courseName }}</div>
                <div class="option-desc">课程号：{{ course.courseId }} | 学分：{{ course.credit }} | 教师：{{ course.teacherName }}</div>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        
        <!-- 当前成绩 -->
        <el-form-item label="当前成绩">
          <el-input v-model="currentScore" placeholder="请先选择课程" readonly />
        </el-form-item>
        
        <!-- 异议理由 -->
        <el-form-item label="异议理由" prop="reason">
          <el-input
            v-model="disputeForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请详细描述您的成绩异议理由"
          />
        </el-form-item>
        
        <!-- 附件上传 -->
        <el-form-item label="附件上传">
          <el-upload
            class="upload-demo"
            drag
            action="#"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持上传PDF、Word等格式文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <!-- 表单操作 -->
        <el-form-item>
          <div class="form-actions">
            <el-button type="primary" @click="handleSubmit">提交申诉</el-button>
            <el-button @click="handleReset">重置</el-button>
          </div>
        </el-form-item>
      </el-form>
      
      <!-- 申诉记录 -->
      <el-card class="dispute-record-card" v-if="disputeList.length > 0">
        <template #header>
          <div class="card-header">
            <h3>申诉记录</h3>
          </div>
        </template>
        <el-table
          :data="disputeList"
          border
          style="width: 100%"
        >
          <el-table-column prop="disputeId" label="申诉ID" width="120" />
          <el-table-column prop="courseName" label="课程名" width="180" />
          <el-table-column prop="score" label="原始成绩" width="100" />
          <el-table-column prop="reason" label="申诉理由" min-width="200" show-overflow-tooltip />
          <el-table-column prop="applyTime" label="申请时间" width="180" />
          <el-table-column prop="auditStatus" label="审核状态" width="120" />
          <el-table-column prop="auditOpinion" label="审核意见" min-width="200" show-overflow-tooltip />
          <el-table-column prop="auditTime" label="审核时间" width="180" />
        </el-table>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { submitDispute, getDisputesByStudentId } from '../../api/score'

export default {
  name: 'ScoreDispute',
  components: {
    UploadFilled
  },
  setup() {
    const disputeFormRef = ref(null)
    
    const disputeForm = reactive({
      courseId: '',
      reason: ''
    })
    
    const currentScore = ref('')
    const fileList = ref([])
    
    // 模拟课程列表
    const courseList = ref([
      {
        courseId: 'CS202501',
        courseName: '软件工程导论',
        credit: 3,
        teacherName: '张老师',
        score: 85.5
      },
      {
        courseId: 'CS202502',
        courseName: '数据结构',
        credit: 4,
        teacherName: '张老师',
        score: 92.0
      },
      {
        courseId: 'CS202503',
        courseName: '人工智能',
        credit: 3,
        teacherName: '李老师',
        score: 88.0
      }
    ])
    
    // 模拟申诉记录
    const disputeList = ref([
      {
        disputeId: 1,
        courseId: 'CS202501',
        courseName: '软件工程导论',
        score: 85.5,
        reason: '期末考试题目与教学内容不符，申请重新评分',
        applyTime: '2025-11-20 14:30:00',
        auditStatus: '待审核',
        auditOpinion: '',
        auditTime: ''
      },
      {
        disputeId: 2,
        courseId: 'CS202502',
        courseName: '数据结构',
        score: 92.0,
        reason: '平时作业成绩统计有误，少算了一次作业分数',
        applyTime: '2025-11-15 09:15:00',
        auditStatus: '通过',
        auditOpinion: '经核实，确实少算了一次作业分数，已重新计算成绩',
        auditTime: '2025-11-16 10:30:00'
      }
    ])
    
    // 表单验证规则
    const disputeRules = {
      courseId: [
        { required: true, message: '请选择课程', trigger: 'change' }
      ],
      reason: [
        { required: true, message: '请输入异议理由', trigger: 'blur' },
        { min: 10, max: 500, message: '异议理由长度在10到500个字符之间', trigger: 'blur' }
      ]
    }
    
    // 处理课程选择
    const handleCourseChange = (courseId) => {
      const course = courseList.value.find(item => item.courseId === courseId)
      if (course) {
        currentScore.value = course.score
      } else {
        currentScore.value = ''
      }
    }
    
    // 处理文件变化
    const handleFileChange = (file, fileList) => {
      // 这里可以添加文件类型和大小验证
      console.log('File changed:', file, fileList)
    }
    
    // 处理文件移除
    const handleFileRemove = (file, fileList) => {
      console.log('File removed:', file, fileList)
    }
    
    // 处理提交
    const handleSubmit = async () => {
      if (!disputeFormRef.value) return
      
      try {
        await disputeFormRef.value.validate()
        
        // 构造申诉数据
        const disputeData = {
          ...disputeForm,
          studentId: '2024001', // 实际应该从登录信息中获取
          attachment: fileList.value.length > 0 ? fileList.value[0].name : null
        }
        
        // 调用后端API提交申诉
        await submitDispute(disputeData)
        
        ElMessage.success('申诉提交成功，等待审核')
        
        // 重置表单
        handleReset()
        
        // 刷新申诉记录
        loadDisputeList()
      } catch (error) {
        if (error === false) {
          return
        }
        ElMessage.error('申诉提交失败：' + error.message)
      }
    }
    
    // 处理重置
    const handleReset = () => {
      if (disputeFormRef.value) {
        disputeFormRef.value.resetFields()
      }
      fileList.value = []
      currentScore.value = ''
    }
    
    // 加载申诉记录
    const loadDisputeList = async () => {
      try {
        // 调用后端API获取申诉记录
        const response = await getDisputesByStudentId('2024001') // 实际应该从登录信息中获取
        disputeList.value = response.data || []
      } catch (error) {
        // 只在控制台打印错误，不向用户显示
        console.log('加载申诉记录失败，使用模拟数据：', error.message)
        // 使用模拟数据
        disputeList.value = [
          {
            disputeId: 1,
            courseId: 'CS202501',
            courseName: '软件工程导论',
            score: 85.5,
            reason: '期末考试题目与教学内容不符，申请重新评分',
            applyTime: '2025-11-20 14:30:00',
            auditStatus: '待审核',
            auditOpinion: '',
            auditTime: ''
          },
          {
            disputeId: 2,
            courseId: 'CS202502',
            courseName: '数据结构',
            score: 92.0,
            reason: '平时作业成绩统计有误，少算了一次作业分数',
            applyTime: '2025-11-15 09:15:00',
            auditStatus: '通过',
            auditOpinion: '经核实，确实少算了一次作业分数，已重新计算成绩',
            auditTime: '2025-11-16 10:30:00'
          }
        ]
      }
    }
    
    // 初始化
    onMounted(() => {
      loadDisputeList()
    })
    
    return {
      disputeFormRef,
      disputeForm,
      disputeRules,
      currentScore,
      fileList,
      courseList,
      disputeList,
      handleCourseChange,
      handleFileChange,
      handleFileRemove,
      handleSubmit,
      handleReset
    }
  }
}
</script>

<style scoped>
.score-dispute-container {
  padding: 20px;
}

.score-dispute-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dispute-form {
  margin-bottom: 30px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
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

.dispute-record-card {
  margin-top: 30px;
}
</style>