<template>
  <header class="w-full fixed top-0 left-0 z-[200] theme-navbar backdrop-blur-md">
    <div class="header-inner max-w-[1800px] mx-auto flex items-center justify-between h-16 px-0">

      <!-- 左侧区域：侧边栏按钮 + 标题 + 图标 -->
<div class="flex items-center gap-2">
  <!-- 侧边栏切换按钮（与右侧风格统一） -->
  <button
    @click="$emit('toggleSidebar')"
    class="relative z-[100] w-10 h-10 flex items-center justify-center rounded-lg bg-gray-200 hover:bg-gray-300 dark:bg-gray-700 dark:hover:bg-gray-600 transition shadow-sm"
    title="切换侧边栏"
  >
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" class="text-gray-900 dark:text-gray-100">
      <path fill="currentColor" d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/>
    </svg>
  </button>

  <!-- 标题 + 图标 -->
  <router-link
    to="/"
    class="flex items-center gap-2 text-xl font-semibold text-gray-800 hover:text-blue-600 transition-colors hidden sm:flex"
  >
    <!-- 简洁的标题图标 -->
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" class="text-blue-600">
      <path fill="currentColor" d="M4 6H2v14c0 1.1.9 2 2 2h14v-2H4V6zm16-4H8c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-1 9H9V9h10v2zm-4 4H9v-2h6v2zm4-8H9V5h10v2z"/>
    </svg>
    <span>{{ pageTitle }}</span>
  </router-link>
</div>





      <!-- 中间搜索框 (优化尺寸) -->
      <div class="flex-1 mx-4 max-w-md">
        <div class="relative w-full">
          <input v-model="keyword" @keyup.enter="search" type="text" placeholder="搜索文章/项目..."
            class="w-full rounded-full px-4 py-1.5 pr-12 border transition text-sm
                   text-gray-800 dark:text-gray-100 
                   border-gray-300 dark:border-gray-600
                   focus:outline-none focus:ring-2 focus:ring-blue-300 dark:focus:ring-blue-500 focus:border-blue-500 dark:focus:border-blue-400
                   placeholder-gray-400 dark:placeholder-gray-300
                   bg-white/50 dark:bg-gray-700/80" />
          <button @click="search"
            class="absolute right-1 top-1/2 -translate-y-1/2 bg-blue-500 text-white px-3 py-0.5 rounded-full hover:bg-blue-600 transition text-sm">
            搜索
          </button>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="flex items-center gap-4">
        <!-- 主题切换按钮 -->
        <button @click="toggleTheme" class="p-1.5 rounded-full hover:bg-blue-100 transition" title="切换主题">
          <svg v-if="isDark" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM8 0a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 0zm0 13a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-1 0v-2A.5.5 0 0 1 8 13zm8-5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2a.5.5 0 0 1 .5.5zM3 8a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1h2A.5.5 0 0 1 3 8zm10.657-5.657a.5.5 0 0 1 0 .707l-1.414 1.415a.5.5 0 1 1-.707-.708l1.414-1.414a.5.5 0 0 1 .707 0zm-9.193 9.193a.5.5 0 0 1 0 .707L3.05 13.657a.5.5 0 0 1-.707-.707l1.414-1.414a.5.5 0 0 1 .707 0zm9.193 2.121a.5.5 0 0 1-.707 0l-1.414-1.414a.5.5 0 0 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .707zM4.464 4.465a.5.5 0 0 1-.707 0L2.343 3.05a.5.5 0 1 1 .707-.707l1.414 1.414a.5.5 0 0 1 0 .708z"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
            <path d="M6 .278a.768.768 0 0 1 .08.858 7.208 7.208 0 0 0-.878 3.46c0 4.021 3.278 7.277 7.318 7.277.527 0 1.04-.055 1.533-.16a.787.787 0 0 1 .81.316.733.733 0 0 1-.031.893A8.349 8.349 0 0 1 8.344 16C3.734 16 0 12.286 0 7.71 0 4.266 2.114 1.312 5.124.06A.752.752 0 0 1 6 .278z"/>
          </svg>
        </button>

        <!-- 右侧用户区 (美化头像区域) -->
        <div class="relative" ref="menuRef">
          <template v-if="user">
            <div class="flex items-center gap-3">
              <!-- 用户信息 -->
            <div
              class="flex items-center gap-2 bg-white/30 rounded-full pl-1.5 pr-1 py-1 hover:bg-white/50 transition cursor-pointer"
              @click="toggleDropdown">
              <img :src="user.avatar || '/images/default-avatar.png'" alt="avatar"
                class="w-11 h-11 rounded-full border-2 border-white shadow-sm object-cover"
                :aria-expanded="dropdownOpen" />
              <span class="text-sm font-medium pr-2 hidden sm:block">{{ user.nickname || user.username }}</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" class="hidden sm:block"
                viewBox="0 0 16 16">
                <path
                  d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z" />
              </svg>
            </div>
          </div>

          <transition name="fade-scale">
            <div v-if="dropdownOpen"
              :class="['absolute right-0 mt-2 w-64 rounded-lg shadow-lg py-3 z-50 border',
                isDark 
                  ? 'bg-gray-800 text-gray-200 border-gray-700' 
                  : 'bg-white text-gray-800 border-gray-200']">
              <div :class="['absolute right-3 -top-2 w-3 h-3 border-l border-t rotate-45',
                isDark ? 'bg-gray-800 border-gray-700' : 'bg-white border-gray-200']"></div>

              <!-- 用户信息头部 -->
              <div :class="['px-4 py-2 border-b mb-2',
                isDark ? 'border-gray-700' : 'border-gray-200']">
                <div class="flex items-center gap-3">
                  <img :src="user.avatar || '/images/default-avatar.png'"
                    class="w-12 h-12 rounded-full border border-gray-200" />
                  <div>
                    <div class="font-medium">{{ user.nickname || user.username }}</div>
                    <div class="text-xs text-gray-500">{{ user.email }}</div>
                  </div>
                </div>
              </div>

              <router-link :to="'/user/'+user.id" 
                :class="['flex items-center gap-2 px-4 py-2 transition',
                  isDark ? 'hover:bg-gray-700' : 'hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                  <path
                    d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z" />
                </svg>
                <span>个人主页</span>
              </router-link>

              <router-link to="/settings" 
                :class="['flex items-center gap-2 px-4 py-2 transition',
                  isDark ? 'hover:bg-gray-700' : 'hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                  <path
                    d="M9.405 1.05c-.413-1.4-2.397-1.4-2.81 0l-.1.34a1.464 1.464 0 0 1-2.105.872l-.31-.17c-1.283-.698-2.686.705-1.987 1.987l.169.311c.446.82.023 1.841-.872 2.105l-.34.1c-1.4.413-1.4 2.397 0 2.81l.34.1a1.464 1.464 0 0 1 .872 2.105l-.17.31c-.698 1.283.705 2.686 1.987 1.987l.311-.169a1.464 1.464 0 0 1 2.105.872l.1.34c.413 1.4 2.397 1.4 2.81 0l.1-.34a1.464 1.464 0 0 1 2.105-.872l.31.17c1.283.698 2.686-.705 1.987-1.987l-.169-.311a1.464 1.464 0 0 1 .872-2.105l.34-.1c1.4-.413 1.4-2.397 0-2.81l-.34-.1a1.464 1.464 0 0 1-.872-2.105l.17-.31c.698-1.283-.705-2.686-1.987-1.987l-.311.169a1.464 1.464 0 0 1-2.105-.872l-.1-.34zM8 10.93a2.929 2.929 0 1 1 0-5.86 2.929 2.929 0 0 1 0 5.858z" />
                </svg>
                <span>设置</span>
              </router-link>

              <div :class="['border-t my-1', isDark ? 'border-gray-700' : 'border-gray-200']"></div>

              <button @click="logout"
                :class="['flex items-center gap-2 w-full text-left px-4 py-2 text-red-500 transition',
                  isDark ? 'hover:bg-gray-700' : 'hover:bg-gray-100']">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                  <path fill-rule="evenodd"
                    d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z" />
                  <path fill-rule="evenodd"
                    d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z" />
                </svg>
                <span>退出登录</span>
              </button>
            </div>
          </transition>
        </template>
        <template v-else>
          <div class="flex items-center gap-4">
            <router-link to="/login" class="hover:text-blue-600 transition">登录</router-link>
            <router-link to="/register" class="hover:text-blue-600 transition">注册</router-link>
          </div>
        </template>
      </div>
    </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUserById } from '@/api/userApi'

// 主题状态
const isDark = ref(false)
const toggleTheme = () => {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('darkMode', isDark.value)
  
  // 更新系统状态栏颜色（仅移动设备）
  const metaThemeColor = document.querySelector('meta[name="theme-color"]')
  if (metaThemeColor) {
    metaThemeColor.setAttribute('content', isDark.value ? '#2c3e50' : '#ffffff')
  }
  
  // 触发主题变化事件，通知其他组件
  window.dispatchEvent(new CustomEvent('themeChanged', { 
    detail: { isDark: isDark.value } 
  }))
}

// 初始化主题
onMounted(() => {
  // 检查用户偏好或存储的设置
  const savedMode = localStorage.getItem('darkMode')
  
  if (savedMode !== null) {
    // 使用存储的设置
    isDark.value = savedMode === 'true'
  } else {
    // 检查系统偏好
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    isDark.value = prefersDark
  }
  
  // 应用主题
  document.documentElement.classList.toggle('dark', isDark.value)
  
  // 监听系统主题变化
  window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
    // 只有当用户没有明确设置主题时才跟随系统
    if (localStorage.getItem('darkMode') === null) {
      isDark.value = e.matches
      document.documentElement.classList.toggle('dark', isDark.value)
    }
  })
  
  // 监听来自其他组件的主题变化事件
  window.addEventListener('themeChanged', (event) => {
    isDark.value = event.detail.isDark
    document.documentElement.classList.toggle('dark', isDark.value)
  })
})

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const user = ref(null)

// 从后端API获取用户信息
const fetchUserInfo = async () => {
  if (!userStore.user?.id) return
  
  try {
    const res = await getUserById(userStore.user.id)
    if (res.status === 'success' && res.data) {
      user.value = res.data
    } else {
      // 如果API获取失败，使用store中的用户信息
      user.value = userStore.user
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    user.value = userStore.user
  }
}

// 监听用户登录状态变化
watch(() => userStore.user, (newUser) => {
  if (newUser) {
    fetchUserInfo()
  } else {
    user.value = null
  }
}, { immediate: true })

// 添加用户数据监听
watch(() => userStore.user, (newUser) => {
  if (newUser) {
    console.log('用户数据更新:', newUser)
  }
}, { immediate: true })

const keyword = ref('')
const search = () => {
  if (!keyword.value.trim()) return
  router.push({ path: '/search', query: { q: keyword.value.trim() } })
}

const logout = () => {
  userStore.clearUser()
  localStorage.removeItem('user')
  router.push('/login')
}

const dropdownOpen = ref(false)
const toggleDropdown = () => (dropdownOpen.value = !dropdownOpen.value)
const menuRef = ref(null)
const handleClickOutside = (e) => {
  if (menuRef.value && !menuRef.value.contains(e.target)) dropdownOpen.value = false
}
onMounted(() => document.addEventListener('click', handleClickOutside))
onBeforeUnmount(() => document.removeEventListener('click', handleClickOutside))

const pageTitle = computed(() => {
  if (route.path.startsWith('/user') && user.value) return user.value.username
  if (route.path.startsWith('/settings')) return '设置'
  if (route.path.startsWith('/search')) return '搜索结果'
  return 'MyBlog'
})
</script>

<style scoped>
.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 150ms ease;
  transform-origin: top right;
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

/* 使用主题变量的导航栏 */
.theme-navbar {
  background: linear-gradient(to right, var(--navbar-bg-start), var(--navbar-bg-end));
  color: var(--text-primary);
  border-bottom: 1px solid var(--border-color);
  transition: background 0.3s ease, border-color 0.3s ease;
}

/* 暗色模式下的特定样式 */
:global(.dark) .theme-navbar {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
</style>
