<template>
  <div class="home-page">
    <el-card class="welcome-card">
      <div class="welcome-content">
        <el-avatar :size="90" :src="avatarUrl" @error="handleAvatarError" />
        <div class="welcome-text">
          <h2 v-if="loginUser?.username">欢迎，{{ loginUser.username }}！</h2>
          <h2 v-else>欢迎回来！</h2>
          <p>欢迎使用用户中心系统</p>
        </div>
      </div>
    </el-card>
    
    <el-row :gutter="24" class="info-cards">
      <el-col :span="8" v-if="isAdmin">
        <el-card class="info-card" @click="goToUserManage">
          <div class="card-content">
            <div class="icon-wrapper">
              <el-icon class="card-icon"><user /></el-icon>
            </div>
            <div class="card-text">
              <h3>用户管理</h3>
              <p>管理用户信息、权限等</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="isAdmin ? 8 : 12">
        <el-card class="info-card" @click="goToUserInfo">
          <div class="card-content">
            <div class="icon-wrapper">
              <el-icon class="card-icon"><document /></el-icon>
            </div>
            <div class="card-text">
              <h3>个人信息</h3>
              <p>查看和修改个人信息</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="isAdmin ? 8 : 12">
        <el-card class="info-card" @click="goToPassword">
          <div class="card-content">
            <div class="icon-wrapper">
              <el-icon class="card-icon"><lock /></el-icon>
            </div>
            <div class="card-text">
              <h3>安全设置</h3>
              <p>修改密码、保护账号</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { User, Document, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loginUser = computed(() => loginUserStore.getLoginUser())
const isAdmin = computed(() => loginUser.value?.role === 'ADMIN')

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const avatarUrl = computed(() => {
  if (!loginUser.value?.avatar) {
    return defaultAvatar
  }
  return loginUser.value.avatar
})

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}

const goToUserManage = () => {
  router.push('/admin/users')
}

const goToUserInfo = () => {
  router.push('/user/info')
}

const goToPassword = () => {
  router.push('/user/password')
}
</script>

<style scoped>
.home-page {
  width: 100%;
  max-width: 87.5rem;
  margin: 0 auto;
  padding: 0 1.25rem;
  min-height: calc(100vh - 7.5rem);
  display: flex;
  flex-direction: column;
}

.welcome-card {
  margin-bottom: 2rem;
  border-radius: 1rem;
  box-shadow: 0 0.125rem 0.75rem rgba(0, 0, 0, 0.06);
}

.welcome-content {
  display: flex;
  align-items: center;
  gap: 1.75rem;
  padding: 2rem 2.25rem;
}

.welcome-text h2 {
  font-size: 1.75rem;
  color: #303133;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

.welcome-text p {
  font-size: 0.9375rem;
  color: #909399;
  margin: 0;
}

.info-cards {
  margin-top: 1.5rem;
  flex: 1;
}

.info-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 1rem;
  border: 0.0625rem solid #ebeef5;
  box-shadow: 0 0.125rem 0.5rem rgba(0, 0, 0, 0.04);
  height: 100%;
}

.info-card:hover {
  transform: translateY(-0.375rem);
  box-shadow: 0 0.5rem 1.5rem rgba(0, 0, 0, 0.12);
  border-color: transparent;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1.5rem;
  text-align: center;
  height: 100%;
  min-height: 17.5rem;
}

.icon-wrapper {
  width: 5rem;
  height: 5rem;
  border-radius: 1.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 0.25rem 0.75rem rgba(102, 126, 234, 0.25);
}

.info-card:nth-child(2) .icon-wrapper {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  box-shadow: 0 0.25rem 0.75rem rgba(245, 87, 108, 0.25);
}

.info-card:nth-child(3) .icon-wrapper {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  box-shadow: 0 0.25rem 0.75rem rgba(79, 172, 254, 0.25);
}

.card-icon {
  font-size: 2.5rem;
  color: #fff;
}

.card-text h3 {
  font-size: 1.25rem;
  color: #303133;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

.card-text p {
  font-size: 0.875rem;
  color: #909399;
  margin: 0;
  line-height: 1.6;
}

@media screen and (max-width: 1200px) {
  .home-page {
    max-width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
    padding: 1.5rem;
  }
  
  .welcome-text h2 {
    font-size: 1.5rem;
  }
  
  .welcome-text p {
    font-size: 0.875rem;
  }
  
  .card-content {
    padding: 2rem 1rem;
    min-height: 15rem;
  }
  
  .icon-wrapper {
    width: 4rem;
    height: 4rem;
    border-radius: 1rem;
    margin-bottom: 1rem;
  }
  
  .card-icon {
    font-size: 2rem;
  }
  
  .card-text h3 {
    font-size: 1.125rem;
  }
  
  .card-text p {
    font-size: 0.8125rem;
  }
}

@media screen and (max-width: 480px) {
  .home-page {
    padding: 0 0.75rem;
  }
  
  .welcome-card {
    margin-bottom: 1.5rem;
  }
  
  .welcome-content {
    padding: 1rem;
  }
  
  .card-content {
    padding: 1.5rem 0.75rem;
    min-height: 12rem;
  }
  
  .icon-wrapper {
    width: 3.5rem;
    height: 3.5rem;
  }
  
  .card-icon {
    font-size: 1.75rem;
  }
}

@media screen and (min-width: 1920px) {
  .welcome-content {
    padding: 2.5rem 3rem;
    gap: 2rem;
  }
  
  .welcome-text h2 {
    font-size: 2rem;
  }
  
  .welcome-text p {
    font-size: 1rem;
  }
  
  .card-content {
    padding: 3.5rem 2rem;
    min-height: 20rem;
  }
  
  .icon-wrapper {
    width: 6rem;
    height: 6rem;
    margin-bottom: 1.75rem;
  }
  
  .card-icon {
    font-size: 3rem;
  }
  
  .card-text h3 {
    font-size: 1.5rem;
  }
  
  .card-text p {
    font-size: 1rem;
  }
}
</style>
