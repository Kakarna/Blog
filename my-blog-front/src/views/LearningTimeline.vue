<template>
  <div class="p-6 w-full max-w-screen-lg mx-auto">
    <div class="relative">
      <div class="flex justify-end mb-4">
        <button @click="toggleSortOrder" class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600 transition">
          排序：{{ sortOrder === 'desc' ? '最近优先' : '最早优先' }}
        </button>
      </div>

      <!-- 垂直时间线 -->
      <div class="absolute left-4 top-0 bottom-0 w-0.5 bg-blue-500"></div>

      <div v-for="(entry, index) in records" :key="index" class="flex items-start mb-8 group">
        <!-- 时间点 & 时间文本 -->
        <div class="flex flex-col items-center w-32 text-sm text-gray-500">
          <div class="w-3 h-3 bg-blue-500 rounded-full z-10"></div>
          <div class="mt-2 text-left">{{ entry.date }}</div>
        </div>

        <!-- 内容卡片 -->
        <div class="ml-4 p-3 transition duration-200 rounded-md hover:bg-gray-50 w-full border-l border-gray-200">
          <div class="flex justify-between items-start">
            <div class="text-base font-semibold text-gray-800">{{ entry.title }}</div>
            <div class="flex space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
              <button @click.stop="editRecord(entry)" class="text-blue-500 hover:text-blue-700">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                </svg>
              </button>
              <button @click.stop="confirmDelete(entry.id)" class="text-red-500 hover:text-red-700">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd"
                    d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                    clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </div>
          <div
            class="text-sm text-gray-600 mt-1 opacity-0 max-h-0 group-hover:opacity-100 group-hover:max-h-40 overflow-hidden transition-all duration-300 ease-in-out">
            {{ entry.description }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 浮动添加按钮 -->
  <button @click="showForm = true; isEditing = false; form = { title: '', content: '', studyDate: '' }"
    class="fixed bottom-8 right-8 bg-blue-500 text-white rounded-full w-14 h-13 text-3xl flex items-center justify-center shadow-lg hover:bg-blue-600 transition">
    +
  </button>

  <!-- 遮罩层 + 表单 -->
  <div v-if="showForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg">
      <h2 class="text-xl font-semibold mb-4">{{ isEditing ? '编辑学习记录' : '添加学习记录' }}</h2>

      <div class="space-y-3">
        <input v-model="form.title" type="text" placeholder="标题" class="w-full border rounded px-3 py-2" />
        <textarea v-model="form.content" placeholder="内容" class="w-full border rounded px-3 py-2 h-24"></textarea>
        <input v-model="form.studyDate" type="date" class="w-full border rounded px-3 py-2" />
      </div>

      <div class="flex justify-end mt-4 space-x-2">
        <button @click="showForm = false" class="px-4 py-2 text-gray-600">取消</button>
        <button @click="submitForm" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
          {{ isEditing ? '更新' : '添加' }}
        </button>
      </div>
    </div>
  </div>

  <!-- 删除确认对话框 -->
  <div v-if="showDeleteConfirm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg">
      <h2 class="text-xl font-semibold mb-4">确认删除</h2>
      <p class="text-gray-600 mb-6">确定要删除这条学习记录吗？此操作不可撤销。</p>
      <div class="flex justify-end space-x-2">
        <button @click="showDeleteConfirm = false" class="px-4 py-2 text-gray-600">取消</button>
        <button @click="deleteRecord" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">删除</button>
      </div>
    </div>
  </div>

  <div class="flex justify-center mt-8 mb-12">
    <div class="flex items-center space-x-4">
      <button @click="prevPage" :disabled="pagination.pageNo === 1"
        class="px-4 py-2 border rounded-md text-sm font-medium disabled:opacity-50 hover:bg-gray-50 transition-colors">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd"
            d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
            clip-rule="evenodd" />
        </svg>
      </button>

      <div class="flex space-x-2">
        <span v-for="page in visiblePages" :key="page" @click="goToPage(page)"
          class="px-3 py-1 rounded-md text-sm font-medium cursor-pointer" :class="{
            'bg-blue-500 text-white': page === pagination.pageNo,
            'text-gray-700 hover:bg-gray-100': page !== pagination.pageNo && page !== '...',
            'pointer-events-none': page === '...'
          }">
          {{ page }}
        </span>
      </div>

      <button @click="nextPage" :disabled="pagination.pageNo >= pagination.totalPage"
        class="px-4 py-2 border rounded-md text-sm font-medium disabled:opacity-50 hover:bg-gray-50 transition-colors">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd"
            d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
            clip-rule="evenodd" />
        </svg>
      </button>

      <select v-model="pagination.pageSize" @change="handlePageSizeChange"
        class="px-3 py-1 border rounded-md text-sm focus:outline-none focus:ring-1 focus:ring-blue-500">
        <option value="5">5条/页</option>
        <option value="10">10条/页</option>
        <option value="20">20条/页</option>
        <option value="50">50条/页</option>
      </select>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { fetchStudyRecords, addStudyRecord, updateStudyRecord, deleteStudyRecord } from '@/api/studyRecord'

const records = ref([]) // 确保这行代码存在
const showForm = ref(false)
const isEditing = ref(false)
const showDeleteConfirm = ref(false)
const sortOrder = ref('desc') // 新增排序状态，desc: 最近优先，asc: 最早优先
const sortField = ref('study_date') // 或你实际的排序字段名，如 'createTime' 或 'studyDate'


// const sortedRecords = computed(() => {
//   return [...records.value].sort((a, b) => {
//     const timeA = new Date(a.date).getTime()
//     const timeB = new Date(b.date).getTime()
//     return sortOrder.value === 'desc' ? timeB - timeA : timeA - timeB
//   })
// })

function toggleSortOrder() {
  sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
  pagination.value.pageNo = 1  // 切换排序时重置页码
  loadRecords()
}

const form = ref({
  title: '',
  content: '',
  studyDate: ''
})

const recordToDelete = ref(null)

// 分页数据
const pagination = ref({
  pageNo: 1,
  pageSize: 10,
  total: 0,
  totalPage: 1
})

// 计算显示的页码范围
const visiblePages = computed(() => {
  const current = pagination.value.pageNo
  const total = pagination.value.totalPage
  const range = 2 // 显示当前页前后各2页
  let pages = []

  // 总页数小于等于5时显示所有页码
  if (total <= 5) {
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
    return pages
  }

  // 添加前几页
  if (current > range + 1) {
    pages.push(1)
    if (current > range + 2) {
      pages.push('...')
    }
  }

  // 添加当前页附近的页码
  const start = Math.max(1, current - range)
  const end = Math.min(total, current + range)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }

  // 添加后几页
  if (current < total - range) {
    if (current < total - range - 1) {
      pages.push('...')
    }
    pages.push(total)
  }

  return pages
})

// 加载学习记录
const loadRecords = async () => {
  console.log('加载数据，页码:', pagination.value.pageNo)
  try {
    const res = await fetchStudyRecords({
      pageNo: pagination.value.pageNo,
      pageSize: pagination.value.pageSize,
      sortField: sortField.value,
      sortOrder: sortOrder.value
    })

    const data = res.data || {}   // 注意多层data
    const list = data.list || []
    const totalCount = data.totalCount || 0
    const totalPage = data.pageTotal || 1

    records.value = list.map(item => ({
      id: item.id,
      date: item.studyDate || item.createTime,
      title: item.title,
      description: item.content
    }))

    pagination.value.total = totalCount
    pagination.value.totalPage = totalPage

  } catch (err) {
    console.error('加载记录失败:', err)
  }
}

// 分页操作
const prevPage = () => {
  if (pagination.value.pageNo > 1) {
    pagination.value.pageNo--
    loadRecords()
  }
}

const nextPage = () => {
  if (pagination.value.pageNo < pagination.value.totalPage) {
    pagination.value.pageNo++
    loadRecords()
  }
}

const goToPage = (page) => {
  if (page !== '...' && page !== pagination.value.pageNo) {
    pagination.value.pageNo = page
    loadRecords()
  }
}

const handlePageSizeChange = () => {
  pagination.value.pageNo = 1
  loadRecords()
}

// 在添加/编辑/删除成功后重置到第一页
const afterMutateSuccess = () => {
  pagination.value.pageNo = 1
  loadRecords()
}

// 修改submitForm和deleteRecord方法，成功时调用afterMutateSuccess
async function submitForm() {
  if (!form.value.title || !form.value.content || !form.value.studyDate) {
    alert('标题、内容和日期不能为空')
    return
  }
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    form.value.username = user.username

    if (isEditing.value) {
      await updateStudyRecord(form.value)
    } else {
      await addStudyRecord(form.value)
    }

    showForm.value = false
    afterMutateSuccess()
  } catch (err) {
    console.error('操作失败:', err)
  }
}

function editRecord(entry) {
  isEditing.value = true
  showForm.value = true
  form.value = {
    id: entry.id,
    title: entry.title,
    content: entry.description,
    studyDate: entry.date
  }
}

function confirmDelete(id) {
  recordToDelete.value = id
  showDeleteConfirm.value = true
}


async function deleteRecord() {
  try {
    await deleteStudyRecord({ id: recordToDelete.value })
    showDeleteConfirm.value = false
    afterMutateSuccess()
  } catch (err) {
    console.error('删除失败:', err)
  }
}



onMounted(loadRecords)
</script>