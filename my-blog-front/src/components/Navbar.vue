<template>
  <header class="w-full h-16 flex items-center justify-between px-6 bg-white shadow">
    <div class="text-xl font-bold text-gray-800">
      <router-link to="/">MyBlog</router-link>
    </div>
    <div class="inline-flex items-center mr-6">
      <input v-model="keyword" @keyup.enter="search" type="text" placeholder="搜索文章/项目..."
        class="border rounded px-3 py-1 focus:outline-none focus:ring focus:border-blue-300" />
      <button @click="search" class="ml-2 px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600">
        搜索
      </button>
    </div>

    <div>
      <template v-if="nickname">
        <span class="mr-4 text-gray-600">你好，{{ nickname }}</span>
        <button @click="logout" class="text-red-500 hover:underline">退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="mr-4 text-gray-600 hover:text-blue-600">登录</router-link>
        <router-link to="/register" class="text-gray-600 hover:text-blue-600">注册</router-link>
      </template>
    </div>
  </header>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const nickname = computed(() => userStore.user?.nickname || null)

// 搜索关键字
const keyword = ref('')

// 搜索方法
const search = () => {
  if (!keyword.value.trim()) return
  // 跳转到搜索结果页，带 query 参数
  router.push({ path: '/search', query: { q: keyword.value.trim() } })
}

// 退出登录
const logout = () => {
  userStore.clearUser()
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

