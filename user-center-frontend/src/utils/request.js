import axios from 'axios'
import message from '@/utils/message'
import { clearAuthAndRedirect } from '@/utils/auth'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const data = response.data
    if (data.code === 0) {
      return data
    } else {
      const errorCode = data.code
      const errorMessage = data.message || '请求失败'
      
      if (errorCode === 40100 || errorCode === 40101 || errorCode === 40300) {
        message.error('登录已过期或无权限，请重新登录')
        clearAuthAndRedirect()
      } else {
        message.error(errorMessage)
      }
      
      return Promise.reject(new Error(errorMessage))
    }
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      
      if (status === 401 || status === 403) {
        message.error('登录已过期或无权限，请重新登录')
        clearAuthAndRedirect()
      } else {
        message.error(data?.message || error.response.data?.message || '请求失败')
      }
    } else {
      message.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request
