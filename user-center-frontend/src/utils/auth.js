import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

export const clearAuthAndRedirect = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  const loginUserStore = useLoginUserStore()
  loginUserStore.clearLoginUser()
  router.push('/user/login')
}

export const isTokenExpired = () => {
  const token = localStorage.getItem('token')
  return !token
}

export const isAuthenticated = () => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  return !!(token && userInfo)
}
