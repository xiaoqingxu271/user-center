import { createRouter, createWebHistory } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import BasicLayout from '@/layouts/BasicLayout.vue'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/user/login',
    name: 'UserLogin',
    component: () => import('@/pages/user/UserLoginPage.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/user/register',
    name: 'UserRegister',
    component: () => import('@/pages/user/UserRegisterPage.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    component: BasicLayout,
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/pages/HomePage.vue'),
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: 'user/info',
        name: 'UserInfo',
        component: () => import('@/pages/user/UserInfoPage.vue'),
        meta: { title: '个人信息', requiresAuth: true }
      },
      {
        path: 'user/password',
        name: 'UserPassword',
        component: () => import('@/pages/user/UserPasswordPage.vue'),
        meta: { title: '修改密码', requiresAuth: true }
      },
      {
        path: 'admin/users',
        name: 'AdminUsers',
        component: () => import('@/pages/admin/UserManagePage.vue'),
        meta: { title: '用户管理', requiresAuth: true, requiresAdmin: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const loginUserStore = useLoginUserStore()
  const loginUser = loginUserStore.getLoginUser()
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth) {
    if (!token || !loginUser) {
      next('/user/login')
      return
    }
    if (to.meta.requiresAdmin && loginUser.role !== 'ADMIN') {
      next('/home')
      return
    }
  }

  if ((to.path === '/user/login' || to.path === '/user/register') && token) {
    next('/home')
    return
  }

  next()
})

export default router
