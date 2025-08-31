<template>
  <a-modal :open="open" title="上传项目" :width="1000" :confirmLoading="confirmLoading" @ok="handleOk"
    @cancel="handleCancel" destroyOnClose>
    <div class="grid grid-cols-3 gap-6">
      <!-- 左侧表单 -->
      <div class="col-span-2 space-y-4">
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
        <div class="p-3 border rounded bg-gray-50 space-y-3">
          <div class="font-semibold">识别结果</div>
          <div class="text-sm text-gray-600">
            <div>文件数：<span class="font-medium">{{ totalFiles }}</span></div>
            <div>文件夹数：<span class="font-medium">{{ totalDirs }}</span></div>
            <div>总大小：<span class="font-medium">{{ formatBytes(totalBytes) }}</span></div>
            <div>已读取内容：<span class="font-medium">{{ filesWithContent }}</span></div>
          </div>
          <div v-if="reading" class="mt-2 text-sm text-blue-600">正在分析文件，请稍候...</div>
          <div v-if="errorMsg" class="mt-2 text-sm text-red-600">{{ errorMsg }}</div>
          <div v-if="fileTree.length" class="mt-3 max-h-64 overflow-auto border rounded p-2 bg-white">
            <FileTree :files="fileTree" :selectedFile="selectedFilePath" @selectFile="handleFileSelect" />
          </div>
          <div v-else class="mt-3 text-sm text-gray-500">尚未识别到文件结构</div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import FileTree from './FileTree.vue'
import { addProjectWithFiles } from '@/api/project'

const props = defineProps({ open: Boolean })
const emit = defineEmits(['update:open', 'submit'])

const name = ref('')
const description = ref('')
const type = ref('')
const readme = ref('')
const typeOptions = ['个人项目', '课程项目', '开源项目', '实习项目', '实验项目', '工具/库']
const includeContents = ref(true)
const maxFileSizeKB = ref(1024)

const fileTree = ref([])
let flatFiles = []
const totalFiles = ref(0)
const totalDirs = ref(0)
const totalBytes = ref(0)
const filesWithContent = ref(0)
const reading = ref(false)
const confirmLoading = ref(false)
const errorMsg = ref(null)
const selectedFilePath = ref('')
const fileInput = ref(null)

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

function makeReactiveTree(nodes) {
  nodes.forEach(node => {
    if (node.isDir && node.children && node.children.length) makeReactiveTree(node.children)
  })
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
      try { const text = await readFileAsText(fileObj); item.content = text; countWithContent++; if (/^readme\.md$/i.test(fileObj.name) && !readme.value) readme.value = text } catch { item.content = null }
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
    let totalB = 0; rawFiles.forEach(f => totalB += f.size || 0)
    totalBytes.value = totalB
    totalFiles.value = rawFiles.length
    const root = buildFileTreeFromFileList(rawFiles)
    makeReactiveTree(root)
    fileTree.value = root
    const { flat, filesWithContent: cnt } = await prepareFlatFiles(rawFiles, includeContents.value, maxFileSizeKB.value)
    flatFiles = flat
    filesWithContent.value = cnt
    totalDirs.value = flat.filter(i => i.isDir).length
  } catch (err) {
    console.error(err)
    errorMsg.value = '读取文件时出错，请重试'
  } finally { reading.value = false }
}

function formatBytes(bytes) {
  if (!bytes) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']; let i = 0; let num = bytes
  while (num >= 1024 && i < units.length - 1) { num /= 1024; i++ }
  return `${num.toFixed(2)} ${units[i]}`
}

async function handleOk() {
  if (!name.value || !type.value) {
    message.error('请填写项目名称和类型')
    return
  }
  if (!flatFiles || !flatFiles.length) {
    message.error('请先选择项目文件夹并等待分析完成')
    return
  }

  confirmLoading.value = true
  try {
    const payload = {
      name: name.value,
      description: description.value,
      type: type.value,
      readme: readme.value,
      files: flatFiles
    }

    const res = await addProjectWithFiles(payload)

    // 假设后端统一返回 { code, msg, data }
    if (res?.code === 200) {
      message.success('上传成功')
      emit('submit', res.data)
      emit('update:open', false)
      resetAll()
    } else if (res?.code === 403 || res?.code === 401) {
      message.error('权限不足，无法上传项目')
    } else {
      message.error(res?.msg || '上传失败，请检查后端或控制台日志')
    }

  } catch (err) {
    console.error(err)
    if (err.response?.status === 403) {
      message.error('权限不足，无法上传项目')
    } else if (err.response?.status === 401) {
      message.error('未登录或会话过期，请先登录')
    } else {
      message.error('上传失败，请检查后端或控制台日志')
    }
  } finally {
    confirmLoading.value = false
  }
}


function handleCancel() {
  emit('update:open', false)
  resetAll()
}

function resetAll() {
  name.value = ''; description.value = ''; type.value = ''; readme.value = ''
  fileTree.value = []; flatFiles = []; totalFiles.value = 0; totalDirs.value = 0; totalBytes.value = 0
  filesWithContent.value = 0; reading.value = false; errorMsg.value = null; selectedFilePath.value = ''
  if (fileInput.value) fileInput.value.value = null
}
</script>
