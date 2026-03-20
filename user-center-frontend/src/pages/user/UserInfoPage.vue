<template>
  <div class="user-info-page">
    <div class="page-container">
      <div class="left-sidebar">
        <div class="profile-card">
          <div class="avatar-container">
            <el-avatar :size="120" :src="avatarPreview" @error="handleAvatarError" class="main-avatar" />
            <el-tag v-if="userForm.userRole === 'ADMIN'" type="danger" effect="dark" class="role-tag">
              管理员
            </el-tag>
            <el-tag v-else type="primary" effect="dark" class="role-tag">
              普通用户
            </el-tag>
          </div>
          
          <div class="user-summary">
            <h2 class="user-name">{{ userForm.userName || '未设置' }}</h2>
            <p class="user-account">@{{ userForm.userAccount }}</p>
            <div class="status-badge">
              <el-tag :type="userForm.userStatus === 0 ? 'success' : 'danger'" effect="light">
                {{ userForm.userStatus === 0 ? '正常' : '已禁用' }}
              </el-tag>
            </div>
          </div>

          <div class="quick-stats">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">角色</div>
                <div class="stat-value">{{ roleText }}</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">注册时间</div>
                <div class="stat-value">{{ formatDate(userForm.createTime) }}</div>
              </div>
            </div>
          </div>

          <el-button type="primary" @click="openEditDialog" class="edit-btn">
            <el-icon><Edit /></el-icon>
            编辑资料
          </el-button>
        </div>
      </div>

      <div class="right-content">
        <div class="info-card">
          <div class="card-header">
            <div class="header-title">
              <el-icon><User /></el-icon>
              基本信息
            </div>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">用户名</span>
              <span class="value">{{ userForm.userName || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">账号</span>
              <span class="value">{{ userForm.userAccount }}</span>
            </div>
            <div class="info-item">
              <span class="label">性别</span>
              <span class="value">{{ genderText }}</span>
            </div>
            <div class="info-item">
              <span class="label">角色</span>
              <span class="value">{{ roleText }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <div class="card-header">
            <div class="header-title">
              <el-icon><Setting /></el-icon>
              账户信息
            </div>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">账户状态</span>
              <el-tag :type="userForm.userStatus === 0 ? 'success' : 'danger'" size="small">
                {{ userForm.userStatus === 0 ? '正常' : '已禁用' }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="label">注册时间</span>
              <span class="value">{{ formatDate(userForm.createTime) }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <div class="card-header">
            <div class="header-title">
              <el-icon><Picture /></el-icon>
              头像信息
            </div>
          </div>
          <div class="avatar-info-section">
            <el-avatar :size="80" :src="avatarPreview" @error="handleAvatarError" />
            <div class="avatar-details">
              <div class="avatar-url-label">头像URL</div>
              <div class="avatar-url-value">{{ userForm.avatarUrl || '未设置头像' }}</div>
              <div class="avatar-tip">点击编辑资料可更换头像</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="500px" :close-on-click-modal="false">
      <el-form
        ref="formRef"
        :model="editForm"
        :rules="rules"
        label-width="80px"
        class="edit-form"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="editForm.userName" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio :value="0">男</el-radio>
            <el-radio :value="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="头像URL" prop="avatarUrl">
          <el-input v-model="editForm.avatarUrl" placeholder="请输入头像URL" />
          <div v-if="editForm.avatarUrl" class="avatar-preview-dialog">
            <el-avatar :size="80" :src="editForm.avatarUrl" @error="handleAvatarError" />
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate" :loading="loading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import message from '@/utils/message'
import { Calendar, Edit, User, Setting, Picture } from '@element-plus/icons-vue'
import { getCurrentUserInfo, updateCurrentUserInfo } from '@/api/user'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const loginUserStore = useLoginUserStore()
const formRef = ref(null)
const editDialogVisible = ref(false)
const loading = ref(false)
const originalForm = ref({})

const userForm = reactive({
  id: null,
  userAccount: '',
  userName: '',
  gender: 0,
  avatarUrl: '',
  userStatus: 0,
  userRole: '',
  createTime: ''
})

const editForm = reactive({
  userName: '',
  gender: 0,
  avatarUrl: ''
})

const rules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}

const roleText = computed(() => {
  return userForm.userRole === 'ADMIN' ? '管理员' : '普通用户'
})

const genderText = computed(() => {
  return userForm.gender === 0 ? '男' : '女'
})

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const avatarPreview = computed(() => {
  if (!userForm.avatarUrl) {
    return defaultAvatar
  }
  return userForm.avatarUrl
})

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const loadUserInfo = async () => {
  try {
    const res = await getCurrentUserInfo()
    userForm.id = res.data.id
    userForm.userAccount = res.data.account
    userForm.userName = res.data.username
    userForm.gender = res.data.gender
    userForm.avatarUrl = res.data.avatar
    userForm.userStatus = res.data.status
    userForm.userRole = res.data.role
    userForm.createTime = res.data.createTime
    originalForm.value = { ...res.data }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const openEditDialog = () => {
  editForm.userName = userForm.userName
  editForm.gender = userForm.gender
  editForm.avatarUrl = userForm.avatarUrl
  editDialogVisible.value = true
}

const handleUpdate = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateCurrentUserInfo({
          username: editForm.userName,
          gender: editForm.gender,
          avatar: editForm.avatarUrl
        })
        message.success('修改成功')
        editDialogVisible.value = false
        await loadUserInfo()
        loginUserStore.setLoginUser({
          id: userForm.id,
          username: userForm.userName,
          avatar: userForm.avatarUrl
        })
      } catch (error) {
        console.error('修改失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.user-info-page {
  width: 100%;
  min-height: 100vh;
  padding: 1.25rem;
  box-sizing: border-box;
  background: #f5f7fa;
}

.page-container {
  width: 100%;
  max-width: 87.5rem;
  margin: 0 auto;
  display: flex;
  gap: 1.25rem;
  align-items: stretch;
}

.left-sidebar {
  width: 18.75rem;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.profile-card {
  background: #fff;
  border-radius: 0.75rem;
  padding: 1.25rem;
  box-shadow: 0 0.125rem 0.5rem rgba(0, 0, 0, 0.06);
  text-align: center;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 0.75rem;
}

.main-avatar {
  border: 0.1875rem solid #fff;
  box-shadow: 0 0.125rem 0.75rem rgba(0, 0, 0, 0.1);
}

.role-tag {
  position: absolute;
  bottom: 0;
  right: 0;
  font-size: 0.75rem;
  padding: 0.25rem 0.625rem;
  border-radius: 0.75rem;
  box-shadow: 0 0.125rem 0.5rem rgba(0, 0, 0, 0.15);
  border: 0.125rem solid #fff;
  transform: translate(15%, 15%);
}

.user-summary {
  margin-bottom: 1rem;
}

.user-name {
  margin: 0 0 0.25rem 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a1a1a;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-account {
  margin: 0 0 0.625rem 0;
  font-size: 0.8125rem;
  color: #666;
}

.status-badge {
  display: flex;
  justify-content: center;
}

.quick-stats {
  display: flex;
  flex-direction: column;
  gap: 0.625rem;
  margin-bottom: 1rem;
  padding: 0.875rem;
  background: #f8f9fa;
  border-radius: 0.625rem;
  flex: 1;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.625rem;
}

.stat-icon {
  width: 2.25rem;
  height: 2.25rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1rem;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
  text-align: left;
  min-width: 0;
}

.stat-label {
  font-size: 0.6875rem;
  color: #999;
  margin-bottom: 0.125rem;
}

.stat-value {
  font-size: 0.8125rem;
  font-weight: 500;
  color: #1a1a1a;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.edit-btn {
  width: 100%;
  border-radius: 0.375rem;
  padding: 0.625rem 1.25rem;
  margin-top: auto;
}

.right-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-card {
  background: #fff;
  border-radius: 0.625rem;
  padding: 1.25rem;
  box-shadow: 0 0.125rem 0.375rem rgba(0, 0, 0, 0.04);
}

.card-header {
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 0.0625rem solid #f0f0f0;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 0.375rem;
  font-size: 1rem;
  font-weight: 600;
  color: #1a1a1a;
}

.header-title .el-icon {
  font-size: 1.125rem;
  color: #667eea;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.75rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 0.5rem;
}

.info-item .label {
  font-size: 0.75rem;
  color: #666;
  font-weight: 500;
}

.info-item .value {
  font-size: 0.875rem;
  color: #1a1a1a;
  font-weight: 500;
  word-break: break-all;
}

.avatar-info-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 0.625rem;
}

.avatar-details {
  flex: 1;
  min-width: 0;
}

.avatar-url-label {
  font-size: 0.6875rem;
  color: #999;
  margin-bottom: 0.25rem;
}

.avatar-url-value {
  font-size: 0.8125rem;
  color: #1a1a1a;
  margin-bottom: 0.375rem;
  word-break: break-all;
  line-height: 1.4;
}

.avatar-tip {
  font-size: 0.6875rem;
  color: #999;
}

.edit-form {
  padding: 1rem 0;
}

.avatar-preview-dialog {
  margin-top: 0.625rem;
  display: flex;
  justify-content: center;
  padding: 0.625rem;
  background: #f5f7fa;
  border-radius: 0.375rem;
}

@media screen and (max-width: 1200px) {
  .page-container {
    max-width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .user-info-page {
    padding: 1rem;
  }
  
  .page-container {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .left-sidebar {
    width: 100%;
    max-width: 25rem;
    margin: 0 auto;
  }
  
  .profile-card {
    flex: none;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .avatar-info-section {
    flex-direction: column;
    text-align: center;
  }
  
  .quick-stats {
    padding: 1rem;
  }
}

@media screen and (max-width: 480px) {
  .user-info-page {
    padding: 0.75rem;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .avatar-info-section {
    flex-direction: column;
  }
  
  .quick-stats {
    padding: 0.75rem;
  }
}

@media screen and (min-width: 1920px) {
  .user-info-page {
    padding: 1.5rem;
  }
  
  .page-container {
    gap: 1.5rem;
  }
  
  .left-sidebar {
    width: 21.25rem;
  }
  
  .profile-card {
    padding: 1.5rem;
  }
  
  .info-card {
    padding: 1.5rem;
  }
  
  .info-grid {
    gap: 1rem;
  }
  
  .info-item {
    padding: 1rem;
  }
  
  .stat-icon {
    width: 2.5rem;
    height: 2.5rem;
  }
}
</style>