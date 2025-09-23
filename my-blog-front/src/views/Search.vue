<template>
  <div :class="['max-w-5xl mx-auto px-6 py-8 min-h-screen', isDark ? 'dark-theme' : 'light-theme']">
    <!-- 顶部标题和标签栏 -->
    <div :class="['p-6 rounded-lg shadow-sm mb-8 transition-colors duration-300',
      isDark ? 'bg-gray-800/60 border border-gray-700' : 'bg-white border border-gray-200']">
      <h2 :class="['text-2xl font-bold mb-6', isDark ? 'text-gray-100' : 'text-gray-800']">
        搜索: "{{ keyword }}"
      </h2>
      
      <div class="flex space-x-2">
        <button 
          v-for="tab in tabs" 
          :key="tab.value"
          @click="changeTab(tab.value)"
          :class="[ 
            'px-4 py-2 rounded-md transition-all font-medium',
            activeTab === tab.value 
              ? (isDark ? 'bg-blue-600 text-white shadow-md' : 'bg-blue-500 text-white shadow-md')
              : (isDark ? 'bg-gray-700 text-gray-300 hover:bg-gray-600' : 'bg-gray-100 text-gray-700 hover:bg-gray-200')
          ]"
        >
          {{ tab.label }}
        </button>
      </div>
    </div>

    <!-- 加载中 -->
    <section v-if="loading" :class="['text-center py-12 rounded-lg shadow-sm transition-colors duration-300',
      isDark ? 'bg-gray-800/60 border border-gray-700' : 'bg-white border border-gray-200']">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600 mx-auto"></div>
      <p class="mt-4 text-gray-600 font-medium">正在搜索...</p>
    </section>

    <!-- 有结果 -->
    <section v-else-if="results.length" class="space-y-6">
      <!-- 用户搜索结果 -->
      <div v-if="activeTab === 'user'">
        <h3 :class="['text-xl font-semibold mb-4', isDark ? 'text-gray-200' : 'text-gray-800']">用户</h3>
        <ul class="grid gap-4">
          <li v-for="item in results" :key="item.id" 
            :class="['p-5 rounded-lg shadow-sm hover:shadow-md transition-all duration-300',
              isDark ? 'bg-gray-800/60 border border-gray-700 hover:border-blue-500' : 'bg-white border border-gray-200 hover:border-blue-300']">
            <div class="flex items-center space-x-4">
              <!-- 用户头像和下拉菜单 -->
              <div class="relative" ref="userMenuRef">
                <div class="flex items-center gap-3 cursor-pointer" @click="toggleUserMenu(item.id)">
                  <img 
                    :src="item.avatar || '/images/default-avatar.png'" 
                    :class="['w-14 h-14 rounded-full object-cover border-2 shadow-sm cursor-pointer',
                      isDark ? 'border-gray-600' : 'border-gray-200']" 
                    alt="avatar"
                    :aria-expanded="activeUserMenu === item.id"
                    @click.stop="goToUserProfile(item.id)"
                  />
                  <div class="flex-1">
                    <h4 :class="['font-semibold text-lg', isDark ? 'text-gray-200' : 'text-gray-800']">{{ item.nickname }}</h4>
                    <p :class="['text-sm', isDark ? 'text-gray-400' : 'text-gray-500']">{{ item.email }}</p>
                  </div>
                </div>

                <!-- 用户菜单 -->
                <transition name="fade-scale">
                  <div 
                    v-if="activeUserMenu === item.id"
                    class="absolute left-0 mt-2 w-64 bg-white text-gray-800 border rounded-lg shadow-lg py-3 z-50"
                  >
                    <div class="absolute left-3 -top-2 w-3 h-3 bg-white border-l border-t rotate-45"></div>

                    <!-- 用户信息头部 -->
                    <div class="px-4 py-2 border-b mb-2">
                      <div class="flex items-center gap-3">
                        <img :src="item.avatar || '/images/default-avatar.png'"
                          class="w-12 h-12 rounded-full border border-gray-200" />
                        <div>
                          <div class="font-medium">{{ item.nickname }}</div>
                          <div class="text-xs text-gray-500">{{ item.email }}</div>
                        </div>
                      </div>
                    </div>

                    <router-link 
                      :to="{ name: 'UserHome', params: { id: item.id } }"
                      class="flex items-center gap-2 px-4 py-2 hover:bg-gray-100 transition"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                        <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                      </svg>
                      <span>查看主页</span>
                    </router-link>

                    <div class="border-t my-1"></div>

                    <a 
                      href="#" 
                      class="flex items-center gap-2 px-4 py-2 hover:bg-gray-100 transition"
                      @click.prevent="sendMessage(item.id)"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                        <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                      </svg>
                      <span>发送消息</span>
                    </a>
                  </div>
                </transition>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <!-- 技术笔记搜索结果 -->
      <div v-else-if="activeTab === 'note'">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">技术笔记</h3>
        <ul class="grid gap-4">
          <li v-for="item in results" :key="item.id" class="p-5 bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow">
            <router-link
              :to="`/techNotes/${item.sectionId}/${item.id}`"
              class="text-blue-600 hover:text-blue-800 font-semibold text-lg transition-colors"
            >
              {{ item.title }}
            </router-link>
            <p class="text-gray-500 mt-2 text-sm line-clamp-2">{{ item.content }}</p>
            <div class="flex items-center justify-between mt-4 text-sm text-gray-500">
              <span>更新于：{{ formatDate(item.updatedTime) }}</span>
              <span>作者：{{ item.nickname }}</span>
            </div>
          </li>
        </ul>
      </div>

      <!-- 项目搜索结果 -->
      <div v-else-if="activeTab === 'project'">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">项目</h3>
        <ul class="grid gap-4">
          <li v-for="item in results" :key="item.id" class="p-5 bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow">
            <router-link
              :to="`/project/${item.id}`"
              class="text-blue-600 hover:text-blue-800 font-semibold text-lg transition-colors"
            >
              {{ item.name }}
            </router-link>
            <p class="text-gray-500 mt-2 text-sm line-clamp-2">{{ item.description }}</p>
            <div class="flex items-center justify-between mt-4 text-sm text-gray-500">
              <span>创建于：{{ formatDate(item.createdTime) }}</span>
              <span v-if="item.isPublic" class="bg-green-100 text-green-800 px-3 py-1 rounded-full text-xs font-medium">
                公开项目
              </span>
              <span v-else class="bg-gray-100 text-gray-800 px-3 py-1 rounded-full text-xs font-medium">
                私有项目
              </span>
            </div>
          </li>
        </ul>
      </div>
    </section>

    <!-- 没结果 -->
    <section v-else :class="['text-center py-16 rounded-lg shadow-sm', isDark ? 'bg-gray-800/60' : 'bg-white']">
      <svg xmlns="http://www.w3.org/2000/svg" :class="['h-16 w-16 mx-auto', isDark ? 'text-gray-500' : 'text-gray-400']" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <p :class="['mt-4 font-medium text-lg', isDark ? 'text-gray-300' : 'text-gray-500']">没有找到相关内容</p>
    </section>

    <!-- 分页控件 -->
    <div v-if="results.length > 0 && total > pageSize" class="flex justify-center mt-10">
      <div class="flex space-x-2">
        <button
          @click="prevPage"
          :disabled="pageNo === 1"
          :class="[
            'px-4 py-2 rounded-md border font-medium',
            pageNo === 1 
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white text-gray-700 hover:bg-gray-50 shadow-sm'
          ]"
          title="上一页"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>
        
        <span class="px-4 py-2 bg-white border rounded-md text-gray-700 font-medium shadow-sm">
          第 {{ pageNo }} 页 / 共 {{ Math.max(1, Math.ceil(total / pageSize)) }} 页
        </span>
        
        <button
          @click="nextPage"
          :disabled="pageNo * pageSize >= total"
          :class="[
            'px-4 py-2 rounded-md border font-medium',
            pageNo * pageSize >= total
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white text-gray-700 hover:bg-gray-50 shadow-sm'
          ]"
          title="下一页"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()

// 主题状态
const isDark = ref(false)
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

// 监听主题变化
onMounted(() => {
  initTheme()
  window.addEventListener('themeChanged', (event) => {
    isDark.value = event.detail.isDark
  })
})
import { unifiedSearch } from '@/api/search'

const route = useRoute()
const keyword = ref(route.query.q || '')
const type = ref(route.query.type || 'user')   // 默认用户
const pageNo = ref(parseInt(route.query.pageNo) || 1)
const pageSize = ref(parseInt(route.query.pageSize) || 10)
const activeTab = ref(type.value)
const results = ref([])
const total = ref(0)
const loading = ref(false)

// tab 只保留三类
const tabs = [
  { label: '用户', value: 'user' },
  { label: '笔记', value: 'note' },
  { label: '项目', value: 'project' }
]

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN', { hour12: false })
}

// 用户菜单功能
const activeUserMenu = ref(null)
const userMenuRef = ref(null)

const toggleUserMenu = (userId) => {
  activeUserMenu.value = activeUserMenu.value === userId ? null : userId
}

const goToUserProfile = (userId) => {
  router.push({
    name: 'UserHome',
    params: { id: userId },
    query: { from: 'search' }
  })
}

const handleClickOutsideUserMenu = (e) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target)) {
    activeUserMenu.value = null
  }
}

const sendMessage = (userId) => {
  console.log('发送消息给用户:', userId)
  activeUserMenu.value = null
}

onMounted(() => {
  document.addEventListener('click', handleClickOutsideUserMenu)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutsideUserMenu)
})

// 切换标签
const changeTab = (tab) => {
  activeTab.value = tab
  pageNo.value = 1
  search()
}

// 上一页
const prevPage = () => {
  if (pageNo.value > 1) {
    pageNo.value--
    search()
  }
}

// 下一页
const nextPage = () => {
  if (pageNo.value * pageSize.value < total.value) {
    pageNo.value++
    search()
  }
}

// 搜索函数
const search = async () => {
  if (!keyword.value.trim()) {
    results.value = []
    return
  }

  loading.value = true
  try {
    const res = await unifiedSearch({
      keyword: keyword.value,
      type: activeTab.value,
      pageNo: pageNo.value,
      pageSize: pageSize.value
    })
    
    if (res.status === 'success') {
      results.value = res.data.list || res.data
      total.value = res.data.totalCount || res.data.length || 0
    } else {
      results.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('搜索失败:', error)
    results.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 监听路由参数变化
watch(() => route.query, (newQuery) => {
  keyword.value = newQuery.q || ''
  type.value = newQuery.type || 'user'
  pageNo.value = parseInt(newQuery.pageNo) || 1
  pageSize.value = parseInt(newQuery.pageSize) || 10
  activeTab.value = type.value
  search()
})

onMounted(() => {
  if (keyword.value) {
    search()
  }
})
</script>

<style scoped>
.dark-theme {
  --tw-text-opacity: 1;
  color: rgba(229, 231, 235, var(--tw-text-opacity));
  background-color: rgba(17, 24, 39, 0.95);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.light-theme {
  --tw-text-opacity: 1;
  color: rgba(31, 41, 55, var(--tw-text-opacity));
  background-color: rgba(255, 255, 255, 0.98);
}

.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 150ms ease;
  transform-origin: top left;
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
