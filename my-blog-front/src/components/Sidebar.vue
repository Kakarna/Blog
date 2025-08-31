<template>
  <aside class="sidebar-container">
    <div class="sidebar-content">
      <ul class="space-y-4">
        <!-- 首页 -->
        <li>
          <router-link
            to="/"
            class="text-lg font-semibold text-gray-800 hover:text-blue-600"
          >
            首页
          </router-link>
        </li>

        <!-- 技术笔记 -->
        <li>
          <div
            @click="toggle('技术笔记')"
            class="text-lg font-semibold text-gray-800 cursor-pointer"
          >
            技术笔记
          </div>

          <ul v-if="open === '技术笔记'" class="ml-4 mt-2 space-y-2">
            <li
              v-for="section in techNoteSections"
              :key="section.id"
              class="flex items-center gap-2 group relative"
            >
              <div class="flex items-center gap-2 w-full">
                <!-- 编辑按钮 -->
                <button
                  v-if="editingId !== section.id"
                  @click="startEdit(section.id, section.name)"
                  class="text-yellow-400 hover:text-yellow-600 text-sm px-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
                  title="编辑"
                >
                  ✎
                </button>

                <!-- 分区名或输入框 -->
                <template v-if="editingId === section.id">
                  <input
                    v-model="editName"
                    @keyup.enter="handleUpdate(section.id)"
                    @blur="handleUpdate(section.id)"
                    class="w-28 px-2 py-1 text-sm border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"
                    autofocus
                  />
                </template>
                <template v-else>
                  <router-link
                    :to="`/techNotes/${section.name}`"
                    class="text-gray-700 text-sm hover:text-blue-500 truncate flex-1"
                    title="点击查看笔记"
                  >
                    {{ section.name }}
                  </router-link>
                </template>

                <!-- 删除按钮 -->
                <button
                  @click="confirmDelete(section.id, section.name)"
                  class="text-red-400 hover:text-red-600 text-sm px-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
                  title="删除"
                >
                  ×
                </button>
              </div>
            </li>

            <!-- 添加按钮 -->
            <li v-if="!showInput" class="mt-2">
              <button
                @click="showInput = true"
                class="text-sm text-blue-600 hover:underline"
              >
                ➕ 添加分区
              </button>
            </li>

            <!-- 输入框 -->
            <li v-else class="mt-2">
              <input
                v-model="newSection"
                @keyup.enter="handleAddSection"
                @blur="cancelAdd"
                placeholder="输入新分区名称"
                class="w-28 px-2 py-1 text-sm border rounded focus:outline-none focus:ring-1 focus:ring-blue-400"
                autofocus
              />
            </li>
          </ul>
        </li>

        <!-- 日记 -->
        <li>
          <router-link
            to="/LearningTimeline"
            class="text-lg font-semibold text-gray-800 hover:text-blue-600"
          >
            日记
          </router-link>
        </li>

        <!-- 项目 -->
        <li>
          <router-link
            to="/MyProjects"
            class="text-lg font-semibold text-gray-800 hover:text-blue-600"
          >
            我的项目
          </router-link>
        </li>

        <!-- 关于我 -->
        <li>
          <router-link
            to="/about"
            class="text-lg font-semibold text-gray-800 hover:text-blue-600"
          >
            关于我
          </router-link>
        </li>
      </ul>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getSectionList,
  addSection,
  deleteSection,
  updateSection,
} from '@/api/techSection'

const open = ref('技术笔记')
const techNoteSections = ref([])
const showInput = ref(false)
const newSection = ref('')

const editingId = ref(null)
const editName = ref('')

// 封装统一 catch 检查
function handleError(err, fallbackMsg = '请求失败，请检查控制台') {
  if (!err.handledByInterceptor) {
    console.error(err)
    alert(fallbackMsg)
  }
}

// 获取分区
const fetchSections = async () => {
  try {
    const res = await getSectionList()
    techNoteSections.value = res?.data?.list || []
  } catch (err) {
    handleError(err, '获取分区列表失败')
  }
}

// 添加分区
const handleAddSection = async () => {
  const name = newSection.value.trim()
  if (!name || techNoteSections.value.some((item) => item.name === name)) {
    resetInput()
    return
  }
  try {
    await addSection({ name })
    await fetchSections()
  } catch (err) {
    handleError(err, '添加分区失败')
  } finally {
    resetInput()
  }
}

// 删除分区
const confirmDelete = async (id, name) => {
  if (window.confirm(`确定删除分区 "${name}" 吗？`)) {
    try {
      await deleteSection(id)
      await fetchSections()
    } catch (err) {
      handleError(err, '删除分区失败')
    }
  }
}

// 编辑分区
const startEdit = (id, name) => {
  editingId.value = id
  editName.value = name
}

// 提交修改
const handleUpdate = async (id) => {
  const name = editName.value.trim()
  if (!name) {
    // 空值触发删除
    if (confirm('分区名称为空，确定要删除该分区吗？')) {
      try {
        await deleteSection(id)
        await fetchSections()
      } catch (err) {
        handleError(err, '删除分区失败')
      } finally {
        cancelEdit()
      }
    } else {
      cancelEdit()
    }
    return
  }

  if (techNoteSections.value.some((item) => item.name === name && item.id !== id)) {
    cancelEdit()
    return
  }

  try {
    await updateSection(id, { name })
    await fetchSections()
  } catch (err) {
    handleError(err, '修改分区失败')
  } finally {
    cancelEdit()
  }
}

// 取消编辑
const cancelEdit = () => {
  editingId.value = null
  editName.value = ''
}

// 展开/收起
const toggle = (sectionName) => {
  open.value = open.value === sectionName ? '' : sectionName
}

// 新增输入框取消
const cancelAdd = () => {
  newSection.value = ''
  showInput.value = false
}

// 重置输入框
const resetInput = () => {
  newSection.value = ''
  showInput.value = false
}

// 初始化
onMounted(() => {
  fetchSections()
})
</script>

<style scoped>
.sidebar-container {
  width: 20%;
  min-width: 200px;
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  position: relative;
  height: calc(100vh - 128px);
}

.sidebar-content {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow-y: auto;
  padding: 1rem;
}

.text-lg {
  font-size: 1.125rem;
  line-height: 1.75rem;
}

.font-semibold {
  font-weight: 600;
}

/* 按钮默认隐藏，鼠标悬停显示 */
li.group button {
  opacity: 0;
  transition: opacity 0.2s;
}
li.group:hover button {
  opacity: 1;
}
</style>
