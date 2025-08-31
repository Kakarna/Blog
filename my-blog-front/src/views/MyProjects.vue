<template>
  <div class="max-w-5xl mx-auto p-6">
    <div class="flex justify-between items-center mb-8">
      <h1 class="text-3xl font-bold text-gray-900">我的项目</h1>
      <a-button type="primary" @click="showModal = true">上传项目</a-button>
    </div>

    <div v-if="projects.length" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div v-for="project in projects" :key="project.id"
        class="bg-white rounded-lg shadow-sm hover:shadow-md transition p-5 border border-gray-200 relative">
        
        <!-- 删除按钮 -->
        <a-popconfirm
          title="确定要删除该项目吗？"
          ok-text="是"
          cancel-text="否"
          @confirm="handleDelete(project.id)"
        >
          <a-button
            danger
            type="text"
            class="absolute top-2 right-2"
          >
            删除
          </a-button>
        </a-popconfirm>

        <router-link :to="`/MyProjects/${project.id}`" class="text-blue-600 font-semibold text-xl hover:underline">
          {{ project.name }}
        </router-link>

        <p class="text-gray-600 mt-2 mb-4 text-sm leading-relaxed">{{ project.description }}</p>
        <div class="flex items-center justify-between text-gray-500 text-xs">
          <span class="capitalize bg-gray-100 px-2 py-1 rounded">{{ project.type }}</span>
          <span>更新时间：{{ formatDate(project.updateTime) }}</span>
        </div>
      </div>
    </div>

    <div v-else class="text-gray-500 text-center py-20">还没有任何项目，快来上传吧～</div>

    <!-- 简单分页 -->
    <div class="flex justify-center space-x-3 mt-6">
      <button :disabled="pageNo <= 1" @click="changePage(pageNo - 1)"
        class="px-3 py-1 border rounded disabled:opacity-50">
        上一页
      </button>

      <button v-for="page in totalPages" :key="page" @click="changePage(page)" :class="[ 
        'px-3 py-1 border rounded',
        page === pageNo ? 'bg-blue-600 text-white' : 'hover:bg-gray-100'
      ]">
        {{ page }}
      </button>

      <button :disabled="pageNo >= totalPages" @click="changePage(pageNo + 1)"
        class="px-3 py-1 border rounded disabled:opacity-50">
        下一页
      </button>
    </div>

    <ProjectUploadModal v-model:open="showModal" @submit="handleSubmit" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ProjectUploadModal from '../components/ProjectUploadModal.vue'
import { getProjects, deleteProject } from '@/api/project'
import { deleteProjectFiles } from '@/api/projectFile'
import { message } from 'ant-design-vue'

const showModal = ref(false)
const projects = ref([])

const pageNo = ref(1)
const pageSize = 15
const totalCount = ref(0)
const totalPages = ref(1)

async function fetchProjects(page = 1) {
  const res = await getProjects({ pageNo: page, pageSize })
  projects.value = res.data?.list || []
  totalCount.value = res.data?.totalCount || 0
  totalPages.value = Math.ceil(totalCount.value / pageSize)
  pageNo.value = page
}

function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    fetchProjects(page)
  }
}

async function handleSubmit(project) {
  await fetchProjects(pageNo.value)
  showModal.value = false
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString()
}

async function handleDelete(id) {
  try {
    // 先删除文件，再删除项目
    await deleteProjectFiles(id)
    await deleteProject(id)
    message.success('删除成功')
    fetchProjects(pageNo.value) // 刷新列表
  } catch (err) {
    console.error(err)
    message.error('删除失败，请检查权限或后端日志')
  }
}

onMounted(() => {
  fetchProjects()
})
</script>
