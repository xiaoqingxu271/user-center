<template>
  <el-container class="layout-container">
    <el-aside :width="asideWidth" class="layout-aside">
      <GlobalSider />
    </el-aside>
    <el-container class="layout-content">
      <el-header class="layout-header">
        <div class="header-left">
          <el-button 
            class="collapse-btn" 
            @click="toggleAside"
            :icon="isCollapsed ? Expand : Fold"
            circle
            size="small"
          />
        </div>
        <GlobalHeader />
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from '@/components/GlobalSider.vue'
import { Expand, Fold } from '@element-plus/icons-vue'

const isCollapsed = ref(false)
const isMobile = ref(false)

const asideWidth = computed(() => {
  if (isMobile.value) {
    return isCollapsed.value ? '0px' : '240px'
  }
  return isCollapsed.value ? '64px' : '240px'
})

const toggleAside = () => {
  isCollapsed.value = !isCollapsed.value
}

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value) {
    isCollapsed.value = true
  }
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.layout-container {
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
}

.layout-aside {
  background-color: #001529;
  overflow-x: hidden;
  overflow-y: auto;
  transition: width 0.3s ease;
  flex-shrink: 0;
}

.layout-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  overflow: hidden;
}

.layout-header {
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 1.25rem;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.collapse-btn {
  display: none;
}

.layout-main {
  background-color: #f5f5f5;
  padding: 1.25rem;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  flex: 1;
}

@media screen and (max-width: 768px) {
  .layout-container {
    position: relative;
  }
  
  .layout-aside {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    z-index: 1000;
  }
  
  .layout-aside[style*="width: 0px"] {
    display: none;
  }
  
  .layout-header {
    padding: 0 1rem;
  }
  
  .collapse-btn {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .layout-main {
    padding: 1rem;
  }
}

@media screen and (max-width: 480px) {
  .layout-header {
    padding: 0 0.75rem;
    height: 56px;
  }
  
  .layout-main {
    padding: 0.75rem;
  }
}

@media screen and (min-width: 1920px) {
  .layout-header {
    padding: 0 1.5rem;
    height: 64px;
  }
  
  .layout-main {
    padding: 1.5rem;
  }
}
</style>
