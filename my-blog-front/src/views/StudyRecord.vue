<template>
  <div class="p-6 w-full max-w-screen-lg mx-auto">
    <!-- 排序按钮 -->
    <div class="flex justify-end mb-4">
      <button
        @click="toggleSortOrder"
        class="px-3 py-1.5 bg-blue-500 text-white rounded-md shadow hover:bg-blue-600 transition"
      >
        排序：{{ sortOrder === 'desc' ? '最近优先' : '最早优先' }}
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center h-40">
      <div class="w-10 h-10 border-4 border-blue-500 border-dashed rounded-full animate-spin"></div>
    </div>

    <!-- 时间线内容 -->
    <div v-else class="timeline-container relative">
      <!-- 垂直时间线（基于 --time-col-width 自动对齐） -->
      <div class="timeline-line"></div>

      <!-- 时间线项 -->
      <div
        v-for="(entry, index) in records"
        :key="index"
        class="flex items-start mb-10 group relative z-10"
      >
        <!-- 时间列：宽度由 CSS 变量控制，时间文本靠左/靠右可通过类控制 -->
        <div class="time-col flex flex-col items-end text-sm text-gray-500 pr-4">
          <div class="dot" aria-hidden="true"></div>
          <div class="mt-2 text-left w-full">{{ entry.date }}</div>
        </div>

        <!-- 内容卡片：半透明 + 毛玻璃效果 -->
        <div
          class="ml-4 p-4 w-full rounded-lg border border-gray-200/60 dark:border-gray-600/60 bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm shadow-sm hover:shadow-md hover:bg-white/90 dark:hover:bg-gray-700/90 transition-colors transition-shadow duration-200"
        >
          <div class="flex justify-between items-start">
            <div class="text-base font-semibold text-gray-800">
              {{ entry.title }}
            </div>
            <!-- 编辑/删除按钮 -->
            <div
              class="flex space-x-2 opacity-0 group-hover:opacity-100 transition-opacity"
            >
              <button
                @click.stop="editRecord(entry)"
                class="text-blue-500 hover:text-blue-700"
                title="编辑"
              >
                ✏️
              </button>
              <button
                @click.stop="confirmDelete(entry.id)"
                class="text-red-500 hover:text-red-700"
                title="删除"
              >
                🗑️
              </button>
            </div>
          </div>
          <!-- 描述，hover 展开；支持换行和长词换行 -->
          <div
            class="text-sm text-gray-600 mt-2 opacity-0 max-h-0 group-hover:opacity-100 group-hover:max-h-96 overflow-hidden transition-all duration-300 ease-in-out whitespace-pre-wrap break-words"
            aria-hidden="false"
          >
            {{ entry.description }}
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div v-if="!loading" class="flex justify-center mt-10 mb-12">
      <div class="flex items-center space-x-3">
        <!-- 上一页 -->
        <button
          @click="prevPage"
          :disabled="pagination.pageNo === 1"
          class="p-2 border rounded-md theme-text-secondary disabled:opacity-40 hover:theme-bg-hover"
          title="上一页"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>

        <!-- 页码 -->
        <div class="flex space-x-2">
          <span
            v-for="page in visiblePages"
            :key="page"
            @click="goToPage(page)"
            class="px-3 py-1 rounded-md text-sm font-medium cursor-pointer"
            :class="{
              'bg-blue-500 text-white': page === pagination.pageNo,
              'theme-text-primary hover:theme-bg-hover': page !== pagination.pageNo && page !== '...',
              'pointer-events-none theme-text-tertiary': page === '...'
            }"
          >
            {{ page }}
          </span>
        </div>

        <!-- 下一页 -->
        <button
          @click="nextPage"
          :disabled="pagination.pageNo >= pagination.totalPage"
          class="p-2 border rounded-md theme-text-secondary disabled:opacity-40 hover:theme-bg-hover"
          title="下一页"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </button>

        <!-- 每页条数 -->
        <select
          v-model="pagination.pageSize"
          @change="handlePageSizeChange"
          class="px-2 py-1 border rounded-md text-sm theme-input"
        >
          <option value="5">5条/页</option>
          <option value="10">10条/页</option>
          <option value="20">20条/页</option>
          <option value="50">50条/页</option>
        </select>
      </div>
    </div>

    <!-- 浮动添加按钮 -->
    <button
      @click="openAddForm"
      class="fixed bottom-8 right-8 bg-blue-500 text-white rounded-full w-14 h-14 text-3xl flex items-center justify-center shadow-lg hover:bg-blue-600 transition"
    >
      +
    </button>

    <!-- 遮罩层 + 表单 -->
    <div
      v-if="showForm"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white dark:bg-gray-800 p-6 rounded-lg w-full max-w-md shadow-lg">
        <h2 class="text-xl font-semibold mb-4 dark:text-gray-100">
          {{ isEditing ? '编辑学习记录' : '添加学习记录' }}
        </h2>

        <div class="space-y-3">
          <input
            v-model="form.title"
            type="text"
            placeholder="标题"
            class="w-full theme-input px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          />
          <textarea
            v-model="form.content"
            placeholder="内容"
            class="w-full theme-input px-3 py-2 h-24 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          ></textarea>
          <input
            v-model="form.studyDate"
            type="date"
            class="w-full theme-input px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          />
        </div>

        <div class="flex justify-end mt-4 space-x-2">
          <button @click="closeForm" class="px-4 py-2 text-gray-600 dark:text-gray-300">取消</button>
          <button
            @click="submitForm"
            class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-800"
          >
            {{ isEditing ? '更新' : '添加' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <div
      v-if="showDeleteConfirm"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white dark:bg-gray-800 p-6 rounded-lg w-full max-w-md shadow-lg">
        <h2 class="text-xl font-semibold mb-4">确认删除</h2>
        <p class="text-gray-600 mb-6">
          确定要删除这条学习记录吗？此操作不可撤销。
        </p>
        <div class="flex justify-end space-x-2">
          <button
            @click="showDeleteConfirm = false"
            class="px-4 py-2 text-gray-600"
          >
            取消
          </button>
          <button
            @click="deleteRecord"
            class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
          >
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  fetchPublicStudyRecords,
  fetchPrivateStudyRecords,
  addStudyRecord,
  updateStudyRecord,
  deleteStudyRecord
} from '@/api/studyRecord'

const records = ref([])
const showForm = ref(false)
const isEditing = ref(false)
const showDeleteConfirm = ref(false)
const sortOrder = ref('desc')
const sortField = ref('study_date')
const loading = ref(false)

const form = ref({
  id: null,
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

// 打开添加表单
function openAddForm() {
  isEditing.value = false
  showForm.value = true
  form.value = { id: null, title: '', content: '', studyDate: '' }
}
function closeForm() { showForm.value = false }

// 切换排序
function toggleSortOrder() {
  sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
  pagination.value.pageNo = 1
  loadRecords()
}

// 加载学习记录
async function loadRecords() {
  try {
    loading.value = true
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    const isLoggedIn = !!user.token

    const queryVO = {
      pageNo: pagination.value.pageNo,
      pageSize: pagination.value.pageSize,
      sortField: sortField.value,
      sortOrder: sortOrder.value
    }

    const res = isLoggedIn
      ? await fetchPrivateStudyRecords(queryVO)
      : await fetchPublicStudyRecords(queryVO)

    const data = res.data || {}
    const list = data.list || []

    records.value = list.map(item => ({
      id: item.id,
      date: item.studyDate || item.createTime,
      title: item.title,
      description: item.content
    }))

    pagination.value.total = data.totalCount || 0
    pagination.value.totalPage = data.pageTotal || 1
  } catch (err) {
    console.error('加载记录失败:', err)
  } finally {
    loading.value = false
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
const goToPage = page => {
  if (page !== '...' && page !== pagination.value.pageNo) {
    pagination.value.pageNo = page
    loadRecords()
  }
}
const handlePageSizeChange = () => {
  pagination.value.pageNo = 1
  loadRecords()
}

// 添加/编辑提交
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
    closeForm()
    pagination.value.pageNo = 1
    loadRecords()
  } catch (err) {
    console.error('保存失败:', err)
  }
}

// 编辑记录
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

// 删除记录
function confirmDelete(id) {
  recordToDelete.value = id
  showDeleteConfirm.value = true
}
async function deleteRecord() {
  try {
    await deleteStudyRecord({ id: recordToDelete.value })
    showDeleteConfirm.value = false
    pagination.value.pageNo = 1
    loadRecords()
  } catch (err) {
    console.error('删除失败:', err)
  }
}

onMounted(loadRecords)
</script>

<style scoped>
/* 时间列宽度由 CSS 变量控制，调整这个变量即可改变时间列与竖线的位置 */
.timeline-container {
  --time-col-width: 160px; /* default: 160px (可按需改小/改大) */
  position: relative;
}

/* 竖线：放在时间列右侧，z-index 低一点以免遮挡卡片阴影 */
.timeline-line {
  position: absolute;
  left: calc(var(--time-col-width));
  top: 0;
  bottom: 0;
  width: 2px;
  background: rgba(59,130,246,0.12); /* 淡蓝色线 */
  z-index: 0;
}

/* 时间列样式（与 --time-col-width 保持一致） */
.time-col {
  width: var(--time-col-width);
  min-width: var(--time-col-width);
  max-width: var(--time-col-width);
  z-index: 20; /* 高于竖线 */
  color: var(--text-secondary);
}

/* 圆点：在时间列的右侧（靠近平衡线） */
.dot {
  width: 12px;
  height: 12px;
  background: var(--primary-color);
  border-radius: 9999px;
  border: 4px solid var(--bg-primary);
  box-shadow: 0 2px 6px rgba(59,130,246,0.25);
  margin-right: 4px;
}

/* 卡片要在竖线上方显示（避免被线覆盖视觉） */
.timeline-container > .group > .ml-4 {
  z-index: 10;
}

/* 小屏时稍微缩小时间列 */
@media (max-width: 768px) {
  .timeline-container {
    --time-col-width: 120px;
  }
}
</style>
