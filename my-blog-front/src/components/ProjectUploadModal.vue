<template>
  <a-modal 
    :key="isDark ? 'dark' : 'light'" 
    :open="open" 
    title="上传项目" 
    :width="1000" 
    :confirmLoading="confirmLoading" 
    @ok="handleOk"
    @cancel="handleCancel" 
    destroyOnClose 
    :okButtonProps="{ disabled: confirmLoading }"
    :modalClassName="isDark ? 'dark-modal' : 'light-modal'"
    :wrapClassName="isDark ? 'dark-modal-wrap' : 'light-modal-wrap'">
    
    <div class="flex items-center justify-between mb-4">
      <span class="text-sm" :class="isDark ? 'text-gray-300' : 'text-gray-500'">
        {{ isDark ? '暗色主题' : '明色主题' }}
      </span>
      <!-- 添加主题切换按钮，方便测试 -->
      <a-button size="small" @click="toggleTheme" class="ml-auto">
        切换主题
      </a-button>
    </div>
    <div class="grid grid-cols-4 gap-6">
      <!-- 左侧表单 -->
      <div class="col-span-3 space-y-4">
        <a-form layout="vertical">
          <a-form-item label="项目名称" required>
            <a-input v-model:value="name" placeholder="例如：my-blog" />
          </a-form-item>
          <a-form-item label="项目简介">
            <a-input v-model:value="description" placeholder="一句话描述项目" />
          </a-form-item>
          <a-form-item label="项目类型" required>
            <a-select v-model:value="type" placeholder="请选择项目类型">
              <a-select-option v-for="t in typeOptions" :key="t" :value="t">{{ t }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="README">
            <a-textarea v-model:value="readme" :rows="5" placeholder="README 内容（可选）" />
          </a-form-item>
          <a-form-item label="选项">
            <div class="flex items-center gap-4">
              <a-switch v-model:checked="includeContents" />
              <span class="text-sm text-gray-600">包含文件内容（大项目建议关闭）</span>
            </div>
            <div class="mt-2 flex items-center gap-2">
              <span class="text-sm text-gray-600">单文件最大读取大小：</span>
              <a-input-number v-model:value="maxFileSizeKB" :min="1" :max="51200" /> <span>KB</span>
            </div>
          </a-form-item>
          <a-form-item label="选择项目文件夹" required>
            <input ref="fileInput" type="file" webkitdirectory multiple @change="handleFolderSelect" class="w-full" />
            <small class="text-xs text-gray-500">注：若项目很大，建议关闭"包含文件内容"。</small>
          </a-form-item>
        </a-form>
      </div>

      <!-- 右侧预览 -->
      <div class="col-span-1">
        <div 
          class="p-4 border rounded-lg space-y-3"
          :class="isDark ? 'bg-gray-900 border-gray-700 text-gray-200' : 'bg-gray-50 border-gray-300 text-gray-800'">
          <div class="font-semibold">识别结果</div>
          <div class="text-sm">
            <div>文件数：<span class="font-medium">{{ totalFiles }}</span></div>
            <div>文件夹数：<span class="font-medium">{{ totalDirs }}</span></div>
            <div>总大小：<span class="font-medium">{{ formatBytes(totalBytes) }}</span></div>
            <div>已读取内容：<span class="font-medium">{{ filesWithContent }}</span></div>
          </div>
          <div v-if="reading" class="mt-2 text-sm text-blue-600">正在分析文件，请稍候...</div>
          <div v-if="errorMsg" class="mt-2 text-sm text-red-600">{{ errorMsg }}</div>
          
          <!-- 上传进度显示 -->
          <div v-if="confirmLoading" class="mt-3">
            <div class="text-sm" :class="isDark ? 'text-gray-100' : 'text-gray-600'">上传进度: {{ uploadProgress }}%</div>
            <a-progress :percent="uploadProgress" status="active" />
            <div class="text-xs mt-1" :class="isDark ? 'text-gray-300' : 'text-gray-500'">
              正在上传分片 {{ currentChunk }}/{{ totalChunks }}
            </div>
          </div>

          <div v-if="fileTree.length" class="mt-3 max-h-[40vh] overflow-auto border rounded p-2" 
            :class="isDark ? 'bg-gray-900 border-gray-700' : 'bg-white border-gray-300'">
            <FileTree :files="fileTree" :selectedFile="selectedFilePath" @selectFile="handleFileSelect" />
          </div>
          <div v-else class="mt-3 text-sm text-gray-500">尚未识别到文件结构</div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { message } from 'ant-design-vue'
import FileTree from './FileTree.vue'
import { addProjectWithFiles } from '@/api/project'
import JSZip from 'jszip'

// 主题状态管理
const isDark = ref(false)

// 监听主题变化 - 确保正确更新状态并同步到localStorage
const handleThemeChange = (event) => {
  if (event?.detail?.isDark !== undefined) {
    isDark.value = event.detail.isDark
    // 不在这里设置localStorage，因为主题切换应该由全局控制
    // 这里只负责响应变化
    
    // 强制更新body上的主题数据属性，确保Modal样式正确应用
    document.body.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
  }
}

// 添加主题切换函数，用于测试
const toggleTheme = () => {
  isDark.value = !isDark.value
  // 触发全局事件，通知其他组件
  window.dispatchEvent(new CustomEvent('themeChanged', { 
    detail: { isDark: isDark.value } 
  }))
  // 保存到localStorage
  localStorage.setItem('darkMode', isDark.value.toString())
  // 更新body上的主题数据属性
  document.body.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
}

// 监听系统主题变化
const handleSystemThemeChange = (e) => {
  const prefersDark = e.matches
  // 只有当用户没有明确设置主题时，才跟随系统
  if (localStorage.getItem('darkMode') === null) {
    isDark.value = prefersDark
  }
}



// 不再需要切换主题功能，只保留状态监听

const props = defineProps({ 
  open: Boolean,
  // 可选地接收外部传入的主题状态
  darkMode: {
    type: Boolean,
    default: undefined
  }
})

// 监听外部传入的darkMode变化
watch(() => props.darkMode, (newVal) => {
  if (newVal !== undefined) {
    isDark.value = newVal
  }
}, { immediate: true })
const emit = defineEmits(['update:open', 'submit'])

const name = ref('')
const description = ref('')
const type = ref('')
const readme = ref('')
const typeOptions = ['个人项目', '课程项目', '开源项目', '实习项目', '实验项目', '工具/库']
const includeContents = ref(true)
const maxFileSizeKB = ref(1024)

const fileTree = ref([])
const flatFiles = ref([])
const totalFiles = ref(0)
const totalDirs = ref(0)
const totalBytes = ref(0)
const filesWithContent = ref(0)
const reading = ref(false)
const confirmLoading = ref(false)
const errorMsg = ref(null)
const selectedFilePath = ref('')
const fileInput = ref(null)
const uploadProgress = ref(0)
const currentChunk = ref(0)
const totalChunks = ref(0)

const handleFileSelect = path => selectedFilePath.value = path

function isProbablyTextFile(file) {
  if (file.type && file.type.startsWith('text')) return true
  return /\.(md|markdown|json|js|ts|java|py|vue|jsx?|tsx?|xml|html|css|scss|yml|yaml|properties|txt|mdx)$/i.test(file.name)
}

function readFileAsText(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = e => resolve(String(e.target.result))
    reader.onerror = e => reject(e)
    reader.readAsText(file, 'utf-8')
  })
}

function buildFileTreeFromFileList(files) {
  const root = []
  const pathMap = {}
  files.forEach(file => {
    const rel = file.webkitRelativePath || file.name
    const parts = rel.split('/')
    let currentLevel = root
    parts.forEach((part, index) => {
      const isDir = index < parts.length - 1
      const path = parts.slice(0, index + 1).join('/')
      if (!pathMap[path]) {
        const newNode = reactive({ name: part, path, isDir, children: isDir ? [] : undefined, open: false })
        if (isDir) {
          pathMap[path] = newNode
          currentLevel.push(newNode)
          currentLevel = newNode.children
        } else currentLevel.push(newNode)
      } else currentLevel = pathMap[path].children
    })
  })
  return root
}



async function prepareFlatFiles(files, includeContentsFlag, maxKB) {
  const mapByPath = new Map()
  files.forEach(f => mapByPath.set(f.webkitRelativePath || f.name, f))
  const dirSet = new Set()
  for (const rel of mapByPath.keys()) {
    const parts = rel.split('/')
    for (let i = 0; i < parts.length - 1; i++) dirSet.add(parts.slice(0, i + 1).join('/'))
  }
  const result = []
  Array.from(dirSet).sort().forEach(p => result.push({ path: p, isDir: true, content: null, open: false }))
  let countWithContent = 0
  for (const [rel, fileObj] of mapByPath.entries()) {
    const item = { path: rel, isDir: false, content: null }
    if (includeContentsFlag && isProbablyTextFile(fileObj) && fileObj.size <= maxKB * 1024) {
      try { 
        const text = await readFileAsText(fileObj); 
        item.content = text; 
        countWithContent++; 
        if (/^readme\.md$/i.test(fileObj.name) && !readme.value) {
          readme.value = text
        }
      } catch { 
        item.content = null 
      }
    }
    result.push(item)
  }
  return { flat: result, filesWithContent: countWithContent }
}

async function handleFolderSelect(evt) {
  errorMsg.value = null
  const rawFiles = Array.from(evt.target.files || [])
  if (!rawFiles.length) return message.warn('未选择文件')
  reading.value = true
  try {
    let totalB = 0; 
    rawFiles.forEach(f => totalB += f.size || 0)
    totalBytes.value = totalB
    totalFiles.value = rawFiles.length
    const root = buildFileTreeFromFileList(rawFiles)

    fileTree.value = root
    const { flat, filesWithContent: cnt } = await prepareFlatFiles(rawFiles, includeContents.value, maxFileSizeKB.value)
    flatFiles.value = flat
    filesWithContent.value = cnt
    totalDirs.value = flat.filter(i => i.isDir).length
  } catch (err) {
    console.error(err)
    errorMsg.value = '读取文件时出错，请重试'
  } finally { 
    reading.value = false 
  }
}

function formatBytes(bytes) {
  if (!bytes) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']; 
  let i = 0; 
  let num = bytes
  while (num >= 1024 && i < units.length - 1) { 
    num /= 1024; 
    i++ 
  }
  return i > 0 ? `${num.toFixed(2)} ${units[i]}` : `${num} ${units[i]}`
}

async function handleOk() {
  if (!name.value || !type.value) {
    message.error('请填写项目名称和类型')
    return
  }
  if (!flatFiles.value || !flatFiles.value.length) {
    message.error('请先选择项目文件夹并等待分析完成')
    return
  }

  confirmLoading.value = true
  uploadProgress.value = 0
  currentChunk.value = 0
  
  try {
    const projectMeta = {
      name: name.value,
      description: description.value,
      type: type.value,
      readme: readme.value,
    }

    const zip = new JSZip()
    const ignoreFolders = ['node_modules', 'dist', '.git']

    for (let file of fileInput.value.files) {
      const path = file.webkitRelativePath || file.name
      if (ignoreFolders.some(p => path.includes(p))) continue
      if (includeContents.value && isProbablyTextFile(file) && file.size <= maxFileSizeKB.value * 1024) {
        const content = await readFileAsText(file)
        zip.file(path, content)
      } else {
        zip.file(path, file)
      }
    }

    const zipBlob = await zip.generateAsync({ type: 'blob' })
    const chunkSize = 5 * 1024 * 1024 // 5MB
    totalChunks.value = Math.ceil(zipBlob.size / chunkSize)

    // 生成唯一的 uploadId
    const uploadId = `${name.value}_${Date.now()}`

    // 分片上传函数
    const uploadChunk = async (chunkBlob, index) => {
      const formData = new FormData()
      formData.append("chunk", chunkBlob)
      formData.append("chunkIndex", index)
      formData.append("totalChunks", totalChunks.value)
      formData.append("fileName", `${name.value}.zip`)
      formData.append("uploadId", uploadId)
      formData.append("maxFileSizeKB", maxFileSizeKB.value)

      try {
        const res = await addProjectWithFiles(formData)
        if (res.code !== 200) {
          throw new Error(res.info || `分片 ${index} 上传失败`)
        }
        console.log(`分片 ${index + 1}/${totalChunks.value} 上传成功`)
        return res
      } catch (error) {
        console.error(`分片 ${index} 上传失败:`, error)
        throw error
      }
    }

    // 重试机制函数
    const uploadWithRetry = async (chunkBlob, index, maxRetries = 3) => {
      let retries = 0
      while (retries < maxRetries) {
        try {
          return await uploadChunk(chunkBlob, index)
        } catch (error) {
          retries++
          if (retries >= maxRetries) {
            throw new Error(`分片 ${index} 上传失败，已重试 ${maxRetries} 次`)
          }
          console.warn(`分片 ${index} 上传失败，第 ${retries} 次重试...`)
          await new Promise(resolve => setTimeout(resolve, 1000 * retries)) // 指数退避
        }
      }
    }

    // 上传所有分片（使用重试机制）
    for (let i = 0; i < totalChunks.value; i++) {
      const start = i * chunkSize
      const end = Math.min(zipBlob.size, (i + 1) * chunkSize)
      const chunk = zipBlob.slice(start, end)
      await uploadWithRetry(chunk, i)
      
      // 更新进度
      currentChunk.value = i + 1
      uploadProgress.value = Math.round(((i + 1) / totalChunks.value) * 100)
    }

    // 发送合并请求
    const mergeForm = new FormData()
    mergeForm.append("merge", "true")
    mergeForm.append("fileName", `${name.value}.zip`)
    mergeForm.append("totalChunks", totalChunks.value)
    mergeForm.append("maxFileSizeKB", maxFileSizeKB.value)
    mergeForm.append("uploadId", uploadId)
    mergeForm.append("project", new Blob([JSON.stringify(projectMeta)], { type: "application/json" }))

    const mergeRes = await addProjectWithFiles(mergeForm)
    if (mergeRes.code !== 200) throw new Error(mergeRes.info || '合并上传失败')

    message.success('上传成功')
    emit('submit', { name: name.value })
    emit('update:open', false)
    resetAll()
  } catch (err) {
    console.error('上传错误:', err)
    message.error('上传失败: ' + (err.message || '请检查网络连接和后端服务'))
  } finally {
    confirmLoading.value = false
  }
}

function handleCancel() {
  emit('update:open', false)
  resetAll()
}

function resetAll() {
  name.value = ''; 
  description.value = ''; 
  type.value = ''; 
  readme.value = ''
  fileTree.value = []; 
  flatFiles.value = []; 
  totalFiles.value = 0; 
  totalDirs.value = 0; 
  totalBytes.value = 0
  filesWithContent.value = 0; 
  reading.value = false; 
  errorMsg.value = null; 
  selectedFilePath.value = ''
  uploadProgress.value = 0;
  currentChunk.value = 0;
  totalChunks.value = 0;
  confirmLoading.value = false;
  if (fileInput.value) fileInput.value.value = null
}

// 初始化主题 - 从localStorage和系统设置中获取
const initTheme = () => {
  const savedMode = localStorage.getItem('darkMode')
  
  if (savedMode !== null) {
    isDark.value = savedMode === 'true'
  } else {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    isDark.value = prefersDark
  }
  
  // 设置body上的主题数据属性
  document.body.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
  
  // 强制触发一次更新，确保样式正确应用
  nextTick(() => {
    // 这里不做任何操作，只是确保Vue的响应式系统能够捕获到isDark的变化
    console.log('Theme initialized:', isDark.value ? 'dark' : 'light')
  })
}

// 生命周期钩子 - 立即初始化主题，确保组件创建时就能响应主题变化
onMounted(() => {
  initTheme()
  // 监听主题变化事件
  window.addEventListener('themeChanged', handleThemeChange)
  
  // 监听系统主题变化
  const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  mediaQuery.addEventListener('change', handleSystemThemeChange)
  
  // 组件卸载时移除事件监听
  onUnmounted(() => {
    window.removeEventListener('themeChanged', handleThemeChange)
    mediaQuery.removeEventListener('change', handleSystemThemeChange)
  })
})
</script>

<style>
/* 定义CSS变量 - 统一主题颜色 */
:root {
  /* 明亮主题 */
  --light-bg-gradient: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  --light-header-gradient: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
  --light-border-color: #e2e8f0;
  --light-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  --light-text-primary: #2d3748;
  --light-text-secondary: #4a5568;
  --light-input-bg: #ffffff;
  --light-input-border: rgba(59, 130, 246, 0.4);
  --light-input-focus-border: rgba(59, 130, 246, 0.7);
  --light-input-focus-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
  
  /* 暗色主题 */
  --dark-bg-gradient: linear-gradient(145deg, rgba(23, 25, 35, 0.98), rgba(17, 24, 39, 0.95));
  --dark-header-gradient: linear-gradient(145deg, rgba(39, 49, 65, 0.95), rgba(31, 41, 55, 0.9));
  --dark-border-color: rgba(255, 255, 255, 0.15);
  --dark-shadow: 0 12px 36px rgba(0, 0, 0, 0.4);
  --dark-text-primary: rgba(255, 255, 255, 0.95);
  --dark-text-secondary: rgba(209, 213, 219, 0.9);
  --dark-input-bg: rgba(31, 41, 55, 0.95);
  --dark-input-border: rgba(96, 165, 250, 0.4);
  --dark-input-focus-border: rgba(96, 165, 250, 0.7);
  --dark-input-focus-shadow: 0 0 0 3px rgba(96, 165, 250, 0.3);
  
  /* 按钮样式 */
  --btn-primary-gradient: linear-gradient(145deg, rgba(59, 130, 246, 0.9), rgba(37, 99, 235, 0.9));
  --btn-primary-hover-gradient: linear-gradient(145deg, rgba(59, 130, 246, 1), rgba(37, 99, 235, 1));
  --btn-primary-border: rgba(59, 130, 246, 0.4);
  --btn-primary-hover-border: rgba(59, 130, 246, 0.6);
  
  /* 进度条样式 */
  --progress-gradient: linear-gradient(90deg, rgba(59, 130, 246, 0.8), rgba(37, 99, 235, 0.8));
}

/* ===== 明亮主题样式 ===== */
/* 使用modalClassName应用到Modal内容 */
.light-modal .ant-modal-content {
  background: var(--light-bg-gradient);
  border: 1px solid var(--light-border-color);
  box-shadow: var(--light-shadow);
}

.light-modal .ant-modal-header {
  background: var(--light-header-gradient);
  border-bottom: 1px solid var(--light-border-color);
}

.light-modal .ant-modal-title {
  color: var(--light-text-primary);
  font-weight: 700;
}

.light-modal .ant-modal-body {
  background: var(--light-bg-gradient);
}

.light-modal .ant-modal-footer {
  background: var(--light-header-gradient);
  border-top: 1px solid var(--light-border-color);
}

.light-modal .ant-form-item-label > label {
  color: var(--light-text-secondary);
}

/* 输入控件样式 */
.light-modal .ant-input,
.light-modal .ant-select-selector,
.light-modal .ant-textarea {
  background: var(--light-input-bg);
  border: 1px solid var(--light-input-border);
  color: var(--light-text-primary);
}

.light-modal .ant-input:hover,
.light-modal .ant-select-selector:hover,
.light-modal .ant-textarea:hover {
  border-color: var(--light-input-focus-border);
}

.light-modal .ant-input:focus,
.light-modal .ant-select-selector:focus,
.light-modal .ant-textarea:focus {
  border-color: var(--light-input-focus-border);
  box-shadow: var(--light-input-focus-shadow);
}

/* 按钮样式 */
.light-modal .ant-btn-primary {
  background: var(--btn-primary-gradient);
  border: 1px solid var(--btn-primary-border);
  color: white;
}

.light-modal .ant-btn-primary:hover {
  background: var(--btn-primary-hover-gradient);
  border-color: var(--btn-primary-hover-border);
}

.light-modal .ant-btn-primary:active {
  background: var(--btn-primary-hover-gradient);
  border-color: var(--btn-primary-hover-border);
  opacity: 0.9;
}

.light-modal .ant-btn-primary:focus {
  outline: none;
  box-shadow: var(--light-input-focus-shadow);
}

.light-modal .ant-btn-default {
  background: white;
  border: 1px solid #d9d9d9;
  color: rgba(0, 0, 0, 0.85);
}

.light-modal .ant-btn-default:hover {
  border-color: var(--btn-primary-border);
  color: var(--btn-primary-border);
}

.light-modal .ant-btn-default:active {
  border-color: var(--btn-primary-hover-border);
  color: var(--btn-primary-hover-border);
}

.light-modal .ant-progress-bg {
  background: var(--progress-gradient);
}

/* ===== 暗色主题样式 ===== */
/* 使用modalClassName应用到Modal内容 */
.dark-modal .ant-modal-content {
  background: var(--dark-bg-gradient);
  border: 1px solid var(--dark-border-color);
  box-shadow: var(--dark-shadow);
}

.dark-modal .ant-modal-header {
  background: var(--dark-header-gradient);
  border-bottom: 1px solid var(--dark-border-color);
}

.dark-modal .ant-modal-title {
  color: var(--dark-text-primary);
  font-weight: 700;
}

.dark-modal .ant-modal-body {
  background: var(--dark-bg-gradient);
}

.dark-modal .ant-modal-footer {
  background: var(--dark-header-gradient);
  border-top: 1px solid var(--dark-border-color);
}

.dark-modal .ant-form-item-label > label {
  color: var(--dark-text-primary);
  font-weight: 500;
}

/* 输入控件样式 */
.dark-modal .ant-input,
.dark-modal .ant-select-selector,
.dark-modal .ant-textarea {
  background: var(--dark-input-bg);
  border: 1px solid var(--dark-input-border);
  color: var(--dark-text-primary);
}

.dark-modal .ant-input:hover,
.dark-modal .ant-select-selector:hover,
.dark-modal .ant-textarea:hover {
  border-color: var(--dark-input-focus-border);
}

.dark-modal .ant-input:focus,
.dark-modal .ant-select-selector:focus,
.dark-modal .ant-textarea:focus {
  border-color: var(--dark-input-focus-border);
  box-shadow: var(--dark-input-focus-shadow);
}

/* 按钮样式 */
.dark-modal .ant-btn-primary {
  background: var(--btn-primary-gradient);
  border: 1px solid var(--btn-primary-border);
  color: white;
}

.dark-modal .ant-btn-primary:hover {
  background: var(--btn-primary-hover-gradient);
  border-color: var(--btn-primary-hover-border);
}

.dark-modal .ant-btn-primary:active {
  background: var(--btn-primary-hover-gradient);
  border-color: var(--btn-primary-hover-border);
  opacity: 0.9;
}

.dark-modal .ant-btn-primary:focus {
  outline: none;
  box-shadow: var(--dark-input-focus-shadow);
}

.dark-modal .ant-btn-default {
  background: rgba(55, 65, 81, 0.9);
  border: 1px solid rgba(75, 85, 99, 0.6);
  color: rgba(255, 255, 255, 0.9);
}

.dark-modal .ant-btn-default:hover {
  background: rgba(55, 65, 81, 1);
  border-color: rgba(96, 165, 250, 0.5);
  color: white;
}

.dark-modal .ant-btn-default:active {
  background: rgba(55, 65, 81, 1);
  border-color: rgba(96, 165, 250, 0.7);
  color: white;
}

.dark-modal .ant-progress-bg {
  background: var(--progress-gradient);
}

/* ===== 文本和背景颜色 ===== */
/* 统一文本颜色 */
.dark-modal .text-gray-500,
.dark-modal .text-gray-600 {
  color: var(--dark-text-secondary);
}

.dark-modal .text-sm {
  color: var(--dark-text-primary);
}

/* 统一背景颜色 */
.dark-modal .bg-gray-50 {
  background: rgba(31, 41, 55, 0.8);
  border-color: rgba(96, 165, 250, 0.3);
}

.dark-modal .bg-white {
  background: rgba(31, 41, 55, 0.95);
  border-color: rgba(96, 165, 250, 0.3);
}

/* 进度条样式 */
.dark-modal .ant-progress-bg {
  background: linear-gradient(90deg, #3b82f6, #2563eb);
}

/* 文本强调 */
.dark-modal .font-semibold,
.dark-modal .font-medium {
  color: var(--dark-text-primary);
}
/* 添加全局主题样式，基于body[data-theme]属性 */
body[data-theme="dark"] .ant-modal-content {
  background: var(--dark-bg-gradient);
  border: 1px solid var(--dark-border-color);
  box-shadow: var(--dark-shadow);
}

body[data-theme="dark"] .ant-modal-header {
  background: var(--dark-header-gradient);
  border-bottom: 1px solid var(--dark-border-color);
}

body[data-theme="dark"] .ant-modal-title {
  color: var(--dark-text-primary);
}

body[data-theme="dark"] .ant-modal-body {
  background: var(--dark-bg-gradient);
}

body[data-theme="dark"] .ant-modal-footer {
  background: var(--dark-header-gradient);
  border-top: 1px solid var(--dark-border-color);
}

/* 明亮主题全局样式 */
body[data-theme="light"] .ant-modal-content {
  background: var(--light-bg-gradient);
  border: 1px solid var(--light-border-color);
  box-shadow: var(--light-shadow);
}

body[data-theme="light"] .ant-modal-header {
  background: var(--light-header-gradient);
  border-bottom: 1px solid var(--light-border-color);
}

body[data-theme="light"] .ant-modal-title {
  color: var(--light-text-primary);
}

body[data-theme="light"] .ant-modal-body {
  background: var(--light-bg-gradient);
}

body[data-theme="light"] .ant-modal-footer {
  background: var(--light-header-gradient);
  border-top: 1px solid var(--light-border-color);
}
</style>