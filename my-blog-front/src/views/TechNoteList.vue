<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute, RouterLink } from 'vue-router'
import { getTechNoteList, deleteTechNoteById } from '@/api/techNote'
import {
  PlusIcon,
  PencilIcon,
  TrashIcon,
  ArrowUpIcon,
  ArrowDownIcon
} from '@heroicons/vue/outline'

const route = useRoute()
const section = ref(route.params.section)

const allNotes = ref([])  // 保存所有笔记
const pageNo = ref(1)
const pageSize = ref(5)
const totalCount = ref(0)
const pageTotal = ref(1)

// 排序字段：title / createdTime
const sortBy = ref('title')
// 排序方向：asc / desc
const sortOrder = ref('asc')

const loadNotes = async () => {
  const res = await getTechNoteList({
    sectionName: section.value,
    pageNo: 1,       // 固定 1，拿全量
    pageSize: 9999
  })
  allNotes.value = res?.data?.list || []
  totalCount.value = allNotes.value.length
  pageTotal.value = Math.ceil(totalCount.value / pageSize.value)
}

// 排序 + 分页
const sortedNotes = computed(() => {
  let arr = [...allNotes.value]

  // 排序逻辑
  if (sortBy.value === 'title') {
    arr.sort((a, b) => {
      const titleA = a.title.trim()
      const titleB = b.title.trim()
      const matchA = titleA.match(/^\d+/)
      const matchB = titleB.match(/^\d+/)
      const numA = matchA ? parseInt(matchA[0], 10) : null
      const numB = matchB ? parseInt(matchB[0], 10) : null
      let result = 0
      if (numA !== null && numB !== null) {
        result = numA !== numB ? numA - numB : titleA.localeCompare(titleB, 'en', { sensitivity: 'base' })
      } else if (numA !== null) result = -1
      else if (numB !== null) result = 1
      else result = titleA.localeCompare(titleB, 'en', { sensitivity: 'base' })
      return sortOrder.value === 'asc' ? result : -result
    })
  } else if (sortBy.value === 'time') {
    arr.sort((a, b) => {
      const timeA = new Date(a.createdTime)
      const timeB = new Date(b.createdTime)
      const result = timeA - timeB
      return sortOrder.value === 'asc' ? result : -result
    })
  }

  // 分页
  const start = (pageNo.value - 1) * pageSize.value
  return arr.slice(start, start + pageSize.value)
})

const handleDelete = async (id) => {
  if (confirm('确定要删除该笔记吗？')) {
    await deleteTechNoteById(id)
    loadNotes()
  }
}

function changePage(newPage) {
  if (newPage >= 1 && newPage <= pageTotal.value) {
    pageNo.value = newPage
  }
}

function handlePageSizeChange() {
  pageNo.value = 1
  pageTotal.value = Math.ceil(totalCount.value / pageSize.value)
}

// 页码数组（带省略号）
const pageNumbers = computed(() => {
  const pages = []
  const total = pageTotal.value
  const current = pageNo.value
  const delta = 2

  for (let i = 1; i <= total; i++) {
    if (i === 1 || i === total || (i >= current - delta && i <= current + delta)) {
      pages.push(i)
    } else if (i === current - delta - 1 || i === current + delta + 1) {
      pages.push('...')
    }
  }

  return pages.filter((v, i, arr) => !(v === '...' && arr[i - 1] === '...'))
})

watch(() => route.params.section, (newSection) => {
  section.value = newSection
  pageNo.value = 1
  loadNotes()
})

loadNotes()
</script>


<template>
  <div class="p-6 w-full max-w-4xl mx-auto theme-bg-primary">
    <!-- 顶部工具栏 -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
      <h1 class="text-2xl font-semibold theme-title">{{ section }}笔记</h1>
      <div class="flex flex-wrap items-center gap-2">
        <!-- 排序字段 -->
        <select
          v-model="sortBy"
          class="theme-select text-sm py-1"
        >
          <option value="title">按标题排序</option>
          <option value="time">按时间排序</option>
        </select>

        <!-- 排序方向按钮 -->
        <button
          @click="sortOrder = sortOrder === 'asc' ? 'desc' : 'asc'"
          class="theme-button-secondary text-sm flex items-center py-1 px-2"
        >
          <ArrowUpIcon v-if="sortOrder === 'asc'" class="h-3 w-3 mr-1" />
          <ArrowDownIcon v-else class="h-3 w-3 mr-1" />
          <span class="hidden sm:inline">{{ sortOrder === 'asc' ? '升序' : '降序' }}</span>
        </button>

        <!-- 新建笔记按钮 -->
        <RouterLink 
          :to="`/techNotes/${section}/new`"
          class="inline-flex items-center theme-button text-sm py-1 px-3"
        >
          <PlusIcon class="h-3 w-3 mr-1" />
          <span class="hidden sm:inline">新建笔记</span>
          <span class="sm:hidden">新建</span>
        </RouterLink>
      </div>
    </div>

    <!-- 笔记列表 -->
    <div class="space-y-4">
      <div
        v-for="note in sortedNotes"
        :key="note.id"
        class="group theme-card hover:theme-shadow-md transition-all"
      >
        <RouterLink
          :to="`/techNotes/${section}/${note.id}`"
          class="block p-4"
        >
          <div class="flex flex-col md:flex-row">
            <div class="w-full md:w-1/4 md:pr-4 md:border-r theme-divider mb-2 md:mb-0">
              <h3 class="text-lg font-medium theme-text line-clamp-2">
                {{ note.title }}
              </h3>
            </div>
            <div class="w-full md:w-3/4 md:pl-4">
              <p class="text-sm theme-text-secondary line-clamp-3 whitespace-pre-line">
                {{ note.content.replace(/<[^>]*>/g, '') }}
              </p>
            </div>
          </div>
        </RouterLink>

        <!-- 操作按钮 -->
        <div class="flex justify-end space-x-3 px-4 py-2 theme-bg-secondary rounded-b-lg">
          <RouterLink
            :to="`/techNotes/${section}/${note.id}/edit`"
            class="theme-link text-sm flex items-center"
          >
            <PencilIcon class="h-4 w-4 mr-1" />
            编辑
          </RouterLink>
          <button
            @click.prevent.stop="handleDelete(note.id)"
            class="text-red-500 hover:text-red-700 text-sm flex items-center"
          >
            <TrashIcon class="h-4 w-4 mr-1" />
            删除
          </button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="mt-8 flex flex-col sm:flex-row justify-between items-center border-t theme-divider pt-6 gap-4">
      <div class="text-sm theme-text-secondary">共 {{ totalCount }} 条笔记</div>
      <div class="flex items-center space-x-2">
        <!-- 每页数量选择 -->
        <select
          v-model="pageSize"
          @change="handlePageSizeChange"
          class="theme-select text-sm py-1"
        >
          <option value="5">5条/页</option>
          <option value="10">10条/页</option>
          <option value="20">20条/页</option>
          <option value="50">50条/页</option>
        </select>

        <!-- 当前页码显示 -->
        <span class="text-sm theme-text-secondary px-2">
          第 {{ pageNo }} 页
        </span>

        <!-- 上一页 -->
        <button
          @click="changePage(pageNo - 1)"
          :disabled="pageNo <= 1"
          class="theme-button-secondary py-1 px-3 text-sm disabled:opacity-50"
          title="上一页"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>

        <!-- 下一页 -->
        <button
          @click="changePage(pageNo + 1)"
          :disabled="pageNo >= pageTotal"
          class="theme-button-secondary py-1 px-3 text-sm disabled:opacity-50"
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


<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;  
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;  
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.whitespace-pre-line {
  white-space: pre-line;
}
</style>
