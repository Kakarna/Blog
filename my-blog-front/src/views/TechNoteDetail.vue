<template>
  <div class="p-6 max-w-4xl mx-auto">
    <h1 class="text-3xl font-bold mb-4">{{ note?.title }}</h1>

    <!-- 时间信息：同一行显示 -->
    <div class="flex flex-wrap items-center text-gray-500 text-sm mb-4 space-x-4">
      <div class="flex items-center">
        <span class="font-medium mr-1">创建：</span>
        <span>{{ formatDate(note?.createdTime) }}</span>
      </div>
      <div class="flex items-center">
        <span class="font-medium mr-1">更新：</span>
        <span>{{ formatDate(note?.updatedTime) }}</span>
      </div>
    </div>

    <!-- 内容 -->
    <div 
      v-html="note?.content" 
      class="ProseMirror prose dark:prose-invert max-w-none"
      style="text-align: left; white-space: pre-wrap;"
    ></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTechNoteById } from '@/api/techNote'

const route = useRoute()
const note = ref(null)

// 格式化时间函数：只保留 YYYY-MM-DD
function formatDate(datetime) {
  if (!datetime) return ''
  return new Date(datetime).toISOString().split('T')[0]
}

onMounted(async () => {
  const res = await getTechNoteById(route.params.id)
  note.value = res?.data
})
</script>
