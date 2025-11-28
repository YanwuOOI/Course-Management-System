<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="dashboard-header">
        <div class="header-left">
          <h1>课程管理系统</h1>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" icon="User" />
              <span>{{ userInfo.name || userInfo.username }}</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-container>
        <!-- 左侧导航菜单 -->
        <el-aside width="200px" class="dashboard-aside">
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            router
            @select="handleMenuSelect"
          >
            <!-- 成绩管理菜单 -->
            <el-sub-menu index="score">
              <template #title>
                <el-icon><document /></el-icon>
                <span>成绩管理</span>
              </template>
              <el-menu-item index="/score/input">成绩录入</el-menu-item>
              <el-menu-item index="/score/query">成绩查询</el-menu-item>
              <el-menu-item index="/score/stat">成绩统计</el-menu-item>
              <el-menu-item index="/score/dispute">成绩异议</el-menu-item>
              <el-menu-item index="/score/dispute/audit">异议审核</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        
        <!-- 右侧内容区域 -->
        <el-main class="dashboard-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

export default {
  name: 'Dashboard',
  setup() {
    const router = useRouter()
    const route = useRoute()
    
    const activeMenu = ref('/score/input')
    
    const userInfo = reactive({
      username: '',
      name: '',
      role: ''
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
}

.dashboard-header {
  background-color: #1E88E5;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left h1 {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-info span {
  margin-left: 8px;
}

.dashboard-aside {
  background-color: #f0f2f5;
  border-right: 1px solid #e4e7ed;
}

.dashboard-main {
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}
</style>
