<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="dashboard-header">
        <div class="header-left">
          <el-icon class="logo-icon"><School /></el-icon>
          <h1>课程管理系统</h1>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click">
            <span class="user-info">
              <el-avatar :size="36" :icon="User" :bg-color="userAvatarColor" />
              <span class="user-name">{{ userInfo.name || userInfo.username }}</span>
              <el-tag size="small" type="info" class="role-tag">{{ userInfo.role }}</el-tag>
              <el-icon class="arrow-icon"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-container>
        <!-- 左侧导航菜单 -->
        <el-aside width="220px" class="dashboard-aside">
          <el-menu
            :default-active="activeMenu"
            class="main-menu"
            router
            @select="handleMenuSelect"
            background-color="#ffffff"
            text-color="#303133"
            active-text-color="#667eea"
            :unique-opened="true"
            :collapse-transition="true"
          >
            <!-- 成绩管理菜单 -->
            <el-sub-menu index="score">
              <template #title>
                <el-icon><Document /></el-icon>
                <span>成绩管理</span>
              </template>
              <el-menu-item index="/score/input">
                <el-icon><EditPen /></el-icon>
                <span>成绩录入</span>
              </el-menu-item>
              <el-menu-item index="/score/query">
                <el-icon><Search /></el-icon>
                <span>成绩查询</span>
              </el-menu-item>
              <el-menu-item index="/score/stat">
                <el-icon><DataAnalysis /></el-icon>
                <span>成绩统计</span>
              </el-menu-item>
              <el-menu-item index="/score/dispute">
                <el-icon><Warning /></el-icon>
                <span>成绩异议</span>
              </el-menu-item>
              <el-menu-item index="/score/dispute/audit">
                <el-icon><Check /></el-icon>
                <span>异议审核</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        
        <!-- 右侧内容区域 -->
        <el-main class="dashboard-main">
          <div class="content-wrapper">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  School, ArrowDown, SwitchButton, Document, 
  EditPen, Search, DataAnalysis, Warning, Check 
} from '@element-plus/icons-vue'

export default {
  name: 'DashboardView',
  components: {
    School,
    ArrowDown,
    SwitchButton,
    Document,
    EditPen,
    Search,
    DataAnalysis,
    Warning,
    Check
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    
    const activeMenu = ref('/score/input')
    
    const userInfo = reactive({
      username: '',
      name: '',
      role: ''
    })
    
    // 根据用户名生成随机头像背景色
    const userAvatarColor = computed(() => {
      const colors = ['#667eea', '#764ba2', '#f093fb', '#4facfe', '#43e97b', '#fa709a', '#fee140']
      if (!userInfo.username) return colors[0]
      const hash = userInfo.username.split('').reduce((acc, char) => {
        return char.charCodeAt(0) + ((acc << 5) - acc)
      }, 0)
      return colors[Math.abs(hash) % colors.length]
    })
    
    // 处理菜单选择
    const handleMenuSelect = (key) => {
      activeMenu.value = key
    }
    
    // 处理退出登录
    const handleLogout = () => {
      // 清除本地存储的token和用户信息
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 跳转到登录页
      router.push('/')
      ElMessage.success('退出登录成功')
    }
    
    // 初始化用户信息
    onMounted(() => {
      const userInfoStr = localStorage.getItem('userInfo')
      if (userInfoStr) {
        const parsedUserInfo = JSON.parse(userInfoStr)
        userInfo.username = parsedUserInfo.username
        userInfo.name = parsedUserInfo.name || parsedUserInfo.username
        userInfo.role = parsedUserInfo.role
      }
      
      // 设置默认激活菜单
      if (route.path) {
        activeMenu.value = route.path
      }
    })
    
    return {
      activeMenu,
      userInfo,
      userAvatarColor,
      handleMenuSelect,
      handleLogout
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.dashboard-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: white;
}

.header-left h1 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: white;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 24px;
  transition: all 0.3s ease;
  background-color: rgba(255, 255, 255, 0.1);
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.user-name {
  margin-left: 12px;
  font-weight: 500;
  color: white;
}

.role-tag {
  margin-left: 12px;
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
}

.arrow-icon {
  margin-left: 8px;
  font-size: 14px;
  color: white;
  transition: transform 0.3s ease;
}

.user-info:hover .arrow-icon {
  transform: translateY(2px);
}

.dashboard-aside {
  background-color: #ffffff;
  border-right: 1px solid #e4e7ed;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
}

.main-menu {
  border-right: none;
  height: 100%;
}

:deep(.el-menu-item), :deep(.el-sub-menu__title) {
  height: 52px;
  line-height: 52px;
  font-size: 15px;
  padding: 0 20px;
  border-radius: 8px;
  margin: 8px 12px;
  transition: all 0.3s ease;
}

:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) {
  background-color: rgba(102, 126, 234, 0.1) !important;
  color: #667eea !important;
  transform: translateX(4px);
}

:deep(.el-menu-item.is-active) {
  background-color: rgba(102, 126, 234, 0.2) !important;
  color: #667eea !important;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

:deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 0 4px 4px 0;
}

:deep(.el-sub-menu .el-menu-item) {
  padding-left: 48px !important;
}

:deep(.el-sub-menu .el-menu-item::before) {
  left: 28px;
}

.dashboard-main {
  padding: 24px;
  overflow-y: auto;
  background-color: #f5f7fa;
}

.content-wrapper {
  background-color: #ffffff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  min-height: calc(100vh - 160px);
}

/* 滚动条样式优化 */
.dashboard-main::-webkit-scrollbar,
.dashboard-aside::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.dashboard-main::-webkit-scrollbar-track,
.dashboard-aside::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.dashboard-main::-webkit-scrollbar-thumb,
.dashboard-aside::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.dashboard-main::-webkit-scrollbar-thumb:hover,
.dashboard-aside::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
