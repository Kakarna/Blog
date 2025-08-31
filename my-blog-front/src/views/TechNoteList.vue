<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute, RouterLink } from 'vue-router'
import { getTechNoteList, deleteTechNoteById } from '@/api/techNote'
import {
  PlusIcon,
  PencilIcon,
  TrashIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
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

// 排序字段：title / createdTime / updatedTime
const sortBy = ref('title')
// 排序方向：asc / desc
const sortOrder = ref('asc')

const loadNotes = async () => {
  const res = await getTechNoteList({
    sectionName: section.value,
    pageNo: 1,       // 固定 1
    pageSize: 9999   // 足够大，拿全量
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

      const isNumA = numA !== null
      const isNumB = numB !== null

      let result = 0
      if (isNumA && isNumB) {
        if (numA !== numB) result = numA - numB
        else result = titleA.localeCompare(titleB, 'en', { sensitivity: 'base' })
      } else if (isNumA) {
        result = -1
      } else if (isNumB) {
        result = 1
      } else {
        result = titleA.localeCompare(titleB, 'en', { sensitivity: 'base' })
      }
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

watch(() => route.params.section, (newSection) => {
  section.value = newSection
  pageNo.value = 1
  loadNotes()
})

loadNotes()
</script>


<template>
  <div class="p-6 w-full max-w-4xl mx-auto">
    <div class="flex justify-between items-center mb-8">
      <h1 class="text-2xl font-semibold text-gray-800">{{ section }}笔记</h1>
      <div class="flex items-center space-x-3">
        <!-- 排序字段 -->
        <select
          v-model="sortBy"
          class="px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
        >
          <option value="title">按标题排序</option>
          <option value="time">按时间排序</option>
        </select>

        <!-- 排序方向按钮 -->
        <button
          @click="sortOrder = sortOrder === 'asc' ? 'desc' : 'asc'"
          class="px-3 py-2 border rounded-lg text-sm flex items-center hover:bg-gray-50"
        >
          <ArrowUpIcon v-if="sortOrder === 'asc'" class="h-4 w-4 mr-1" />
          <ArrowDownIcon v-else class="h-4 w-4 mr-1" />
          {{ sortOrder === 'asc' ? '升序' : '降序' }}
        </button>

        <!-- 新建笔记按钮 -->
        <RouterLink 
          :to="`/techNotes/${section}/new`"
          class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm"
        >
          <PlusIcon class="h-4 w-4 mr-1" />
          新建笔记
        </RouterLink>
      </div>
    </div>

    <div class="space-y-4">
      <div
        v-for="note in sortedNotes"
        :key="note.id"
        class="group border rounded-lg hover:shadow-md transition-all"
      >
        <RouterLink
          :to="`/techNotes/${section}/${note.id}`"
          class="block p-4"
        >
          <div class="flex">
            <div class="w-1/4 pr-4 border-r border-gray-100">
              <h3 class="text-lg font-medium text-gray-900 line-clamp-2">
                {{ note.title }}
              </h3>
            </div>
            
            <div class="w-3/4 pl-4">
              <p class="text-sm text-gray-600 line-clamp-3 whitespace-pre-line">
                {{ note.content.replace(/<[^>]*>/g, '') }}
              </p>
            </div>
          </div>
        </RouterLink>

        <div class="flex justify-end space-x-3 px-4 py-2 bg-gray-50 rounded-b-lg">
          <RouterLink
            :to="`/techNotes/${section}/${note.id}/edit`"
            class="text-blue-500 hover:text-blue-700 text-sm flex items-center"
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

    <div class="mt-8 flex flex-col sm:flex-row justify-between items-center border-t pt-6">
      <div class="text-sm text-gray-500 mb-4 sm:mb-0">
        共 {{ totalCount }} 条笔记
      </div>
      <div class="flex items-center space-x-4">
        <select
          v-model="pageSize"
          @change="handlePageSizeChange"
          class="px-3 py-1 border rounded-md text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
        >
          <option value="5">5条/页</option>
          <option value="10">10条/页</option>
          <option value="20">20条/页</option>
          <option value="50">50条/页</option>
        </select>
        
        <div class="flex space-x-2">
          <button
            @click="changePage(pageNo - 1)"
            :disabled="pageNo <= 1"
            class="px-3 py-1 border rounded-md text-sm disabled:opacity-50 hover:bg-gray-50 flex items-center"
          >
            <ChevronLeftIcon class="h-4 w-4" />
          </button>
          <span class="px-3 py-1 text-sm text-gray-700">
            {{ pageNo }} / {{ pageTotal }}
          </span>
          <button
            @click="changePage(pageNo + 1)"
            :disabled="pageNo >= pageTotal"
            class="px-3 py-1 border rounded-md text-sm disabled:opacity-50 hover:bg-gray-50 flex items-center"
          >
            <ChevronRightIcon class="h-4 w-4" />
          </button>
        </div>
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
