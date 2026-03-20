<template>
  <div class="user-manage-page">
    <el-card class="main-card">
      <div class="card-content">
        <div class="search-section">
          <div class="search-form-wrapper">
            <el-form :model="searchForm" inline class="search-form">
              <el-form-item label="账号">
                <el-input v-model="searchForm.userAccount" placeholder="请输入账号" clearable style="width: 150px;" />
              </el-form-item>
              <el-form-item label="用户名">
                <el-input v-model="searchForm.userName" placeholder="请输入用户名" clearable style="width: 150px;" />
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="searchForm.userStatus" placeholder="请选择状态" clearable style="width: 120px;">
                  <el-option label="启用" :value="0" />
                  <el-option label="禁用" :value="1" />
                </el-select>
              </el-form-item>
              <el-form-item class="search-actions">
                <el-button type="primary" @click="handleSearchWithReset">搜索</el-button>
                <el-button @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="action-buttons">
            <el-button type="success" @click="handleAdd">添加用户</el-button>
            <el-button 
              type="danger" 
              @click="handleBatchDelete" 
              :disabled="selectedRows.length === 0"
            >
              批量删除 ({{ selectedRows.length }})
            </el-button>
          </div>
        </div>

        <el-divider class="section-divider" />

        <div class="table-section">
          <el-table
            :data="tableData"
            v-loading="loading"
            @selection-change="handleSelectionChange"
            :height="tableHeight"
            stripe
            border
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column label="头像" width="80">
              <template #default="{ row }">
                <el-avatar :size="40" :src="getAvatarUrl(row.avatar)" @error="handleAvatarError" />
              </template>
            </el-table-column>
            <el-table-column prop="account" label="账号" min-width="150" />
            <el-table-column prop="username" label="用户名" min-width="150" />
            <el-table-column prop="gender" label="性别" width="80">
              <template #default="{ row }">
                <span>{{ getGenderText(row.gender) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'primary'">
                  {{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <div class="status-indicator">
                  <span
                    class="status-dot"
                    :class="{ 'active': row.status === 0, 'inactive': row.status === 1 }"
                  ></span>
                  <span class="status-text">
                    {{ row.status === 0 ? '启用' : '禁用' }}
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="180" />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  :type="row.status === 0 ? 'warning' : 'success'"
                  size="small"
                  @click="handleToggleStatus(row)"
                >
                  {{ row.status === 0 ? '禁用' : '启用' }}
                </el-button>
                <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <div class="empty-text">暂无数据</div>
            </template>
          </el-table>
        </div>

        <el-divider class="section-divider" />

        <div class="pagination-section">
          <div class="pagination-container">
            <div class="pagination-info">
              <span class="total-text">共 {{ total }} 条</span>
            </div>
            <el-pagination
              v-model:current-page="searchForm.current"
              v-model:page-size="searchForm.pageSize"
              :page-sizes="[10, 20]"
              :total="total"
              layout="sizes, prev, pager, next"
              :page-size="searchForm.pageSize"
              @size-change="handleSearch"
              @current-change="handleSearch"
            >
              <template #sizes>
                <span>{{ searchForm.pageSize }}/页</span>
              </template>
              <template #prev>
                <span>上一页</span>
              </template>
              <template #next>
                <span>下一页</span>
              </template>
            </el-pagination>
          </div>
        </div>
      </div>
    </el-card>
    
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="账号" prop="userAccount" v-if="dialogType === 'add'">
          <el-input v-model="userForm.userAccount" placeholder="请输入账号（11位数字）" />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userForm.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword" v-if="dialogType === 'add'">
          <el-input v-model="userForm.userPassword" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="userForm.gender" placeholder="请选择性别">
            <el-option label="男" :value="0" />
            <el-option label="女" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="userRole">
          <el-select v-model="userForm.userRole" placeholder="请选择角色" style="width: 100%;">
            <el-option label="普通用户" value="USER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl">
          <div class="avatar-form-item">
            <el-avatar :size="60" :src="getAvatarUrl(userForm.avatarUrl)" @error="handleAvatarError" class="avatar-preview" />
            <el-input v-model="userForm.avatarUrl" placeholder="请输入头像URL" class="avatar-input" />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import message from '@/utils/message'
import { ElMessageBox } from 'element-plus'
import {
  getUserPage,
  addUser,
  modifyUser,
  deleteUser,
  startOrUpUser
} from '@/api/user'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedRows = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)

const searchForm = reactive({
  userAccount: '',
  userName: '',
  userStatus: null,
  current: 1,
  pageSize: 10
})

const userForm = reactive({
  id: null,
  userAccount: '',
  userName: '',
  userPassword: '',
  gender: 0,
  userRole: 'USER',
  avatarUrl: ''
})

const dialogTitle = computed(() => {
  return dialogType.value === 'add' ? '添加用户' : '编辑用户'
})

const tableHeight = computed(() => {
  const rowHeight = 44
  const headerHeight = 40
  return searchForm.pageSize * rowHeight + headerHeight
})

const rules = computed(() => {
  const baseRules = {
    userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
    userRole: [{ required: true, message: '请选择角色', trigger: 'change' }]
  }
  
  if (dialogType.value === 'add') {
    baseRules.userAccount = [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { pattern: /^\d{11}$/, message: '账号必须为11位数字', trigger: 'blur' }
    ]
    baseRules.userPassword = [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9_]{6,8}$/, message: '密码长度为6-8位，只能包含字母、数字和下划线', trigger: 'blur' }
    ]
  }
  
  return baseRules
})

const getGenderText = (gender) => {
  const map = { 0: '男', 1: '女' }
  return map[gender] || '未知'
}

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const getAvatarUrl = (avatar) => {
  if (!avatar) {
    return defaultAvatar
  }
  return avatar
}

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}

const loadData = async () => {
  loading.value = true
  try {
    console.log('loadData - 发送请求参数:', searchForm)
    console.log('loadData - current:', searchForm.current)
    console.log('loadData - pageSize:', searchForm.pageSize)
    const res = await getUserPage(searchForm)
    console.log('loadData - 响应数据:', res.data)
    console.log('loadData - userInfoVOList:', res.data.userInfoVOList)
    console.log('loadData - total:', res.data.total)
    tableData.value = res.data.userInfoVOList
    total.value = res.data.total
    console.log('loadData - 表格数据条数:', tableData.value.length)
    console.log('loadData - 更新后的 current:', searchForm.current)
    console.log('loadData - 更新后的 pageSize:', searchForm.pageSize)
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  console.log('handleSearch - 开始搜索，current:', searchForm.current, 'pageSize:', searchForm.pageSize)
  await loadData()
}

const handleSearchWithReset = async () => {
  console.log('handleSearchWithReset - 开始搜索并重置页码')
  searchForm.current = 1
  await loadData()
}

const handleReset = () => {
  searchForm.userAccount = ''
  searchForm.userName = ''
  searchForm.userStatus = null
  searchForm.current = 1
  searchForm.pageSize = 10
  loadData()
}

const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  userForm.id = row.id
  userForm.userAccount = row.account
  userForm.userName = row.username
  userForm.gender = row.gender
  userForm.userRole = row.role
  userForm.avatarUrl = row.avatar
}

const handleToggleStatus = async (row) => {
  const action = row.status === 0 ? '禁用' : '启用'

  try {
    await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await startOrUpUser(row.id, row.status)
    message.success(`${action}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteUser(row.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    message.warning('请先选择要删除的用户')
    return
  }

  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const ids = selectedRows.value.map(row => row.id).join(',')
    await deleteUser(ids)
    message.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (dialogType.value === 'add') {
          await addUser(userForm)
          message.success('添加成功')
        } else {
          await modifyUser(userForm)
          message.success('修改成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('操作失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDialogClose = () => {
  resetForm()
}

const resetForm = () => {
  userForm.id = null
  userForm.userAccount = ''
  userForm.userName = ''
  userForm.userPassword = ''
  userForm.gender = 0
  userForm.userRole = 'USER'
  userForm.avatarUrl = ''
  formRef.value?.clearValidate()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-manage-page {
  width: 100%;
  height: 100%;
  padding: 0;
  box-sizing: border-box;
  overflow: hidden;
  background-color: transparent;
}

.main-card {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.main-card :deep(.el-card__body) {
  height: 100%;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.card-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.search-section {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.search-form-wrapper {
  flex: 1;
}

.search-form {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 0;
}

.search-actions {
  display: flex;
  gap: 0.5rem;
}

.search-actions :deep(.el-button) {
  margin-left: 0;
}

.action-buttons {
  flex-shrink: 0;
  display: flex;
  gap: 0.625rem;
}

.section-divider {
  margin: 0.75rem 0;
  flex-shrink: 0;
}

.table-section {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.table-section :deep(.el-table) {
  width: 100%;
}

.table-section :deep(.el-table__body-wrapper) {
  overflow-y: auto;
}

.table-section :deep(.el-table__row) {
  height: 2.75rem;
}

.table-section :deep(.el-table td) {
  padding: 0.5rem 0;
}

.pagination-section {
  flex-shrink: 0;
  padding-top: 0.5rem;
}

.pagination-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
}

.pagination-info {
  flex-shrink: 0;
}

.total-text {
  font-size: 0.875rem;
  color: #606266;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.status-dot {
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 50%;
  background-color: #909399;
}

.status-dot.active {
  background-color: #67c23a;
}

.status-dot.inactive {
  background-color: #f56c6c;
}

.status-text {
  font-size: 0.875rem;
  color: #606266;
}

.empty-text {
  color: #909399;
  font-size: 0.875rem;
  padding: 1.25rem 0;
}

.avatar-form-item {
  display: flex;
  align-items: center;
  gap: 0.9375rem;
}

.avatar-preview {
  flex-shrink: 0;
}

.avatar-input {
  flex: 1;
}

@media screen and (max-width: 1200px) {
  .search-form {
    flex-wrap: wrap;
  }
}

@media screen and (max-width: 768px) {
  .search-section {
    flex-direction: column;
    align-items: stretch;
    gap: 0.75rem;
  }
  
  .search-form-wrapper {
    width: 100%;
  }
  
  .search-form {
    flex-direction: column;
    align-items: stretch;
    gap: 0.5rem;
  }
  
  .action-buttons {
    width: 100%;
    justify-content: center;
  }
  
  .action-buttons :deep(.el-button) {
    flex: 1;
  }
  
  .pagination-container {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .pagination-info {
    width: 100%;
    text-align: center;
  }
}

@media screen and (max-width: 480px) {
  .main-card :deep(.el-card__body) {
    padding: 0.75rem;
  }
  
  .search-actions {
    width: 100%;
  }
  
  .search-actions :deep(.el-button) {
    flex: 1;
  }
  
  .table-section :deep(.el-table__row) {
    height: 3.5rem;
  }
  
  .table-section :deep(.el-table td) {
    padding: 0.375rem 0;
  }
}

@media screen and (min-width: 1920px) {
  .main-card :deep(.el-card__body) {
    padding: 1.25rem;
  }
  
  .search-section {
    gap: 1.25rem;
  }
  
  .search-form {
    gap: 1rem;
  }
  
  .table-section :deep(.el-table__row) {
    height: 3rem;
  }
  
  .table-section :deep(.el-table td) {
    padding: 0.625rem 0;
  }
}
</style>
