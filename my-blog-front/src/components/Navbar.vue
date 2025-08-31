<script setup>
import { useUserStore } from '@/stores/user'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const nickname = computed(() => userStore.user?.nickname || null)

const logout = () => {
  userStore.clearUser()
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<template>
  <header class="w-full h-16 flex items-center justify-between px-6 bg-white shadow">
    <div class="text-xl font-bold text-gray-800">
      <router-link to="/">MyBlog</router-link>
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
