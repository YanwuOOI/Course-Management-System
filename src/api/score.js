import axios from 'axios'

// 成绩录入
export const inputScore = (scoreData) => {
  return axios.post('/scores', scoreData)
}

// 批量录入成绩
export const batchInputScores = (scoreDataList) => {
  return axios.post('/scores/batch', scoreDataList)
}

// 修改成绩
export const updateScore = (scoreId, scoreData) => {
  return axios.put(`/scores/${scoreId}`, scoreData)
}

// 删除成绩
export const deleteScore = (scoreId) => {
  return axios.delete(`/scores/${scoreId}`)
}

// 根据成绩ID查询成绩
export const getScoreById = (scoreId) => {
  return axios.get(`/scores/${scoreId}`)
}

// 根据学生和课程查询成绩
export const getScoreByStudentAndCourse = (studentId, courseId) => {
  return axios.get(`/scores/student/${studentId}/course/${courseId}`)
}

// 根据课程查询成绩列表
export const getScoresByCourseId = (courseId) => {
  return axios.get(`/scores/course/${courseId}`)
}

// 根据学生查询成绩列表
export const getScoresByStudentId = (studentId) => {
  return axios.get(`/scores/student/${studentId}`)
}

// 根据教师查询成绩列表
export const getScoresByTeacherId = (teacherId) => {
  return axios.get(`/scores/teacher/${teacherId}`)
}

// 提交成绩
export const submitScores = (courseId) => {
  return axios.put(`/scores/course/${courseId}/submit`)
}

// 锁定成绩
export const lockScores = (courseId) => {
  return axios.put(`/scores/course/${courseId}/lock`)
}

// 统计课程成绩
export const statCourseScores = (courseId) => {
  return axios.get(`/scores/course/${courseId}/stat`)
}

// 归档成绩
export const archiveScores = (semester) => {
  return axios.post(`/scores/archive/${semester}`)
}

// 提交成绩异议
export const submitDispute = (disputeData) => {
  // 如果是FormData类型，需要设置Content-Type为multipart/form-data
  if (disputeData instanceof FormData) {
    return axios.post('/score-disputes', disputeData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
  // 否则使用默认的application/json
  return axios.post('/score-disputes', disputeData)
}

// 审核成绩异议
export const auditDispute = (disputeId, disputeData) => {
  return axios.put(`/score-disputes/${disputeId}`, disputeData)
}

// 根据异议ID查询成绩异议
export const getDisputeById = (disputeId) => {
  return axios.get(`/score-disputes/${disputeId}`)
}

// 根据学生查询成绩异议列表
export const getDisputesByStudentId = (studentId) => {
  return axios.get(`/score-disputes/student/${studentId}`)
}

// 根据课程查询成绩异议列表
export const getDisputesByCourseId = (courseId) => {
  return axios.get(`/score-disputes/course/${courseId}`)
}

// 根据教师查询成绩异议列表
export const getDisputesByTeacherId = (teacherId) => {
  return axios.get(`/score-disputes/teacher/${teacherId}`)
}

// 查询所有成绩异议列表
export const getAllDisputes = () => {
  return axios.get('/score-disputes')
}

// 根据审核状态查询成绩异议列表
export const getDisputesByAuditStatus = (auditStatus) => {
  return axios.get(`/score-disputes/status/${auditStatus}`)
}
