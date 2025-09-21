<template>
  <div class="relative p-6 max-w-4xl mx-auto theme-bg-primary">
    <!-- 返回按钮 -->
    <button
      @click="goBack"
      class="absolute top-4 right-4 px-3 py-1.5 theme-bg-translucent rounded-lg theme-shadow hover:theme-shadow-md transition flex items-center space-x-1 backdrop-blur"
      title="返回"
    >
      <span class="text-lg">←</span>
      <span class="text-sm font-medium">
        {{ route.query.from === 'userProfile' ? '返回用户主页' : '返回列表' }}
      </span>
    </button>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-20 theme-text-tertiary">加载中...</div>
    <div v-else-if="error" class="text-center py-20 text-red-500">{{ error }}</div>

    <!-- 内容 -->
    <div v-else>
      <!-- 标题 -->
      <h1 class="text-3xl font-bold mb-4 text-center theme-title">
        {{ note?.title }}
      </h1>

      <!-- 时间信息 -->
      <div
        class="flex flex-wrap justify-center items-center theme-text-secondary text-sm mb-6 gap-6"
      >
        <div class="flex items-center">
          <span class="font-medium mr-1">创建：</span>
          <span>{{ formatDate(note?.createdTime) }}</span>
        </div>
        <div class="flex items-center">
          <span class="font-medium mr-1">更新：</span>
          <span>{{ formatDate(note?.updatedTime) }}</span>
        </div>
      </div>

      <!-- 正文内容 -->
      <div
        v-html="note?.content"
        class="ProseMirror prose prose-blue dark:prose-invert max-w-none leading-relaxed p-4 rounded-lg bg-white/80 dark:bg-gray-800/80 border border-gray-200 dark:border-gray-700"
        style="white-space: pre-wrap;"
      ></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTechNoteById } from '@/api/techNote'

const route = useRoute()
const router = useRouter()

const note = ref(null)
const loading = ref(true)
const error = ref(null)

// 返回按钮
function goBack() {
  // 检查是否来自用户主页
  if (route.query.from === 'userProfile') {
    // 获取查询参数中的用户ID
    const userId = route.query.userId
    
    // 如果有用户ID，返回到该用户的主页
    if (userId) {
      router.push({ 
        path: `/user/${userId}`
      })
    } else {
      // 如果没有用户ID，返回到当前登录用户的主页
      router.push({ 
        path: '/user'
      })
    }
  } else {
    // 默认返回到笔记列表
    router.push({ name: 'TechNoteList', params: { section: route.params.section } })
  }
}

// 格式化时间函数
function formatDate(datetime) {
  if (!datetime) return ''
  const d = new Date(datetime)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
    d.getDate()
  ).padStart(2, '0')}`
}

// 数据加载
onMounted(async () => {
  try {
    const res = await getTechNoteById(route.params.id)
    note.value = res?.data
  } catch (e) {
    error.value = '加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
})
</script>

<style>
/* 优化代码块样式 */
.ProseMirror pre {
  background: var(--code-bg) !important;
  color: var(--code-text) !important;
  padding: 0.75rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  overflow-x: auto;
  border: 1px solid var(--border-color);
  margin: 1rem 0;
}

/* 优化表格样式 */
.ProseMirror table {
  border-collapse: collapse;
  width: 100%;
  margin: 1rem 0;
}
.ProseMirror th, .ProseMirror td {
  border: 1px solid var(--border-color);
  padding: 0.5rem;
}
.ProseMirror th {
  background: var(--bg-tertiary);
}
</style>
