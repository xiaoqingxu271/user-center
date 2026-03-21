<template>
  <div class="login-container">
    <div class="animated-background">
      <div class="floating-element cat" style="left: 5%; top: 10%; animation-delay: 0s;">
        <svg viewBox="0 0 100 100" width="60" height="60">
          <circle cx="50" cy="55" r="35" fill="#FFB6C1"/>
          <path d="M25 30 L35 45 L15 45 Z" fill="#FFB6C1"/>
          <path d="M75 30 L65 45 L85 45 Z" fill="#FFB6C1"/>
          <circle cx="40" cy="50" r="5" fill="#333"/>
          <circle cx="60" cy="50" r="5" fill="#333"/>
          <ellipse cx="50" cy="62" rx="4" ry="3" fill="#FF69B4"/>
          <path d="M45 70 Q50 75 55 70" stroke="#333" stroke-width="2" fill="none"/>
        </svg>
      </div>
      <div class="floating-element dog" style="left: 85%; top: 15%; animation-delay: 1s;">
        <svg viewBox="0 0 100 100" width="70" height="70">
          <circle cx="50" cy="50" r="30" fill="#DEB887"/>
          <ellipse cx="35" cy="35" rx="12" ry="18" fill="#DEB887"/>
          <ellipse cx="65" cy="35" rx="12" ry="18" fill="#DEB887"/>
          <circle cx="40" cy="45" r="4" fill="#333"/>
          <circle cx="60" cy="45" r="4" fill="#333"/>
          <ellipse cx="50" cy="58" rx="8" ry="5" fill="#8B4513"/>
          <path d="M42 65 Q50 72 58 65" stroke="#333" stroke-width="2" fill="none"/>
        </svg>
      </div>
      <div class="floating-element bird" style="left: 15%; top: 70%; animation-delay: 2s;">
        <svg viewBox="0 0 100 100" width="50" height="50">
          <ellipse cx="50" cy="50" rx="25" ry="20" fill="#87CEEB"/>
          <circle cx="60" cy="45" r="8" fill="#87CEEB"/>
          <circle cx="63" cy="43" r="2" fill="#333"/>
          <polygon points="70,45 85,48 70,51" fill="#FFA500"/>
          <path d="M30 50 Q20 40 25 30" stroke="#FFD700" stroke-width="3" fill="none"/>
          <path d="M70 50 Q80 40 75 30" stroke="#FFD700" stroke-width="3" fill="none"/>
        </svg>
      </div>
      <div class="floating-element rabbit" style="left: 75%; top: 75%; animation-delay: 1.5s;">
        <svg viewBox="0 0 100 100" width="55" height="55">
          <ellipse cx="50" cy="60" rx="28" ry="25" fill="#FFF0F5"/>
          <ellipse cx="38" cy="25" rx="8" ry="20" fill="#FFF0F5"/>
          <ellipse cx="62" cy="25" rx="8" ry="20" fill="#FFF0F5"/>
          <circle cx="42" cy="55" r="4" fill="#FF69B4"/>
          <circle cx="58" cy="55" r="4" fill="#FF69B4"/>
          <ellipse cx="50" cy="68" rx="5" ry="4" fill="#FFB6C1"/>
          <path d="M45 75 Q50 80 55 75" stroke="#FF69B4" stroke-width="2" fill="none"/>
        </svg>
      </div>
      <div class="floating-element person" style="left: 50%; top: 5%; animation-delay: 0.5s;">
        <svg viewBox="0 0 100 100" width="65" height="65">
          <circle cx="50" cy="30" r="20" fill="#FFDAB9"/>
          <path d="M30 25 Q50 10 70 25" fill="#8B4513"/>
          <circle cx="42" cy="28" r="3" fill="#333"/>
          <circle cx="58" cy="28" r="3" fill="#333"/>
          <path d="M45 35 Q50 40 55 35" stroke="#333" stroke-width="2" fill="none"/>
          <rect x="35" y="50" width="30" height="35" rx="5" fill="#4169E1"/>
          <line x1="35" y1="55" x2="20" y2="75" stroke="#FFDAB9" stroke-width="8" stroke-linecap="round"/>
          <line x1="65" y1="55" x2="80" y2="75" stroke="#FFDAB9" stroke-width="8" stroke-linecap="round"/>
        </svg>
      </div>
      <div class="floating-element star" style="left: 8%; top: 40%; animation-delay: 2.5s;">
        <svg viewBox="0 0 100 100" width="40" height="40">
          <polygon points="50,5 61,40 98,40 68,62 79,97 50,75 21,97 32,62 2,40 39,40" fill="#FFD700" opacity="0.8"/>
        </svg>
      </div>
      <div class="floating-element heart" style="left: 90%; top: 50%; animation-delay: 3s;">
        <svg viewBox="0 0 100 100" width="45" height="45">
          <path d="M50 90 C20 60 5 40 20 25 C35 10 50 25 50 25 C50 25 65 10 80 25 C95 40 80 60 50 90" fill="#FF6B6B" opacity="0.7"/>
        </svg>
      </div>
      <div class="floating-element cloud" style="left: 20%; top: 25%; animation-delay: 1.8s;">
        <svg viewBox="0 0 100 100" width="80" height="80">
          <ellipse cx="30" cy="60" rx="25" ry="18" fill="rgba(255,255,255,0.6)"/>
          <ellipse cx="55" cy="55" rx="30" ry="22" fill="rgba(255,255,255,0.6)"/>
          <ellipse cx="75" cy="65" rx="20" ry="15" fill="rgba(255,255,255,0.6)"/>
        </svg>
      </div>
      <div class="floating-element sun" style="left: 80%; top: 8%; animation-delay: 0.8s;">
        <svg viewBox="0 0 100 100" width="60" height="60">
          <circle cx="50" cy="50" r="20" fill="#FFA500" opacity="0.8"/>
          <g stroke="#FFA500" stroke-width="3" opacity="0.6">
            <line x1="50" y1="20" x2="50" y2="5"/>
            <line x1="50" y1="80" x2="50" y2="95"/>
            <line x1="20" y1="50" x2="5" y2="50"/>
            <line x1="80" y1="50" x2="95" y2="50"/>
            <line x1="29" y1="29" x2="18" y2="18"/>
            <line x1="71" y1="71" x2="82" y2="82"/>
            <line x1="29" y1="71" x2="18" y2="82"/>
            <line x1="71" y1="29" x2="82" y2="18"/>
          </g>
        </svg>
      </div>
    </div>
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
  position: relative;
  overflow: hidden;
}

@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.animated-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.floating-element {
  position: absolute;
  animation: float 6s ease-in-out infinite;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.15));
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  25% {
    transform: translateY(-20px) rotate(5deg);
  }
  50% {
    transform: translateY(-10px) rotate(-3deg);
  }
  75% {
    transform: translateY(-25px) rotate(3deg);
  }
}

.floating-element:hover {
  animation-play-state: paused;
  transform: scale(1.1);
  transition: transform 0.3s ease;
}

.cat {
  animation-duration: 7s;
}

.dog {
  animation-duration: 8s;
}

.bird {
  animation-duration: 5s;
}

.rabbit {
  animation-duration: 6.5s;
}

.person {
  animation-duration: 7.5s;
}

.star {
  animation-duration: 4s;
  animation-name: twinkle;
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.8;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 1;
    transform: scale(1.2) rotate(180deg);
  }
}

.heart {
  animation-duration: 5.5s;
  animation-name: heartbeat;
}

@keyframes heartbeat {
  0%, 100% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.1);
  }
  50% {
    transform: scale(1);
  }
  75% {
    transform: scale(1.15);
  }
}

.cloud {
  animation-duration: 9s;
  animation-name: drift;
}

@keyframes drift {
  0%, 100% {
    transform: translateX(0);
  }
  50% {
    transform: translateX(30px);
  }
}

.sun {
  animation-duration: 10s;
  animation-name: rotateSun;
}

@keyframes rotateSun {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.login-card {
  width: 100%;
  max-width: 25rem;
  padding: 2.5rem;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 1rem;
  box-shadow: 0 0.5rem 1.5rem rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
  animation: cardAppear 0.8s ease-out;
}

@keyframes cardAppear {
  0% {
    opacity: 0;
    transform: translateY(30px) scale(0.9);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.login-title {
  text-align: center;
  margin-bottom: 1.875rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  position: relative;
}

.login-title::after {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  margin: 10px auto 0;
  border-radius: 2px;
}

.login-form {
  margin-top: 1.25rem;
}

.login-button {
  width: 100%;
  margin-top: 0.625rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
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
  
  .floating-element {
    transform: scale(0.8);
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
  
  .floating-element {
    transform: scale(0.6);
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
  
  .floating-element {
    transform: scale(1.2);
  }
}
</style>
