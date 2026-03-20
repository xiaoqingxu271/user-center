<template>
  <div class="global-header">
    <div class="header-left">
      <h2 class="logo">用户中心</h2>
    </div>
    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          <el-avatar :size="32" :src="avatarUrl" @error="handleAvatarError" />
          <span class="username">{{ loginUser?.username || '用户' }}</span>
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="info">个人信息</el-dropdown-item>
            <el-dropdown-item command="password">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import message from '@/utils/message'
import { clearAuthAndRedirect } from '@/utils/auth'
import { ElMessageBox } from 'element-plus'
import { userLogout } from '@/api/user'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loginUser = computed(() => loginUserStore.getLoginUser())

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

const handleCommand = async (command) => {
  switch (command) {
    case 'info':
      router.push('/user/info')
      break
    case 'password':
      router.push('/user/password')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await userLogout()
        clearAuthAndRedirect()
        message.success('退出登录成功')
      } catch (error) {
        if (error !== 'cancel') {
          message.error('退出登录失败')
        }
      }
      break
  }
}
</script>

<style scoped>
.global-header {
  width: 100%;
  height: 60px;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left .logo {
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
  margin: 0;
}

.header-right .user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.header-right .user-info:hover {
  background-color: #f5f5f5;
}

.header-right .username {
  margin-left: 8px;
  margin-right: 4px;
  font-size: 14px;
  color: #333;
}
</style>
