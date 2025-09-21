<template>
  <div :class="['max-w-5xl mx-auto p-6 min-h-screen relative overflow-hidden', isDark ? 'dark-theme' : 'light-theme']">
    <!-- 简化的背景元素 - 同时支持明暗主题 -->
    <div class="fixed inset-0 bg-gradient-to-br z-0"
         :class="isDark ? 'from-blue-900/5 via-indigo-900/3 to-purple-900/2 backdrop-blur-xl' : 
                         'from-blue-100/20 via-cyan-100/15 to-teal-100/10 backdrop-blur-lg'">
    </div>
    
    <!-- 主要光晕元素 -->
    <div class="fixed z-0 rounded-full blur-3xl will-change-transform will-change-opacity"
         :class="isDark ? 'top-0 right-0 w-[30rem] h-[30rem] bg-blue-500/5 animate-pulse-slow' : 
                         'top-20 right-20 w-80 h-80 bg-yellow-300/30 animate-pulse'">
    </div>
    
    <div class="fixed z-0 rounded-full blur-3xl will-change-transform will-change-opacity"
         :class="isDark ? 'bottom-0 left-0 w-[28rem] h-[28rem] bg-purple-500/5 animate-pulse-delayed' : 
                         'bottom-10 left-10 w-72 h-72 bg-orange-300/25 animate-pulse-delayed'">
    </div>
    
    <!-- 中心渐变 -->
    <div class="fixed inset-0 z-0"
         :class="isDark ? 'bg-[radial-gradient(ellipse_80%_80%_at_50%_50%,_rgba(120,119,198,0.3)_0%,_transparent_60%)]' : 
                         'bg-[radial-gradient(ellipse_80%_80%_at_50%_50%,_rgba(191,219,254,0.3)_0%,_transparent_60%)]'">
    </div>
    
    <!-- 底部渐变 -->
    <div class="fixed bottom-0 left-0 right-0 h-40 bg-gradient-to-t z-0"
         :class="isDark ? 'from-blue-900/10 to-transparent' : 'from-blue-300/15 to-transparent'">
    </div>
    
    <!-- 装饰性小光点 - 使用伪元素减少DOM节点 -->
    <div class="fixed inset-0 z-0 particles-container" :class="isDark ? 'dark-particles' : 'light-particles'"></div>
    
    <div class="relative z-10">
    <!-- 标题、筛选与上传按钮 - 固定位置 -->
    <div class="sticky top-0 bg-opacity-90 backdrop-blur-md z-20 py-4 mb-6"
         :class="isDark ? 'bg-gray-900/70' : 'bg-white/80'">
      <div class="flex flex-col md:flex-row md:justify-between md:items-center gap-4">
        <h1 :class="['text-4xl font-bold p3-title-glow', 
                    isDark ? 'text-white' : 'text-blue-900']">项目列表</h1>
        
        <div class="flex items-center gap-3">
          <a-button v-if="isLoggedIn" @click="showModal = true" 
                   class="upload-btn p3-btn-primary"
                   :class="isDark ? 'bg-gradient-to-r from-indigo-600 to-blue-600 hover:from-indigo-700 hover:to-blue-700' :
                                   'bg-gradient-to-r from-indigo-500 to-blue-500 hover:from-indigo-600 hover:to-blue-600'">
            <template #icon><PlusOutlined /></template>
            上传项目
          </a-button>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <a-skeleton active :paragraph="{ rows: 3 }" v-for="i in 4" :key="i" class="bg-white dark:bg-gray-700/90 rounded-lg p-5" />
    </div>

    <!-- 时间线项目列表 -->
    <div v-else-if="projects.length" class="timeline-container">
      <div class="timeline-line"></div>
      <div 
        v-for="(project, index) in projects" 
        :key="project.id"
        :class="['timeline-item', index % 2 === 0 ? 'timeline-item-left' : 'timeline-item-right']"
      >
        <div class="timeline-dot"></div>
        <a-card 
          hoverable
          :class="['transition-all duration-300 project-card timeline-card', 
                  isDark ? 'bg-gray-800/90 backdrop-blur-xl border-blue-500/20 hover:shadow-blue-500/30' : 
                          'bg-white/95 backdrop-blur-lg border-blue-300/30 hover:shadow-blue-400/20']"
        >
          <template #extra>
            <a-popconfirm
              v-if="isLoggedIn"
              title="确定要删除该项目吗？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(project.id)"
            >
              <a-button danger type="text">
                <template #icon><DeleteOutlined /></template>
              </a-button>
            </a-popconfirm>
          </template>
          
          <template #title>
            <router-link :to="`/MyProjects/${project.id}`"
              class="text-blue-600 dark:text-blue-300 font-semibold text-xl hover:underline dark:drop-shadow-sm">
              {{ project.name }}
            </router-link>
          </template>
          


          <div class="flex items-center justify-between text-gray-600 dark:text-gray-300 text-xs mt-3 dark:drop-shadow-sm">
            <span class="text-gray-500 dark:text-gray-400 text-sm">{{ project.type }}</span>
            <div class="flex flex-col items-end">
              <span class="text-blue-500 dark:text-blue-300 font-medium text-sm">
                {{ formatDate(project.updateTime) }}
              </span>
              <span class="text-gray-400 dark:text-gray-500 text-xs mt-1">
                <ClockCircleOutlined class="mr-1" />更新时间
              </span>
            </div>
          </div>
        </a-card>
      </div>
    </div>

    <!-- 空状态 -->
    <a-empty 
      v-else 
      description="暂无项目，快来查看或上传吧～"
      class="my-16"
    >
      <template #extra>
        <a-button v-if="isLoggedIn" type="primary" @click="showModal = true">
          上传项目
        </a-button>
      </template>
    </a-empty>

    <!-- 分页 -->
    <div class="flex justify-center mt-8">
      <a-pagination
        v-if="totalPages > 1"
        v-model:current="pageNo"
        :total="totalCount"
        :pageSize="pageSize"
        @change="changePage"
        show-quick-jumper
        :show-total="total => `共 ${total} 个项目`"
      />
    </div>

    <!-- 上传弹窗 -->
    <ProjectUploadModal v-model:open="showModal" @submit="handleSubmit" v-if="isLoggedIn" />
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick, onBeforeMount } from 'vue'
import ProjectUploadModal from '../components/ProjectUploadModal.vue'
import { getMyProjects, getPublicProjects, deleteProject } from '@/api/project'
import { deleteProjectFiles } from '@/api/projectFile'
import { message } from 'ant-design-vue'
import { 
  PlusOutlined, 
  DeleteOutlined, 
  ClockCircleOutlined 
} from '@ant-design/icons-vue'
import { parseJwt } from '@/utils/auth' // 添加JWT解析工具

// 初始化CSS变量
onBeforeMount(() => {
  // 设置主题相关CSS变量
  document.documentElement.style.setProperty('--color-primary', '#1890ff');
  document.documentElement.style.setProperty('--color-primary-light', '#40a9ff');
  document.documentElement.style.setProperty('--color-primary-dark', '#096dd9');
  
  // 设置标签颜色RGB值，用于阴影效果
  document.documentElement.style.setProperty('--tag-color-rgb', '59, 130, 246');
})

// 上传弹窗显示
const showModal = ref(false)
// 项目列表
const projects = ref([])
// 加载状态
const loading = ref(false)

// 分页信息
const pageNo = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value))

// 判断是否登录（这里假设有全局状态或 localStorage 里存 token）
const isLoggedIn = ref(!!localStorage.getItem('token'))



// 获取项目列表
async function fetchProjects(page = 1) {
  loading.value = true
  try {
    let res
    const params = { 
      pageNo: page, 
      pageSize: pageSize.value
    }

    if (isLoggedIn.value) {
      // 从JWT token中获取用户ID
      const token = localStorage.getItem('token')
      if (token) {
        const decoded = parseJwt(token)
        if (decoded && decoded.id) {
          params.userId = decoded.id
        }
      }
      res = await getMyProjects(params)
    } else {
      res = await getPublicProjects(params)
    }

    projects.value = res.data?.list || []
    totalCount.value = res.data?.totalCount || 0
    pageNo.value = page
  } catch (error) {
    message.error('获取项目列表失败')
    console.error('获取项目列表失败:', error)
    projects.value = []
  } finally {
    loading.value = false
  }
}

// 切换分页
function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    fetchProjects(page)
  }
}



// 上传完成刷新列表
async function handleSubmit(project) {
  showModal.value = false
  message.success('项目上传成功')
  await fetchProjects(1) // 返回第一页查看新项目
}

// 日期格式化
function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const options = { year: 'numeric', month: 'short', day: 'numeric' }
  return d.toLocaleDateString(undefined, options)
}

// 获取标签颜色
function getTagColor(type) {
  const colorMap = {
    'Web': 'blue',
    'Mobile': 'green',
    'Desktop': 'purple',
    'API': 'orange',
    'Library': 'cyan',
    'Other': 'default'
  }
  return colorMap[type] || 'default'
}

// 删除项目
async function handleDelete(id) {
  loading.value = true
  try {
    await deleteProjectFiles(id)
    await deleteProject(id)
    message.success('删除成功')
    
    // 如果当前页没有数据了且不是第一页，则返回上一页
    if (projects.value.length === 1 && pageNo.value > 1) {
      fetchProjects(pageNo.value - 1)
    } else {
      fetchProjects(pageNo.value)
    }
  } catch (err) {
    console.error(err)
    message.error('删除失败，请检查权限或后端日志')
  } finally {
    loading.value = false
  }
}

// 主题状态管理
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

// 初始化
onMounted(() => {
  initTheme()
  fetchProjects().then(() => {
    nextTick(() => {
      setupTimelineAnimation()
    })
  })
  
  // 监听来自其他组件的主题变化事件
  window.addEventListener('themeChanged', (event) => {
    isDark.value = event.detail.isDark
    document.documentElement.classList.toggle('dark', isDark.value)
  })
})

// 设置时间线动画
function setupTimelineAnimation() {
  // 确保DOM元素存在
  nextTick(() => {
    const timelineItems = document.querySelectorAll('.timeline-item')
    if (timelineItems.length === 0) return
    
    const observerOptions = {
      threshold: 0.3,
      rootMargin: '0px 0px -50px 0px'
    }

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible')
          observer.unobserve(entry.target)
        }
      })
    }, observerOptions)

    // 观察所有时间线项目
    timelineItems.forEach(item => {
      observer.observe(item)
    })
  })
}
</script>

<style scoped>
/* 暗色模式下的卡片样式调整 - 统一使用Tailwind类 */
:deep(.ant-card-bordered) {
  @apply border border-gray-200 dark:border-gray-700;
}

:deep(.ant-card-head) {
  @apply border-b border-gray-200 bg-gray-50/90 dark:border-gray-700 dark:bg-gray-800/90;
}

:deep(.ant-card-head-title),
:deep(.ant-card-extra) {
  @apply text-gray-800 dark:text-gray-100;
}

:deep(.ant-card-body) {
  @apply bg-white/95 text-gray-700 dark:bg-gray-700/90 dark:text-gray-200;
  backdrop-filter: blur(8px);
}

/* 卡片特效 - 使用CSS变量简化明暗主题切换 */
:root {
  --card-bg-light: linear-gradient(145deg, rgba(255, 255, 255, 0.95), rgba(248, 250, 252, 0.98));
  --card-bg-dark: linear-gradient(145deg, rgba(39, 49, 65, 0.95), rgba(25, 35, 45, 0.98));
  --card-shadow-light: 0 8px 20px rgba(0, 0, 0, 0.08);
  --card-shadow-dark: 0 8px 20px rgba(0, 0, 0, 0.25);
  --card-border-light: 1px solid rgba(226, 232, 240, 0.8);
  --card-border-dark: 1px solid rgba(65, 75, 89, 0.5);
  
  --card-hover-bg-light: linear-gradient(145deg, rgba(255, 255, 255, 1), rgba(248, 250, 252, 1));
  --card-hover-bg-dark: linear-gradient(145deg, rgba(39, 49, 65, 0.9), rgba(30, 41, 59, 0.95));
  --card-hover-shadow-light: 0 12px 25px rgba(59, 130, 246, 0.15);
  --card-hover-shadow-dark: 0 12px 25px rgba(30, 64, 175, 0.25);
  --card-hover-border-light: 1px solid rgba(203, 213, 224, 1);
  --card-hover-border-dark: 1px solid rgba(59, 130, 246, 0.4);
}

.project-card {
  background: var(--card-bg-light);
  box-shadow: var(--card-shadow-light);
  border: var(--card-border-light);
  position: relative;
  overflow: hidden;
}

.project-card:hover {
  background: var(--card-hover-bg-light);
  box-shadow: var(--card-hover-shadow-light);
  border: var(--card-hover-border-light);
}

.dark .project-card {
  background: var(--card-bg-dark);
  box-shadow: var(--card-shadow-dark);
  border: var(--card-border-dark);
}

.dark .project-card:hover {
  background: var(--card-hover-bg-dark);
  box-shadow: var(--card-hover-shadow-dark);
  border: var(--card-hover-border-dark);
}

/* 标签效果 - 减少过度装饰，提高可读性 */
.dark .ant-tag {
  text-shadow: 0 0 2px currentColor;
  box-shadow: 0 0 4px rgba(var(--tag-color-rgb, 59, 130, 246), 0.3);
  transition: all 0.3s ease;
}

.dark .ant-tag:hover {
  box-shadow: 0 0 6px rgba(var(--tag-color-rgb, 59, 130, 246), 0.4);
}

/* 增强暗色模式下的文字可读性 - 使用Tailwind类 */
.dark :deep(.ant-card-body) {
  @apply text-gray-100 antialiased;
}

.dark :deep(.ant-card-head-title) {
  @apply text-white;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}

/* Ant Design 组件暗色模式统一样式 */
:deep(.ant-empty-description) {
  @apply text-gray-500 dark:text-gray-400;
}

:deep(.ant-pagination-item) {
  @apply bg-white border-gray-200 text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-300;
}

:deep(.ant-pagination-item-active) {
  @apply bg-blue-500 border-blue-500 text-white dark:bg-blue-600 dark:border-blue-600;
}

:deep(.ant-pagination-item:hover) {
  @apply border-blue-500 text-blue-500 dark:border-blue-400 dark:text-blue-400;
}

:deep(.ant-select-dropdown) {
  @apply bg-white text-gray-800 shadow-lg dark:bg-gray-800 dark:text-gray-200 dark:border dark:border-gray-700;
}

:deep(.ant-select-item) {
  @apply text-gray-700 dark:text-gray-300;
}

:deep(.ant-select-item-option-active) {
  @apply bg-gray-100 dark:bg-gray-700;
}

:deep(.ant-select-item-option-selected) {
  @apply bg-blue-50 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400;
}

/* 统一卡片悬停效果 */
.ant-card-hoverable:hover {
  transform: translateY(-4px);
  transition: all 0.3s ease;
}

/* 文本截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 时间线样式 */
.timeline-container {
  position: relative;
  padding: 20px 0;
  margin: 0 auto;
  max-width: 1200px;
}

.timeline-line {
  position: absolute;
  left: 50%;
  top: 0;
  bottom: 0;
  width: 4px;
  background: linear-gradient(to bottom, #3b82f6, #6366f1, #8b5cf6);
  transform: translateX(-50%);
  border-radius: 2px;
  box-shadow: 0 0 8px rgba(59, 130, 246, 0.3);
}

.timeline-item {
  position: relative;
  margin-bottom: 40px;
  width: 100%;
}

.timeline-dot {
  position: absolute;
  top: 20px;
  width: 16px;
  height: 16px;
  background: #3b82f6;
  border: 3px solid white;
  border-radius: 50%;
  z-index: 2;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.2);
}

.dark .timeline-dot {
  border-color: #1e293b;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.4);
}

/* 简化时间线布局 - 确保蓝点正确对齐 */
.timeline-item {
  position: relative;
  min-height: 120px;
}

.timeline-item-left {
  padding-right: 50%;
}

.timeline-item-right {
  padding-left: 50%;
}

.timeline-card {
  width: calc(50% - 30px);
  position: relative;
  z-index: 1;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  overflow: hidden;
  border: none;
  min-height: 180px;
  will-change: transform, box-shadow;
}

.timeline-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.12);
}

.timeline-item-left .timeline-card {
  float: right;
  margin-right: 30px;
}

.timeline-item-right .timeline-card {
  float: left;
  margin-left: 30px;
}

/* 增加卡片间距 */
.timeline-item {
  margin-bottom: 50px;
}

/* 统一卡片设计 - 使用CSS变量简化 */
.timeline-card {
  @apply rounded-xl transition-all duration-300;
  background: var(--card-bg-light);
  border: var(--card-border-light);
  box-shadow: var(--card-shadow-light);
}

.dark .timeline-card {
  background: var(--card-bg-dark);
  border: var(--card-border-dark);
  box-shadow: var(--card-shadow-dark);
}

.timeline-card:hover {
  @apply transform -translate-y-1;
  background: var(--card-hover-bg-light);
  box-shadow: var(--card-hover-shadow-light);
  border: var(--card-hover-border-light);
}

.dark .timeline-card:hover {
  background: var(--card-hover-bg-dark);
  box-shadow: var(--card-hover-shadow-dark);
  border: var(--card-hover-border-dark);
}

/* 卡片样式 - 统一使用Tailwind类，减少重复定义 */
.timeline-card :deep(.ant-card-head) {
  @apply border-b border-gray-200 bg-gradient-to-r from-gray-50 to-gray-100 py-5 px-6 dark:border-gray-700/30 dark:from-gray-800 dark:to-gray-800;
}

/* 卡片内容区域 */
.timeline-card :deep(.ant-card-body) {
  @apply p-4 sm:p-6;
}

/* 文字样式 */
.timeline-card :deep(.ant-card-head-title) {
  @apply text-gray-800 font-bold text-lg tracking-tight dark:text-gray-100;
}

.timeline-card :deep(.ant-card-body) {
  @apply text-gray-600 font-medium dark:text-gray-300;
}

/* 提高文字对比度 - 使用CSS变量替代!important */
:root {
  --text-gray-300: #d1d5db;
  --text-gray-400: #9ca3af;
  --text-gray-500: #6b7280;
  
  --dark-text-gray-300: rgba(237, 242, 247, 0.9);
  --dark-text-gray-400: rgba(203, 213, 224, 0.8);
  --dark-text-gray-500: rgba(160, 174, 192, 0.7);
}

.text-gray-300 {
  color: var(--text-gray-300);
}

.text-gray-400 {
  color: var(--text-gray-400);
}

.text-gray-500 {
  color: var(--text-gray-500);
}

.dark .text-gray-300 {
  color: var(--dark-text-gray-300);
}

.dark .text-gray-400 {
  color: var(--dark-text-gray-400);
}

.dark .text-gray-500 {
  color: var(--dark-text-gray-500);
}

/* 精确控制蓝点位置 */
.timeline-item-left .timeline-dot {
  right: calc(50% - 8px);
}

.timeline-item-right .timeline-dot {
  left: calc(50% - 8px);
}

/* 响应式调整 - 改进移动端体验 */
@media (max-width: 768px) {
  .timeline-line {
    left: 20px;
    width: 3px;
  }
  
  .timeline-dot {
    left: 16px !important;
    right: auto !important;
    width: 14px;
    height: 14px;
  }
  
  .timeline-card {
    width: calc(100% - 50px);
    margin-left: 50px !important;
    margin-right: 0 !important;
    min-height: 150px;
  }
  
  .timeline-item-left,
  .timeline-item-right {
    padding-left: 0;
    padding-right: 0;
  }
  
  .timeline-item-left .timeline-card,
  .timeline-item-right .timeline-card {
    margin-left: 50px;
    margin-right: 0;
    float: none;
  }
  
  .timeline-item {
    margin-bottom: 30px;
  }
}

/* 时间线动画 */
.timeline-item {
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.6s ease;
}

.timeline-item.visible {
  opacity: 1;
  transform: translateY(0);
}

.timeline-item-left {
  animation: slideInLeft 0.6s ease forwards;
}

.timeline-item-right {
  animation: slideInRight 0.6s ease forwards;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 上传按钮样式 - 移除!important，使用Tailwind类和CSS变量 */
.upload-btn {
  background: linear-gradient(135deg, var(--color-primary, #1890ff) 0%, var(--color-primary-dark, #096dd9) 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.4);
  font-weight: 600;
  transition: all 0.3s ease;
}

.upload-btn:hover {
  background: linear-gradient(135deg, var(--color-primary-light, #40a9ff) 0%, var(--color-primary, #1890ff) 100%);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.5);
  transform: translateY(-2px);
}

.dark .upload-btn {
  background: linear-gradient(135deg, var(--color-primary, #1890ff) 0%, var(--color-primary-dark, #0050b3) 100%);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.6);
}

.dark .upload-btn:hover {
  background: linear-gradient(135deg, var(--color-primary-light, #40a9ff) 0%, var(--color-primary-dark, #096dd9) 100%);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.8);
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 新增动画效果 - 从Setting.vue学习 */
/* 简化的动画定义 */
@keyframes pulse-slow {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.05);
  }
}

@keyframes pulse-delayed {
  0%, 100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.08);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 标准动画类 */
.animate-pulse {
  animation: pulse-slow 4s ease-in-out infinite;
}

.animate-pulse-slow {
  animation: pulse-slow 6s ease-in-out infinite;
}

.animate-pulse-delayed {
  animation: pulse-delayed 5s ease-in-out infinite;
  animation-delay: 2s;
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}

/* 粒子背景样式 */
.particles-container {
  pointer-events: none;
}

.light-particles::before,
.light-particles::after {
  content: '';
  position: absolute;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  opacity: 0.5;
  animation: float 8s ease-in-out infinite;
}

.light-particles::before {
  top: 20%;
  left: 20%;
  background-color: rgba(59, 130, 246, 0.6);
  box-shadow: 
    30vw 10vh 0 rgba(14, 165, 233, 0.5),
    80vw 40vh 0 rgba(6, 182, 212, 0.4),
    20vw 70vh 0 rgba(59, 130, 246, 0.6),
    70vw 80vh 0 rgba(14, 165, 233, 0.5);
  animation-delay: 0s;
}

.light-particles::after {
  top: 40%;
  left: 60%;
  background-color: rgba(6, 182, 212, 0.5);
  box-shadow: 
    10vw 20vh 0 rgba(59, 130, 246, 0.5),
    60vw 60vh 0 rgba(14, 165, 233, 0.4),
    40vw 30vh 0 rgba(6, 182, 212, 0.6),
    80vw 10vh 0 rgba(59, 130, 246, 0.5);
  animation-delay: 2s;
  animation-duration: 10s;
}

.dark-particles::before,
.dark-particles::after {
  content: '';
  position: absolute;
  width: 3px;
  height: 3px;
  border-radius: 50%;
  opacity: 0.4;
  animation: float 8s ease-in-out infinite;
}

.dark-particles::before {
  top: 25%;
  left: 15%;
  background-color: rgba(96, 165, 250, 0.4);
  box-shadow: 
    35vw 15vh 0 rgba(125, 211, 252, 0.3),
    75vw 35vh 0 rgba(103, 232, 249, 0.3),
    25vw 65vh 0 rgba(96, 165, 250, 0.4),
    65vw 75vh 0 rgba(125, 211, 252, 0.3);
  animation-delay: 1s;
}

.dark-particles::after {
  top: 45%;
  left: 55%;
  background-color: rgba(103, 232, 249, 0.3);
  box-shadow: 
    15vw 25vh 0 rgba(96, 165, 250, 0.3),
    55vw 55vh 0 rgba(125, 211, 252, 0.3),
    45vw 35vh 0 rgba(103, 232, 249, 0.4),
    85vw 15vh 0 rgba(96, 165, 250, 0.3);
  animation-delay: 3s;
  animation-duration: 12s;
}

/* 标题和按钮样式 - 使用CSS变量和Tailwind类 */
:root {
  --title-glow-light: 0 0 15px rgba(59, 130, 246, 0.4), 0 0 30px rgba(59, 130, 246, 0.2);
  --title-glow-dark: 0 0 15px rgba(96, 165, 250, 0.5), 0 0 30px rgba(96, 165, 250, 0.25);
  --title-color-light: #1e40af;
  --title-color-dark: #ffffff;
  
  --btn-shadow-light: 0 4px 12px rgba(59, 130, 246, 0.3);
  --btn-shadow-dark: 0 4px 12px rgba(59, 130, 246, 0.5);
  --btn-hover-shadow-light: 0 6px 16px rgba(59, 130, 246, 0.4);
  --btn-hover-shadow-dark: 0 6px 16px rgba(59, 130, 246, 0.6);
}

.p3-title-glow {
  @apply font-bold font-sans;
  text-shadow: var(--title-glow-light);
  color: var(--title-color-light);
}

.dark-theme .p3-title-glow {
  text-shadow: var(--title-glow-dark);
  color: var(--title-color-dark);
}

.p3-btn-primary {
  @apply py-3 px-6 rounded-xl font-semibold transition-all duration-300 border-0 cursor-pointer backdrop-blur-md;
  box-shadow: var(--btn-shadow-light);
}

.dark-theme .p3-btn-primary {
  box-shadow: var(--btn-shadow-dark);
}

.p3-btn-primary:hover {
  @apply transform -translate-y-1;
  box-shadow: var(--btn-hover-shadow-light);
}

.dark-theme .p3-btn-primary:hover {
  box-shadow: var(--btn-hover-shadow-dark);
}
</style>