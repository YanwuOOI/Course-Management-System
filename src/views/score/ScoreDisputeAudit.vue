<template>
  <div class="score-dispute-audit-container">
    <el-card class="score-dispute-audit-card">
      <template #header>
        <div class="card-header">
          <h2>成绩申诉审核</h2>
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
            <el-form-item label="审核状态">
              <el-select v-model="queryForm.auditStatus" placeholder="请选择审核状态">
                <el-option label="全部" value="" />
                <el-option label="待审核" value="待审核" />
                <el-option label="通过" value="通过" />
                <el-option label="驳回" value="驳回" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="操作">
              <el-button type="primary" @click="handleQuery">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 申诉列表 -->
      <el-table
        :data="disputeList"
        border
        style="width: 100%"
        @row-click="handleRowClick"
        ref="disputeTableRef"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="disputeId" label="申诉ID" width="120" />
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="courseId" label="课程号" width="120" />
        <el-table-column prop="courseName" label="课程名" width="180" />
        <el-table-column prop="score" label="原始成绩" width="100" />
        <el-table-column prop="reason" label="申诉理由" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applyTime" label="申请时间" width="180" />
        <el-table-column prop="auditStatus" label="审核状态" width="120">
          <template #default="scope">
            <el-tag
              :type="scope.row.auditStatus === '待审核' ? 'warning' : 
                     scope.row.auditStatus === '通过' ? 'success' : 'danger'"
            >
              {{ scope.row.auditStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditOpinion" label="审核意见" min-width="200" show-overflow-tooltip />
        <el-table-column prop="auditTime" label="审核时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleAudit(scope.row)"
              :disabled="scope.row.auditStatus !== '待审核'"
            >
              审核
            </el-button>
            <el-button
              type="info"
              size="small"
              @click="handleViewDetail(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
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
    
    <!-- 审核弹窗 -->
    <el-dialog
      v-model="auditDialogVisible"
      title="成绩申诉审核"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="auditForm" :rules="auditRules" ref="auditFormRef" label-width="120px" class="audit-form">
        <!-- 申诉基本信息 -->
        <el-form-item label="申诉ID">
          <el-input v-model="auditForm.disputeId" readonly />
        </el-form-item>
        <el-form-item label="学生信息">
          <el-input v-model="`${auditForm.studentId} - ${auditForm.studentName}`" readonly />
        </el-form-item>
        <el-form-item label="课程信息">
          <el-input v-model="`${auditForm.courseName} (${auditForm.courseId})`" readonly />
        </el-form-item>
        <el-form-item label="原始成绩">
          <el-input v-model="auditForm.score" readonly />
        </el-form-item>
        <el-form-item label="申诉理由">
          <el-input
            v-model="auditForm.reason"
            type="textarea"
            :rows="4"
            readonly
          />
        </el-form-item>
        
        <!-- 审核信息 -->
        <el-form-item label="审核状态" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio label="通过" />
            <el-radio label="驳回" />
          </el-radio-group>
        </el-form-item>
        
        <!-- 新成绩（仅审核通过时必填） -->
        <el-form-item 
          label="新成绩" 
          prop="newScore"
          v-if="auditForm.auditStatus === '通过'"
        >
          <el-input-number
            v-model="auditForm.newScore"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="请输入新成绩"
          />
        </el-form-item>
        
        <el-form-item label="审核意见" prop="auditOpinion">
          <el-input
            v-model="auditForm.auditOpinion"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleAuditSubmit">提交审核</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="成绩申诉详情"
      width="600px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="申诉ID">{{ selectedDispute.disputeId }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ selectedDispute.studentId }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ selectedDispute.studentName }}</el-descriptions-item>
        <el-descriptions-item label="课程号">{{ selectedDispute.courseId }}</el-descriptions-item>
        <el-descriptions-item label="课程名">{{ selectedDispute.courseName }}</el-descriptions-item>
        <el-descriptions-item label="原始成绩">{{ selectedDispute.score }}</el-descriptions-item>
        <el-descriptions-item label="申诉理由">{{ selectedDispute.reason }}</el-descriptions-item>
        <el-descriptions-item label="附件">{{ selectedDispute.attachment || '无' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ selectedDispute.applyTime }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag
            :type="selectedDispute.auditStatus === '待审核' ? 'warning' : 
                   selectedDispute.auditStatus === '通过' ? 'success' : 'danger'"
          >
            {{ selectedDispute.auditStatus }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核意见">{{ selectedDispute.auditOpinion || '无' }}</el-descriptions-item>
        <el-descriptions-item label="审核人">{{ selectedDispute.auditUserId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ selectedDispute.auditTime || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { auditDispute, getAllDisputes } from '../../api/score'

export default {
  name: 'ScoreDisputeAudit',
  setup() {
    const disputeTableRef = ref(null)
    const auditFormRef = ref(null)
    
    const queryForm = reactive({
      studentId: '',
      courseId: '',
      auditStatus: ''
    })
    
    const disputeList = ref([])
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    
    const auditDialogVisible = ref(false)
    const detailDialogVisible = ref(false)
    
    const selectedDispute = ref({})
    
    const auditForm = reactive({
      disputeId: '',
      studentId: '',
      studentName: '',
      courseId: '',
      courseName: '',
      score: '',
      reason: '',
      auditStatus: '',
      auditOpinion: '',
      newScore: ''
    })
    
    const auditRules = {
      auditStatus: [
        { required: true, message: '请选择审核状态', trigger: 'change' }
      ],
      newScore: [
        {
          required: (rule, value, callback) => {
            if (auditForm.auditStatus === '通过' && !value) {
              callback(new Error('审核通过时必须输入新成绩'))
            } else {
              callback()
            }
          },
          trigger: ['blur', 'change']
        }
      ],
      auditOpinion: [
        { required: true, message: '请输入审核意见', trigger: 'blur' },
        { min: 5, max: 500, message: '审核意见长度在5到500个字符之间', trigger: 'blur' }
      ]
    }
    
    // 模拟申诉列表数据
    const mockDisputeList = [
      {
        disputeId: 1,
        studentId: '2024001',
        studentName: '张三',
        courseId: 'CS202501',
        courseName: '软件工程导论',
        score: 85.5,
        reason: '期末考试题目与教学内容不符，申请重新评分',
        attachment: null,
        applyTime: '2025-11-20 14:30:00',
        auditStatus: '待审核',
        auditOpinion: '',
        auditUserId: null,
        auditTime: null
      },
      {
        disputeId: 2,
        studentId: '2024002',
        studentName: '李四',
        courseId: 'CS202502',
        courseName: '数据结构',
        score: 65.5,
        reason: '平时作业成绩统计有误，少算了一次作业分数',
        attachment: '作业截图.pdf',
        applyTime: '2025-11-15 09:15:00',
        auditStatus: '通过',
        auditOpinion: '经核实，确实少算了一次作业分数，已重新计算成绩',
        auditUserId: 'T001',
        auditTime: '2025-11-16 10:30:00'
      },
      {
        disputeId: 3,
        studentId: '2024003',
        studentName: '王五',
        courseId: 'CS202503',
        courseName: '人工智能',
        score: 78.0,
        reason: '实验报告评分标准不明确，申请重新评分',
        attachment: '实验报告.pdf',
        applyTime: '2025-11-18 16:45:00',
        auditStatus: '驳回',
        auditOpinion: '评分标准已在课程大纲中明确说明，评分结果无误',
        auditUserId: 'T002',
        auditTime: '2025-11-19 09:20:00'
      },
      {
        disputeId: 4,
        studentId: '2024001',
        studentName: '张三',
        courseId: 'CS202503',
        courseName: '人工智能',
        score: 88.0,
        reason: '期末考试答题纸可能被漏判，申请重新检查',
        attachment: null,
        applyTime: '2025-11-22 11:20:00',
        auditStatus: '待审核',
        auditOpinion: '',
        auditUserId: null,
        auditTime: null
      }
    ]
    
    // 加载申诉列表
    const loadDisputeList = async () => {
      try {
        // 调用后端API获取申诉列表，支持分页和筛选
        const response = await getAllDisputes({
          params: {
            studentId: queryForm.studentId,
            courseId: queryForm.courseId,
            auditStatus: queryForm.auditStatus,
            page: currentPage.value,
            pageSize: pageSize.value
          }
        })
        
        total.value = response.data.total || 0
        disputeList.value = response.data.list || []
      } catch (error) {
        ElMessage.error('获取申诉列表失败：' + error.message)
        // 出错时使用模拟数据
        const startIndex = (currentPage.value - 1) * pageSize.value
        const endIndex = startIndex + pageSize.value
        
        let filteredList = [...mockDisputeList]
        if (queryForm.studentId) {
          filteredList = filteredList.filter(item => item.studentId.includes(queryForm.studentId))
        }
        if (queryForm.courseId) {
          filteredList = filteredList.filter(item => item.courseId.includes(queryForm.courseId))
        }
        if (queryForm.auditStatus) {
          filteredList = filteredList.filter(item => item.auditStatus === queryForm.auditStatus)
        }
        
        total.value = filteredList.length
        disputeList.value = filteredList.slice(startIndex, endIndex)
      }
    }
    
    // 处理查询
    const handleQuery = () => {
      currentPage.value = 1
      loadDisputeList()
    }
    
    // 处理重置
    const handleReset = () => {
      Object.keys(queryForm).forEach(key => {
        queryForm[key] = ''
      })
      currentPage.value = 1
      loadDisputeList()
    }
    
    // 处理行点击
    const handleRowClick = (row) => {
      console.log('Row clicked:', row)
    }
    
    // 处理分页大小变化
    const handleSizeChange = (newSize) => {
      pageSize.value = newSize
      currentPage.value = 1
      loadDisputeList()
    }
    
    // 处理当前页变化
    const handleCurrentChange = (newPage) => {
      currentPage.value = newPage
      loadDisputeList()
    }
    
    // 处理审核
    const handleAudit = (row) => {
      selectedDispute.value = { ...row }
      Object.assign(auditForm, row)
      auditDialogVisible.value = true
    }
    
    // 处理查看详情
    const handleViewDetail = (row) => {
      selectedDispute.value = { ...row }
      detailDialogVisible.value = true
    }
    
    // 处理审核提交
    const handleAuditSubmit = async () => {
      if (!auditFormRef.value) return
      
      try {
        await auditFormRef.value.validate()
        
        // 构造审核数据
        const auditData = {
          disputeId: auditForm.disputeId,
          auditStatus: auditForm.auditStatus,
          auditOpinion: auditForm.auditOpinion,
          auditUserId: 'T001', // 实际应该从登录信息中获取
          newScore: auditForm.auditStatus === '通过' ? auditForm.newScore : null
        }
        
        // 调用后端API审核申诉
        const response = await auditDispute(auditForm.disputeId, auditData)
        
        ElMessage.success('审核成功')
        auditDialogVisible.value = false
        
        // 刷新申诉列表
        loadDisputeList()
      } catch (error) {
        if (error === false) {
          return
        }
        ElMessage.error('审核失败：' + error.message)
      }
    }
    
    // 处理弹窗关闭
    const handleDialogClose = () => {
      if (auditFormRef.value) {
        auditFormRef.value.resetFields()
      }
    }
    
    // 初始化
    onMounted(() => {
      loadDisputeList()
    })
    
    return {
      disputeTableRef,
      auditFormRef,
      queryForm,
      disputeList,
      total,
      currentPage,
      pageSize,
      auditDialogVisible,
      detailDialogVisible,
      selectedDispute,
      auditForm,
      auditRules,
      handleQuery,
      handleReset,
      handleRowClick,
      handleSizeChange,
      handleCurrentChange,
      handleAudit,
      handleViewDetail,
      handleAuditSubmit,
      handleDialogClose
    }
  }
}
</script>

<style scoped>
.score-dispute-audit-container {
  padding: 20px;
}

.score-dispute-audit-card {
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.audit-form {
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>