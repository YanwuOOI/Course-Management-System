import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    children: [
      // 成绩管理相关路由
      {
        path: '/score/input',
        name: 'ScoreInput',
        component: () => import('../views/score/ScoreInput.vue')
      },
      {
        path: '/score/query',
        name: 'ScoreQuery',
        component: () => import('../views/score/ScoreQuery.vue')
      },
      {
        path: '/score/stat',
        name: 'ScoreStat',
        component: () => import('../views/score/ScoreStat.vue')
      },
      {
        path: '/score/dispute',
        name: 'ScoreDispute',
        component: () => import('../views/score/ScoreDispute.vue')
      },
      {
        path: '/score/dispute/audit',
        name: 'ScoreDisputeAudit',
        component: () => import('../views/score/ScoreDisputeAudit.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  // 检查是否登录
  const isLogin = localStorage.getItem('token')
  if (to.path !== '/' && !isLogin) {
    // 未登录，跳转到登录页
    next('/')
  } else {
    next()
  }
})

export default router
