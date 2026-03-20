<template>
  <el-menu
    :default-active="activeMenu"
    class="global-sider"
    background-color="#001529"
    text-color="#fff"
    active-text-color="#1890ff"
    router
  >
    <el-menu-item index="/home">
      <el-icon><home-filled /></el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-sub-menu index="user">
      <template #title>
        <el-icon><user /></el-icon>
        <span>个人中心</span>
      </template>
      <el-menu-item index="/user/info">个人信息</el-menu-item>
      <el-menu-item index="/user/password">修改密码</el-menu-item>
    </el-sub-menu>
    <el-menu-item v-if="isAdmin" index="/admin/users">
      <el-icon><user-filled /></el-icon>
      <span>用户管理</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { HomeFilled, User, UserFilled } from '@element-plus/icons-vue'

const route = useRoute()
const loginUserStore = useLoginUserStore()
const loginUser = computed(() => loginUserStore.getLoginUser())

const activeMenu = computed(() => route.path)
const isAdmin = computed(() => loginUser.value?.role === 'ADMIN')
</script>

<style scoped>
.global-sider {
  height: 100%;
  border-right: none;
}

.global-sider .el-menu-item,
.global-sider .el-sub-menu__title {
  height: 50px;
  line-height: 50px;
}

.global-sider .el-menu-item:hover,
.global-sider .el-sub-menu__title:hover {
  background-color: #1890ff !important;
}
</style>
