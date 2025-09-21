<template>
  <div :class="['min-h-screen bg-cover bg-fixed relative overflow-hidden', isDark ? 'dark-theme' : 'light-theme']">
    <!-- 明色主题背景元素 - 纯净水花气泡效果 -->
    <div v-if="!isDark" class="fixed inset-0 bg-gradient-to-br from-blue-50/30 via-cyan-50/20 to-teal-50/10 backdrop-blur-3xl z-0"></div>
    <div v-if="!isDark" class="fixed top-20 right-20 w-80 h-80 bg-blue-200/20 rounded-full blur-4xl animate-float-slow z-0"></div>
    <div v-if="!isDark" class="fixed bottom-10 left-10 w-72 h-72 bg-cyan-200/20 rounded-full blur-3xl animate-float-medium z-0"></div>
    <div v-if="!isDark" class="fixed bottom-20 left-20 w-60 h-60 bg-blue-300/15 rounded-full blur-3xl animate-bubble z-0"></div>
    <div v-if="!isDark" class="fixed top-1/4 right-1/4 w-56 h-56 bg-cyan-300/15 rounded-full blur-2xl animate-bubble-delayed z-0"></div>
    <div v-if="!isDark" class="fixed top-1/3 right-1/4 w-40 h-40 bg-blue-200/15 rounded-full blur-2xl animate-ripple z-0"></div>
    <div v-if="!isDark" class="fixed bottom-1/4 left-1/3 w-36 h-36 bg-cyan-200/15 rounded-full blur-xl animate-ripple-delayed z-0"></div>
    
    <!-- 暗色主题背景元素 - 霓虹灯效果 -->
    <div v-if="isDark" class="fixed inset-0 bg-gradient-to-br from-blue-900/3 via-purple-900/2 to-indigo-900/1 backdrop-blur-4xl z-0"></div>
    <div v-if="isDark" class="fixed bottom-16 right-24 w-60 h-60 bg-blue-400/5 rounded-full blur-3xl animate-ripple-smooth z-0"></div>
    <div v-if="isDark" class="fixed inset-0 bg-[radial-gradient(ellipse_80%_80%_at_50%_50%,_rgba(120,119,198,0.4)_0%,_transparent_60%)] z-0"></div>
    <div v-if="isDark" class="fixed top-0 right-0 w-[32rem] h-[32rem] bg-purple-500/8 rounded-full blur-4xl z-0"></div>
    <div v-if="isDark" class="fixed bottom-0 left-0 w-[28rem] h-[28rem] bg-blue-500/6 rounded-full blur-4xl z-0"></div>
    
    <div class="max-w-5xl mx-auto p-6 relative z-10">
    <!-- 头部区域 -->
    <div :class="['flex items-start gap-8 p-8 rounded-2xl shadow-lg border backdrop-blur-xl', 
      isDark ? 'bg-gray-800/60 border-blue-400/15 shadow-blue-500/3' : 'bg-white/80 border-gray-100']">
      <!-- 头像 -->
      <div class="relative">
        <img
          :src="user.avatar || defaultAvatar"
          alt="avatar"
          class="w-36 h-36 rounded-full border-4 border-white shadow-xl object-cover"
        />
        <div class="absolute -bottom-2 -right-2 w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center shadow-md">
          <svg class="w-4 h-4 text-white" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"/>
          </svg>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="flex-1">
        <div class="flex items-center gap-4 mb-3">
          <h1 :class="['text-4xl font-bold bg-gradient-to-r bg-clip-text text-transparent', 
            isDark ? 'from-blue-300 to-purple-400 p3-title-glow' : 'from-gray-800 to-blue-600']">
            {{ user.nickname || user.username }}
          </h1>
          <span :class="['px-3 py-1 text-sm font-medium rounded-full', 
            isDark ? 'bg-blue-900/40 text-blue-300' : 'bg-blue-100 text-blue-700']">
            @{{ user.username }}
          </span>
        </div>

        <div :class="['space-y-2', isDark ? 'text-gray-300' : 'text-gray-600']">
          <div v-if="user.email" class="flex items-center gap-2">
            <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
              <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
            </svg>
            <span>{{ user.email }}</span>
          </div>

          <div class="flex items-center gap-2">
            <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd"/>
            </svg>
            <span>加入于 {{ formatDate(user.createdAt) }}</span>
          </div>
        </div>

        <div :class="['mt-4 p-4 rounded-lg border', 
          isDark ? 'bg-gray-700/40 border-gray-600/50' : 'bg-gray-50 border-gray-200']">
          <p :class="['leading-relaxed', isDark ? 'text-gray-300' : 'text-gray-700']">
            {{ user.bio || '这个人很懒，什么都没写~' }}
          </p>
        </div>

        <!-- 编辑按钮（仅本人可见） -->
        <div v-if="isSelf" class="mt-6">
          <router-link
            to="/settings"
            :class="['inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r text-white font-medium rounded-lg shadow-md hover:shadow-lg transition-all duration-200',
              isDark ? 'from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700' : 'from-blue-400 to-cyan-500 hover:from-blue-500 hover:to-cyan-600']"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
            </svg>
            编辑个人资料
          </router-link>
        </div>
      </div>
    </div>

    <div :class="['my-8 border-t', isDark ? 'border-blue-400/20' : 'border-gray-200/50']"></div>

    <!-- 内容标签 -->
    <div :class="['backdrop-blur-xl rounded-2xl shadow-lg border p-6', 
      isDark ? 'bg-gray-800/60 border-blue-400/15 shadow-blue-500/3' : 'bg-white/80 border-gray-100']">
      <nav :class="['flex gap-8 border-b pb-4 mb-6', 
        isDark ? 'border-blue-400/20' : 'border-gray-200/50']">
        <button
          v-for="tab in tabs"
          :key="tab"
          @click="currentTab = tab"
          :class="[ 
            'relative px-4 py-2 font-semibold transition-all duration-200',
            currentTab === tab 
              ? (isDark ? 'text-blue-300' : 'text-blue-600')
              : (isDark ? 'text-gray-400 hover:text-gray-200' : 'text-gray-500 hover:text-gray-700')
          ]"
        >
          {{ tab }}
          <span 
            v-if="currentTab === tab"
            :class="['absolute -bottom-1 left-0 w-full h-0.5 bg-gradient-to-r rounded-full',
              isDark ? 'from-blue-400 to-purple-500' : 'from-blue-500 to-purple-600']"
          ></span>
        </button>
      </nav>

      <!-- 动态内容 -->
      <div v-if="currentTab === '文章'">
        <div v-if="!user.articles?.length" class="text-center py-12">
          <svg :class="['w-16 h-16 mx-auto mb-4', isDark ? 'text-gray-600' : 'text-gray-300']" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
          </svg>
          <p :class="['text-lg', isDark ? 'text-gray-400' : 'text-gray-500']">还没有发布过文章</p>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <article
            v-for="article in user.articles"
            :key="article.id"
            :class="[
              'group cursor-pointer overflow-hidden rounded-xl border transition-all duration-500 transform hover:-translate-y-1 hover:shadow-lg hover:rotate-1',
              isDark 
                ? 'border-gray-600 bg-gray-800/90 hover:bg-gray-700/80' 
                : 'border-gray-200 bg-white/95 hover:bg-gray-50'
            ]"
            @click="goToArticle(article)"
          >
            <!-- 文章卡片顶部装饰条 -->
            <div :class="['h-1.5 bg-gradient-to-r', 
              isDark ? 'from-purple-500 to-blue-500' : 'from-blue-400 to-cyan-400']"></div>
            
            <div class="p-5">
              <!-- 文章标题 -->
              <h3 :class="[
                'text-lg font-bold transition-colors duration-200 mb-2 line-clamp-2',
                isDark 
                  ? 'text-gray-200 group-hover:text-blue-300' 
                  : 'text-gray-800 group-hover:text-blue-600'
              ]">
                {{ article.title }}
              </h3>
              
              <!-- 文章内容预览 -->
              <p v-if="article.content" :class="[
                'text-sm mb-3 line-clamp-2',
                isDark ? 'text-gray-400' : 'text-gray-600'
              ]">
                {{ stripHtml(article.content) }}
              </p>
              
              <!-- 底部信息栏 -->
              <div :class="[
                'flex items-center justify-between mt-3 pt-2 border-t',
                isDark ? 'border-gray-700' : 'border-gray-100'
              ]">
                <div :class="['flex items-center text-xs', 
                  isDark ? 'text-gray-400' : 'text-gray-500']">
                  <svg class="w-3.5 h-3.5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd"/>
                  </svg>
                  {{ formatDate(article.createdAt) }}
                </div>
                
                <span :class="['inline-flex items-center px-2 py-0.5 rounded text-xs font-medium',
                  isDark ? 'bg-blue-900/40 text-blue-300' : 'bg-blue-100 text-blue-800']">
                  查看详情
                  <svg class="w-3 h-3 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                  </svg>
                </span>
              </div>
            </div>
          </article>
        </div>

        <!-- 分页控件 -->
        <div v-if="user.articles?.length && pagination.totalPage > 0" class="mt-8 flex justify-center items-center">
          <div class="flex items-center space-x-3">
            <!-- 上一页 -->
            <button
              @click="prevPage"
              :disabled="pagination.pageNo === 1"
              :class="['px-3 py-1 border rounded-md disabled:opacity-40',
                isDark ? 'text-gray-300 border-gray-700 hover:bg-gray-800' : 'text-gray-600 hover:bg-gray-50']"
            >
              上一页
            </button>

            <!-- 页码 -->
            <div class="flex space-x-2">
              <span
                v-for="page in visiblePages"
                :key="page"
                @click="goToPage(page)"
                class="px-3 py-1 rounded-md text-sm font-medium cursor-pointer"
                :class="{
                  'bg-blue-600 text-white': isDark && page === pagination.pageNo,
                  'bg-blue-500 text-white': !isDark && page === pagination.pageNo,
                  'text-gray-300 hover:bg-gray-800': isDark && page !== pagination.pageNo && page !== '...',
                  'text-gray-700 hover:bg-gray-100': !isDark && page !== pagination.pageNo && page !== '...',
                  'pointer-events-none text-gray-600': isDark && page === '...',
                  'pointer-events-none text-gray-400': !isDark && page === '...'
                }"
              >
                {{ page }}
              </span>
            </div>

            <!-- 下一页 -->
            <button
              @click="nextPage"
              :disabled="pagination.pageNo >= pagination.totalPage"
              :class="['px-3 py-1 border rounded-md disabled:opacity-40',
                isDark ? 'text-gray-300 border-gray-700 hover:bg-gray-800' : 'text-gray-600 hover:bg-gray-50']"
            >
              下一页
            </button>

            <!-- 每页条数 -->
            <select
              v-model="pagination.pageSize"
              @change="handlePageSizeChange"
              :class="['px-2 py-1 border rounded-md text-sm',
                isDark ? 'bg-gray-800 text-gray-300 border-gray-700' : 'bg-white text-gray-700 border-gray-300']"
            >
              <option value="6">6条/页</option>
              <option value="10">10条/页</option>
              <option value="20">20条/页</option>
              <option value="50">50条/页</option>
            </select>
          </div>
        </div>
      </div>

      <div v-if="currentTab === '项目'">
        <div v-if="!user.projects?.length" class="text-center py-12">
          <svg :class="['w-16 h-16 mx-auto mb-4', isDark ? 'text-gray-600' : 'text-gray-300']" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"/>
          </svg>
          <p :class="['text-lg', isDark ? 'text-gray-400' : 'text-gray-500']">还没有展示的项目</p>
        </div>
        <div v-else class="space-y-4">
          <div class="space-y-4">
            <div 
              v-for="project in user.projects"
              :key="project.id"
              :class="[
                'group cursor-pointer p-4 transition-colors duration-200',
                isDark 
                  ? 'hover:bg-gray-700/50 border-gray-700' 
                  : 'hover:bg-gray-100/50 border-gray-200'
              ]"
              @click="goToProject(project)"
            >
              <div class="flex items-start gap-3">
                <div :class="['flex-shrink-0 w-8 h-8 rounded-full flex items-center justify-center',
                  isDark ? 'bg-indigo-900/40 text-indigo-300' : 'bg-indigo-100 text-indigo-800']">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                  </svg>
                </div>
                <div class="flex-1">
                  <h3 :class="['font-medium', isDark ? 'text-gray-200' : 'text-gray-800']">
                    {{ project.name }}
                  </h3>
                  <p v-if="project.description" :class="['text-sm mt-1', isDark ? 'text-gray-400' : 'text-gray-600']">
                    {{ project.description }}
                  </p>
                  <div v-if="project.tags?.length" class="flex flex-wrap gap-1 mt-2">
                    <span 
                      v-for="tag in project.tags.slice(0, 3)"
                      :key="tag"
                      :class="['px-2 py-0.5 text-xs rounded-full',
                        isDark ? 'bg-indigo-900/30 text-indigo-300' : 'bg-indigo-100 text-indigo-800']"
                    >
                      {{ tag }}
                    </span>
                  </div>
                  <div :class="['text-xs mt-2', isDark ? 'text-gray-500' : 'text-gray-400']">
                    {{ formatDate(project.createdAt) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 项目分页控件 -->
        <div v-if="user.projects?.length && projectPagination.totalPage > 0" class="mt-8 flex justify-center items-center">
          <div class="flex items-center space-x-3">
            <!-- 上一页 -->
            <button
              @click="prevProjectPage"
              :disabled="projectPagination.pageNo === 1"
              class="px-3 py-1 border rounded-md text-gray-600 disabled:opacity-40 hover:bg-gray-50"
            >
              上一页
            </button>

            <!-- 页码 -->
            <div class="flex space-x-2">
              <span
                v-for="page in projectVisiblePages"
                :key="page"
                @click="goToProjectPage(page)"
                class="px-3 py-1 rounded-md text-sm font-medium cursor-pointer"
                :class="{
                  'bg-blue-500 text-white': page === projectPagination.pageNo,
                  'text-gray-700 hover:bg-gray-100': page !== projectPagination.pageNo && page !== '...',
                  'pointer-events-none text-gray-400': page === '...'
                }"
              >
                {{ page }}
              </span>
            </div>

            <!-- 下一页 -->
            <button
              @click="nextProjectPage"
              :disabled="projectPagination.pageNo >= projectPagination.totalPage"
              class="px-3 py-1 border rounded-md text-gray-600 disabled:opacity-40 hover:bg-gray-50"
            >
              下一页
            </button>

            <!-- 每页条数 -->
            <select
              v-model="projectPagination.pageSize"
              @change="handleProjectPageSizeChange"
              class="px-2 py-1 border rounded-md text-sm"
            >
              <option value="5">5条/页</option>
              <option value="10">10条/页</option>
              <option value="20">20条/页</option>
              <option value="50">50条/页</option>
            </select>
          </div>
        </div>
      </div>

      <div v-if="currentTab === '动态'">
        <div v-if="!user.activities?.length" class="text-center py-12">
          <svg :class="['w-16 h-16 mx-auto mb-4', isDark ? 'text-gray-600' : 'text-gray-300']" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M13 10V3L4 14h7v7l9-11h-7z"/>
          </svg>
          <p :class="['text-lg', isDark ? 'text-gray-400' : 'text-gray-500']">暂无动态</p>
        </div>
        <ul v-else class="space-y-4">
          <li
            v-for="(activity, index) in user.activities"
            :key="index"
            :class="['flex items-start gap-3 p-4 rounded-xl border',
              isDark ? 'bg-gray-800/80 border-gray-700' : 'bg-white border-gray-200']"
          >
            <div :class="['w-2 h-2 rounded-full mt-2 flex-shrink-0',
              isDark ? 'bg-blue-400' : 'bg-blue-500']"></div>
            <span :class="[isDark ? 'text-gray-300' : 'text-gray-700']">{{ activity }}</span>
          </li>
        </ul>
      </div>
    </div>
    </div>
  </div>
</template>

<script setup>

import { ref, computed, onMounted, watch, onBeforeMount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUserById } from '@/api/userApi'
import { getMyProjects } from '@/api/project'
import { getPrivateNotes } from '@/api/techNote'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 用户 ID
const userId = computed(() => {
  const id = route.params.id
  return id && !isNaN(Number(id)) ? Number(id) : userStore.user?.id
})

// 主题状态（与NavBar保持一致）
const isDark = ref(false)

// 初始化主题
const initTheme = () => {
  const savedMode = localStorage.getItem('darkMode')
  
  if (savedMode !== null) {
    isDark.value = savedMode === 'true'
  } else {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    isDark.value = prefersDark
  }
  
  document.documentElement.classList.toggle('dark', isDark.value)
}

// 切换主题 (优化性能)
const toggleTheme = () => {
  // 立即更新DOM类名
  document.documentElement.classList.toggle('dark', !isDark.value)
  
  // 批量更新状态
  requestAnimationFrame(() => {
    isDark.value = !isDark.value
    localStorage.setItem('darkMode', isDark.value)
    
    // 更新系统状态栏颜色
    const metaThemeColor = document.querySelector('meta[name="theme-color"]')
    if (metaThemeColor) {
      metaThemeColor.setAttribute('content', isDark.value ? '#2c3e50' : '#ffffff')
    }
    
    // 触发自定义事件通知其他组件主题变化
    window.dispatchEvent(new CustomEvent('themeChanged', {
      detail: { isDark: isDark.value }
    }))
  })
}

// 监听系统主题变化
const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
const handleSystemThemeChange = (e) => {
  isDark.value = e.matches
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('darkMode', isDark.value)
}

// 组件挂载时初始化主题
onMounted(() => {
  // 优先从localStorage获取主题设置
  const savedTheme = localStorage.getItem('darkMode')
  if (savedTheme !== null) {
    isDark.value = savedTheme === 'true'
  } else {
    // 其次检查系统偏好
    isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
  }
  
  // 立即应用主题
  document.documentElement.classList.toggle('dark', isDark.value)
  
  // 监听系统主题变化
  mediaQuery.addEventListener('change', handleSystemThemeChange)
  
  // 监听来自NavBar的主题变化事件
  window.addEventListener('themeChanged', (event) => {
    isDark.value = event.detail.isDark
    document.documentElement.classList.toggle('dark', isDark.value)
    localStorage.setItem('darkMode', isDark.value)
  })
})

// 默认头像
const defaultAvatar = '/default-avatar.png'

// 用户信息
const user = ref({
  avatar: '',
  username: '',
  nickname: '',
  email: '',
  bio: '',
  createdAt: '',
  articles: [],
  projects: [],
  activities: []
})

// 分页相关状态
const pagination = ref({
  pageNo: 1,
  pageSize: 6,
  total: 0,
  totalPage: 0
})

// 计算显示的页码范围
const visiblePages = computed(() => {
  const current = pagination.value.pageNo
  const total = pagination.value.totalPage
  const range = 2
  let pages = []

  if (total <= 5) {
    return Array.from({ length: total }, (_, i) => i + 1)
  }

  if (current > range + 1) {
    pages.push(1)
    if (current > range + 2) pages.push('...')
  }

  const start = Math.max(1, current - range)
  const end = Math.min(total, current + range)
  for (let i = start; i <= end; i++) pages.push(i)

  if (current < total - range) {
    if (current < total - range - 1) pages.push('...')
    pages.push(total)
  }

  return pages
})

// 标签切换
const tabs = ['文章', '项目', '动态']
const currentTab = ref('文章')

// 项目分页相关状态
const projectPagination = ref({
  pageNo: 1,
  pageSize: 5,
  total: 0,
  totalPage: 0
})

// 计算项目的页码范围
const projectVisiblePages = computed(() => {
  const current = projectPagination.value.pageNo
  const total = projectPagination.value.totalPage
  const range = 2
  let pages = []

  if (total <= 5) {
    return Array.from({ length: total }, (_, i) => i + 1)
  }

  if (current > range + 1) {
    pages.push(1)
    if (current > range + 2) pages.push('...')
  }

  const start = Math.max(1, current - range)
  const end = Math.min(total, current + range)
  for (let i = start; i <= end; i++) pages.push(i)

  if (current < total - range) {
    if (current < total - range - 1) pages.push('...')
    pages.push(total)
  }

  return pages
})

// 是否本人
const isSelf = computed(() => userStore.user?.id === userId.value)

// 日期格式化（修复 "2025-08-02 21:38:55" 解析问题）
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  // 替换空格为 T，保证能被 Date 解析
  return new Date(dateStr.replace(' ', 'T')).toLocaleDateString()
}

// 去除HTML标签，用于显示纯文本预览
const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '').substring(0, 100) + (html.length > 100 ? '...' : '')
}

// 获取用户信息
async function loadUser() {
  if (!userId.value) {
    console.error('无效的用户ID')
    return
  }
  
  try {
    const res = await getUserById(userId.value)
    console.log('用户信息API响应:', res)
    
    if (res.status === 'success' && res.data) {
      user.value = {
        avatar: res.data.avatar || '',
        username: res.data.username,
        nickname: res.data.nickname || res.data.username,
        email: res.data.email || '',
        bio: res.data.signature || '',
        createdAt: res.data.createTime || res.data.updateTime,
        articles: [],
        projects: [],
        activities: []
      }
    }
  } catch (err) {
    console.error('加载用户信息失败', err)
  }

  // 加载用户的笔记（文章）
  try {
    const notesRes = await getPrivateNotes({
      userId: userId.value,
      pageNo: pagination.value.pageNo,
      pageSize: pagination.value.pageSize
    }, true) // 第二个参数表示需要传递用户ID
    console.log('用户笔记API响应:', notesRes)
    if (notesRes.status === 'success' && notesRes.data) {
      // 确保每篇文章都有有效的ID
      user.value.articles = notesRes.data.list.map(note => {
        console.log('处理文章数据:', note)
        return {
          id: note.id,
          title: note.title,
          content: note.content,
          createdAt: note.createdTime,
          section: note.sectionId ? `section-${note.sectionId}` : 'default'
        }
      })
      // 更新分页信息
      pagination.value.total = notesRes.data.totalCount
      pagination.value.totalPage = notesRes.data.pageTotal
    }
  } catch (err) {
    console.error('加载用户笔记失败', err)
  }

  // 加载用户项目
  try {
    const res = await getMyProjects({ 
      pageNo: projectPagination.value.pageNo, 
      pageSize: projectPagination.value.pageSize,
      userId: userId.value
    }, true) // 第二个参数表示需要传递用户ID
    if (res.status === 'success' && res.data) {
      user.value.projects = res.data.list || []
      projectPagination.value.total = res.data.totalCount || 0
      projectPagination.value.totalPage = res.data.pageTotal || 1
    }
  } catch (err) {
    console.error('加载用户项目失败', err)
  }
}

// 分页操作函数
const prevPage = () => {
  if (pagination.value.pageNo > 1) {
    pagination.value.pageNo--
    loadUser()
  }
}

const nextPage = () => {
  if (pagination.value.pageNo < pagination.value.totalPage) {
    pagination.value.pageNo++
    loadUser()
  }
}

const goToPage = (page) => {
  if (page !== '...' && page !== pagination.value.pageNo) {
    pagination.value.pageNo = page
    loadUser()
  }
}

const handlePageSizeChange = () => {
  pagination.value.pageNo = 1
  loadUser()
}

// 项目分页操作函数
const prevProjectPage = () => {
  if (projectPagination.value.pageNo > 1) {
    projectPagination.value.pageNo--
    loadUser()
  }
}

const nextProjectPage = () => {
  if (projectPagination.value.pageNo < projectPagination.value.totalPage) {
    projectPagination.value.pageNo++
    loadUser()
  }
}

const goToProjectPage = (page) => {
  if (page !== '...' && page !== projectPagination.value.pageNo) {
    projectPagination.value.pageNo = page
    loadUser()
  }
}

const handleProjectPageSizeChange = () => {
  projectPagination.value.pageNo = 1
  loadUser()
}

// 跳转到项目详情页
const goToProject = (project) => {
  if (project && project.id) {
    // 添加from=userProfile查询参数，标识来源是用户主页
    router.push({
      path: `/MyProjects/${project.id}`,
      query: { 
        from: 'userProfile',
        userId: userId.value // 传递用户ID，以便返回时能回到正确的用户主页
      }
    })
  }
}

// 跳转到文章详情页
const goToArticle = (article) => {
  console.log('点击文章:', article)
  if (article && article.id) {
    // 添加from=userProfile查询参数，标识来源是用户主页
    router.push({
      path: `/techNotes/default/${article.id}`,
      query: { 
        from: 'userProfile',
        userId: userId.value // 传递用户ID，以便返回时能回到正确的用户主页
      }
    })
  }
}

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId, oldId) => {
    if (newId && newId !== oldId) {
      userId.value = newId
      loadUser()
    }
  },
  { immediate: true }
)

onMounted(() => {
  // 初始加载时使用当前路由参数
  if (route.params.id) {
    userId.value = route.params.id
  }
  loadUser()
})
</script>

<style scoped>
/* 优化后的主题样式 */
.dark-theme {
  @apply bg-gray-900 text-white;
  will-change: background-color, color;
}

.light-theme {
  @apply bg-blue-50 text-gray-800;
  will-change: background-color, color;
}

/* 标题发光效果 */
.p3-title-glow {
  text-shadow: 0 0 8px rgba(96, 165, 250, 0.5);
}

.p3-subtitle {
  @apply text-blue-300;
}

/* 动画定义 */
@keyframes pulse {
  0%, 100% { opacity: 0.8; }
  50% { opacity: 0.4; }
}

@keyframes pulse-delayed {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 0.2; }
}

@keyframes ripple {
  0% { transform: scale(1); opacity: 0.8; }
  100% { transform: scale(1.2); opacity: 0; }
}

@keyframes ripple-delayed {
  0% { transform: scale(1); opacity: 0.6; }
  100% { transform: scale(1.15); opacity: 0; }
}

@keyframes bubble {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

@keyframes bubble-delayed {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

@keyframes ripple-smooth {
  0% { transform: scale(1); opacity: 0.6; }
  50% { transform: scale(1.1); opacity: 0.3; }
  100% { transform: scale(1); opacity: 0.6; }
}

@keyframes float-slow {
  0%, 100% { transform: translateY(0) translateX(0); }
  50% { transform: translateY(-20px) translateX(10px); }
}

@keyframes float-medium {
  0%, 100% { transform: translateY(0) translateX(0); }
  50% { transform: translateY(-15px) translateX(5px); }
}

@keyframes float-fast {
  0%, 100% { transform: translateY(0) translateX(0); }
  50% { transform: translateY(-10px) translateX(3px); }
}

.animate-pulse {
  animation: pulse 4s ease-in-out infinite;
}

.animate-pulse-delayed {
  animation: pulse-delayed 5s ease-in-out infinite;
}

.animate-ripple {
  animation: ripple 6s ease-out infinite;
}

.animate-ripple-delayed {
  animation: ripple-delayed 7s ease-out infinite;
}

.animate-bubble {
  animation: bubble 8s ease-in-out infinite;
}

.animate-bubble-delayed {
  animation: bubble-delayed 9s ease-in-out infinite;
}

.animate-ripple-smooth {
  animation: ripple-smooth 8s ease-in-out infinite;
}

.animate-float-slow {
  animation: float-slow 10s ease-in-out infinite;
}

.animate-float-medium {
  animation: float-medium 8s ease-in-out infinite;
}

.animate-float-fast {
  animation: float-fast 6s ease-in-out infinite;
}

/* 文本截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
