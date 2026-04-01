

<template>
  <a-layout class="basic-layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader />
    <!-- 主要内容区域 -->
    <a-layout-content class="main-content">
      <router-view />
    </a-layout-content>
    <!-- 底部版权信息 -->
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'

const loginUserStore = useLoginUserStore()

// 应用初始化时获取登录用户信息
onMounted(async () => {
  await loginUserStore.fetchLoginUser()
})
</script>

<style scoped>
.basic-layout {
  background: none;
}

.main-content {
  width: 100%;
  padding: 0;
  background: none;
  margin: 0;
}
</style>

