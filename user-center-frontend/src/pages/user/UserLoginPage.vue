<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">用户登录</h2>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="0"
        class="login-form"
      >
        <el-form-item prop="userAccount">
          <el-input
            v-model="loginForm.userAccount"
            placeholder="请输入账号"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="userPassword">
          <el-input
            v-model="loginForm.userPassword"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        <div class="login-footer">
          <span>还没有账号？</span>
          <el-link type="primary" @click="goToRegister">立即注册</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import message from '@/utils/message'
import { userLogin } from '@/api/user'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userAccount: '',
  userPassword: ''
})

const loginRules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { pattern: /^\d{11}$/, message: '账号必须为11位数字', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{6,8}$/, message: '密码长度为6-8位，只能包含字母、数字和下划线', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userLogin(loginForm)
        localStorage.setItem('token', res.data.token)
        const userInfo = res.data.userInfo
        console.log('登录响应:', res)
        console.log('userInfo:', userInfo)
        loginUserStore.setLoginUser(userInfo)
        message.success('登录成功')
        router.push('/home')
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/user/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 100%;
  max-width: 25rem;
  padding: 2.5rem;
  background: #fff;
  border-radius: 0.5rem;
  box-shadow: 0 0.25rem 0.75rem rgba(0, 0, 0, 0.15);
}

.login-title {
  text-align: center;
  margin-bottom: 1.875rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
}

.login-form {
  margin-top: 1.25rem;
}

.login-button {
  width: 100%;
  margin-top: 0.625rem;
}

.login-footer {
  text-align: center;
  margin-top: 1.25rem;
  font-size: 0.875rem;
  color: #666;
}

.login-footer .el-link {
  margin-left: 0.3125rem;
}

@media screen and (max-width: 768px) {
  .login-card {
    padding: 2rem;
    max-width: 22rem;
  }
  
  .login-title {
    font-size: 1.375rem;
    margin-bottom: 1.5rem;
  }
}

@media screen and (max-width: 480px) {
  .login-container {
    padding: 0.75rem;
  }
  
  .login-card {
    padding: 1.5rem;
    max-width: 100%;
  }
  
  .login-title {
    font-size: 1.25rem;
    margin-bottom: 1.25rem;
  }
  
  .login-form {
    margin-top: 1rem;
  }
}

@media screen and (min-width: 1920px) {
  .login-card {
    max-width: 28rem;
    padding: 3rem;
  }
  
  .login-title {
    font-size: 1.75rem;
    margin-bottom: 2rem;
  }
}
</style>
