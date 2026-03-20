<template>
  <div class="password-page">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <el-icon class="header-icon"><Lock /></el-icon>
          <span class="header-title">修改密码</span>
        </div>
      </template>
      
      <div class="password-content">
        <div class="password-tips">
          <el-icon class="tips-icon"><InfoFilled /></el-icon>
          <span>为了您的账户安全，建议定期修改密码。密码长度为6-8位，只能包含字母、数字和下划线。</span>
        </div>
        
        <el-form
          ref="formRef"
          :model="passwordForm"
          :rules="rules"
          label-width="120px"
          class="password-form"
        >
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入当前使用的密码"
              show-password
              size="large"
              clearable
            />
          </el-form-item>
          
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码（6-8位字母、数字、下划线）"
              show-password
              size="large"
              clearable
            />
          </el-form-item>
          
          <el-form-item label="确认新密码" prop="checkPassword">
            <el-input
              v-model="passwordForm.checkPassword"
              type="password"
              placeholder="请再次输入新密码以确认"
              show-password
              size="large"
              clearable
            />
          </el-form-item>
          
          <el-form-item class="form-actions">
            <el-button 
              type="primary" 
              @click="handleResetPassword" 
              :loading="loading"
              size="large"
              class="submit-btn"
            >
              <el-icon class="btn-icon"><Check /></el-icon>
              提交修改
            </el-button>
            <el-button 
              @click="handleCancel" 
              size="large"
              class="cancel-btn"
            >
              <el-icon class="btn-icon"><Close /></el-icon>
              取消
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import message from '@/utils/message'
import { clearAuthAndRedirect } from '@/utils/auth'
import { Lock, InfoFilled, Check, Close } from '@element-plus/icons-vue'
import { resetPassword } from '@/api/user'
import { useLoginUserStore } from '@/stores/useLoginUserStore'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const formRef = ref(null)
const loading = ref(false)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  checkPassword: ''
})

const validatePassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{6,8}$/, message: '密码长度为6-8位，只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}

const handleResetPassword = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await resetPassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        message.success('密码修改成功，请重新登录')
        clearAuthAndRedirect()
        setTimeout(() => {
          router.push('/user/login')
        }, 1000)
      } catch (error) {
        console.error('修改密码失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleCancel = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.checkPassword = ''
  formRef.value?.clearValidate()
}
</script>

<style scoped>
.password-page {
  width: 100%;
  max-width: 50rem;
  margin: 0 auto;
  padding: 1.25rem;
  background-color: #ffffff;
  border-radius: 0.5rem;
}

.password-card {
  box-shadow: none;
  border: none;
  border-radius: 0;
}

.password-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1.25rem 1.5rem;
  border-bottom: none;
  border-radius: 0.5rem 0.5rem 0 0;
}

.password-card :deep(.el-card__body) {
  padding: 0;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.header-icon {
  font-size: 1.5rem;
  color: #ffffff;
}

.header-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #ffffff;
}

.password-content {
  padding: 2rem 2.5rem;
}

.password-tips {
  display: flex;
  align-items: flex-start;
  gap: 0.625rem;
  padding: 1rem 1.25rem;
  background-color: #f0f9ff;
  border-left: 0.25rem solid #409eff;
  border-radius: 0.25rem;
  margin-bottom: 2rem;
  font-size: 0.875rem;
  color: #606266;
  line-height: 1.6;
}

.tips-icon {
  font-size: 1.125rem;
  color: #409eff;
  flex-shrink: 0;
  margin-top: 0.125rem;
}

.password-form {
  padding: 0;
}

.password-form :deep(.el-form-item) {
  margin-bottom: 1.75rem;
}

.password-form :deep(.el-form-item__label) {
  font-size: 0.9375rem;
  font-weight: 500;
  color: #303133;
}

.password-form :deep(.el-input__wrapper) {
  padding: 0.75rem 1rem;
  border-radius: 0.375rem;
  transition: all 0.3s;
}

.password-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 0.0625rem #c0c4cc inset;
}

.password-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 0.0625rem #409eff inset;
}

.password-form :deep(.el-input__inner) {
  font-size: 0.9375rem;
}

.form-actions {
  margin-top: 2.5rem;
  margin-bottom: 0;
}

.form-actions :deep(.el-form-item__content) {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.submit-btn,
.cancel-btn {
  min-width: 8.75rem;
  padding: 0.75rem 2rem;
  font-size: 0.9375rem;
  border-radius: 0.375rem;
  transition: all 0.3s;
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.submit-btn:hover {
  transform: translateY(-0.125rem);
  box-shadow: 0 0.25rem 0.75rem rgba(102, 126, 234, 0.4);
}

.cancel-btn {
  border-color: #dcdfe6;
  color: #606266;
}

.cancel-btn:hover {
  background-color: #f5f7fa;
  border-color: #c0c4cc;
}

.btn-icon {
  margin-right: 0.375rem;
}

@media screen and (max-width: 768px) {
  .password-page {
    padding: 1rem;
    max-width: 100%;
  }
  
  .password-content {
    padding: 1.5rem 1.75rem;
  }
  
  .password-tips {
    padding: 0.875rem 1rem;
    margin-bottom: 1.5rem;
    font-size: 0.8125rem;
  }
  
  .password-form :deep(.el-form-item) {
    margin-bottom: 1.5rem;
  }
  
  .form-actions {
    margin-top: 2rem;
  }
  
  .form-actions :deep(.el-form-item__content) {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .submit-btn,
  .cancel-btn {
    width: 100%;
    min-width: auto;
  }
}

@media screen and (max-width: 480px) {
  .password-page {
    padding: 0.75rem;
  }
  
  .password-content {
    padding: 1.25rem 1rem;
  }
  
  .password-tips {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .tips-icon {
    margin-top: 0;
  }
}

@media screen and (min-width: 1920px) {
  .password-page {
    max-width: 56.25rem;
    padding: 1.5rem;
  }
  
  .password-content {
    padding: 2.5rem 3rem;
  }
  
  .password-form :deep(.el-form-item) {
    margin-bottom: 2rem;
  }
  
  .password-form :deep(.el-form-item__label) {
    font-size: 1rem;
  }
  
  .password-form :deep(.el-input__wrapper) {
    padding: 0.875rem 1.125rem;
  }
  
  .password-form :deep(.el-input__inner) {
    font-size: 1rem;
  }
  
  .submit-btn,
  .cancel-btn {
    min-width: 10rem;
    padding: 0.875rem 2.5rem;
    font-size: 1rem;
  }
}
</style>
