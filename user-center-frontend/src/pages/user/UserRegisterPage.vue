<template>
  <div class="register-container">
    <div class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="0"
        class="register-form"
      >
        <el-form-item prop="userAccount">
          <el-input
            v-model="registerForm.userAccount"
            placeholder="请输入账号（11位数字）"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="userPassword">
          <el-input
            v-model="registerForm.userPassword"
            type="password"
            placeholder="请输入密码（6-8位字母、数字、下划线）"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="checkPassword">
          <el-input
            v-model="registerForm.checkPassword"
            type="password"
            placeholder="请再次输入密码"
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
            class="register-button"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
        <div class="register-footer">
          <span>已有账号？</span>
          <el-link type="primary" @click="goToLogin">立即登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import message from '@/utils/message'
import { userRegister } from '@/api/user'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

const validatePassword = (rule, value, callback) => {
  if (value !== registerForm.userPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { pattern: /^\d{11}$/, message: '账号必须为11位数字', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{6,8}$/, message: '密码长度为6-8位，只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userRegister({
          userAccount: registerForm.userAccount,
          userPassword: registerForm.userPassword
        })
        message.success('注册成功，请登录')
        router.push('/user/login')
      } catch (error) {
        console.error('注册失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/user/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 100%;
  max-width: 25rem;
  padding: 2.5rem;
  background: #fff;
  border-radius: 0.5rem;
  box-shadow: 0 0.25rem 0.75rem rgba(0, 0, 0, 0.15);
}

.register-title {
  text-align: center;
  margin-bottom: 1.875rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
}

.register-form {
  margin-top: 1.25rem;
}

.register-button {
  width: 100%;
  margin-top: 0.625rem;
}

.register-footer {
  text-align: center;
  margin-top: 1.25rem;
  font-size: 0.875rem;
  color: #666;
}

.register-footer .el-link {
  margin-left: 0.3125rem;
}

@media screen and (max-width: 768px) {
  .register-card {
    padding: 2rem;
    max-width: 22rem;
  }
  
  .register-title {
    font-size: 1.375rem;
    margin-bottom: 1.5rem;
  }
}

@media screen and (max-width: 480px) {
  .register-container {
    padding: 0.75rem;
  }
  
  .register-card {
    padding: 1.5rem;
    max-width: 100%;
  }
  
  .register-title {
    font-size: 1.25rem;
    margin-bottom: 1.25rem;
  }
  
  .register-form {
    margin-top: 1rem;
  }
}

@media screen and (min-width: 1920px) {
  .register-card {
    max-width: 28rem;
    padding: 3rem;
  }
  
  .register-title {
    font-size: 1.75rem;
    margin-bottom: 2rem;
  }
}
</style>
