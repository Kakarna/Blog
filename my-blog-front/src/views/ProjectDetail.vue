<template>
  <div class="flex flex-col md:flex-row border rounded overflow-hidden h-screen">
    <!-- 移动端文件树切换按钮 -->
    <div class="md:hidden p-3 border-b border-gray-200 dark:border-gray-700 bg-translucent-medium">
      <button
        @click="toggleFileTree"
        class="flex items-center gap-2 px-3 py-2 bg-blue-500 text-white rounded-lg shadow hover:bg-blue-600 transition"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd" />
        </svg>
        📁 文件目录
      </button>
    </div>

    <!-- 左侧文件树 -->
    <aside 
      :class="[
        'border-r flex flex-col min-w-0 bg-translucent-light backdrop-blur border-gray-200 dark:border-gray-700 transition-all duration-300',
        isMobileFileTreeOpen ? 'w-full absolute inset-0 z-50' : 'hidden md:block md:w-2/5'
      ]"
    >
      <!-- 移动端关闭按钮 -->
      <div v-if="isMobileFileTreeOpen" class="md:hidden p-3 border-b border-gray-200 bg-translucent-medium text-gray-800 dark:border-gray-700 dark:text-gray-200 flex justify-between items-center">
        <span class="font-semibold">📁 文件目录</span>
        <button @click="toggleFileTree" class="p-1 rounded hover:bg-gray-200 dark:hover:bg-gray-600">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
          </svg>
        </button>
      </div>
      
      <!-- 桌面端标题 -->
      <div v-else class="p-3 font-semibold border-b border-gray-200 bg-translucent-medium text-gray-800 dark:border-gray-700 dark:text-gray-200">
        📁 文件目录
      </div>
      
      <div class="flex-1 overflow-auto">
        <FileTree :files="fileTree" :selected-file="selectedFile" @selectFile="handleFileSelect" class="p-2" />
      </div>
    </aside>

    <!-- 右侧文件内容 -->
    <main class="flex-1 p-4 md:p-6 overflow-auto flex flex-col min-w-0">
      <!-- 返回按钮 -->
      <div class="mb-4 flex items-center">
        <button
          @click="goBack"
          class="px-3 py-1.5 bg-blue-500 text-white rounded-lg shadow hover:bg-blue-600 transition"
        >
          ← 返回我的项目
        </button>
      </div>

      <div class="flex-1 overflow-auto">
        <template v-if="selectedFile.endsWith('.md')">
          <div class="prose max-w-none" v-html="renderedMarkdown"></div>
        </template>
        <template v-else-if="selectedFile">
          <div ref="editorContainer" class="editor-container h-full border rounded"></div>
        </template>
        <template v-else>
          <div class="flex items-center justify-center h-64 text-gray-500">
            请选择一个文件查看内容
          </div>
        </template>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import FileTree from '../components/FileTree.vue'
import { getProjectFiles, getFileContent } from '@/api/projectFile'

import { EditorState } from '@codemirror/state'
import { EditorView, basicSetup } from 'codemirror'
import { oneDark } from '@codemirror/theme-one-dark'
import { javascript } from '@codemirror/lang-javascript'
import { java } from '@codemirror/lang-java'
import { markdown } from '@codemirror/lang-markdown'
import { html } from '@codemirror/lang-html'
import { css } from '@codemirror/lang-css'
import { python } from '@codemirror/lang-python'

const editorContainer = ref(null)
let editorView = null

const route = useRoute()
const router = useRouter()
const projectId = ref(route.params.projectId)

const fileTree = ref([])
const fileContents = ref({})
const selectedFile = ref('')
const renderedMarkdown = ref('')
const isMobileFileTreeOpen = ref(false)

// 返回逻辑
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
    // 默认返回到项目列表
    router.push({ name: 'MyProjects' })
  }
}

// 判断是否是目录
function isDirectory(path) {
  return path.endsWith('/')
}

// 构建文件树
function buildFileTree(files) {
  const nodeMap = new Map()
  const rootNodes = []

  files.forEach(file => {
    nodeMap.set(file.path, {
      path: file.path,
      name: file.path.split('/').filter(Boolean).pop(),
      isDir: isDirectory(file.path),
      children: [],
      open: true,
    })
  })

  files.forEach(file => {
    const node = nodeMap.get(file.path)
    if (!node) return
    const parts = file.path.split('/').filter(Boolean)
    if (parts.length > 1) {
      const parentPath = parts.slice(0, -1).join('/') + '/'
      const parentNode = nodeMap.get(parentPath)
      if (parentNode) parentNode.children.push(node)
      else rootNodes.push(node)
    } else rootNodes.push(node)
  })

  const sortNodes = nodes => nodes.sort((a, b) => (a.isDir && !b.isDir ? -1 : !a.isDir && b.isDir ? 1 : a.name.localeCompare(b.name)))
  sortNodes(rootNodes)
  rootNodes.forEach(node => node.children && sortNodes(node.children))
  return rootNodes
}

// 找到第一个非目录文件
function findFirstFile(files) {
  for (const file of files) {
    if (!file.isDir) return file
    if (file.children?.length) {
      const found = findFirstFile(file.children)
      if (found) return found
    }
  }
  return null
}

// 获取文件树
async function fetchFileTree() {
  if (!projectId.value) return
  try {
    const res = await getProjectFiles(projectId.value)
    const files = (res.data || []).map(f => ({ ...f, isDir: isDirectory(f.path) }))
    fileTree.value = buildFileTree(files)

    const firstFile = findFirstFile(fileTree.value)
    if (firstFile) selectFile(firstFile.path)
  } catch (err) {
    console.error('获取文件列表失败', err)
  }
}

// 获取文件内容
async function fetchFileContent(path) {
  if (fileContents.value[path]) return
  try {
    const res = await getFileContent(projectId.value, path)
    fileContents.value[path] = res.data || ''
  } catch {
    fileContents.value[path] = '无法加载文件内容'
  }
}

// 切换文件树显示（移动端）
function toggleFileTree() {
  isMobileFileTreeOpen.value = !isMobileFileTreeOpen.value
}

// 处理文件选择（移动端自动关闭文件树）
function handleFileSelect(path) {
  selectFile(path)
  // 移动端选择文件后自动关闭文件树
  if (window.innerWidth < 768) {
    isMobileFileTreeOpen.value = false
  }
}

// 选中文件
async function selectFile(path) {
  if (isDirectory(path)) return
  selectedFile.value = path
  if (!fileContents.value[path]) await fetchFileContent(path)

  if (path.endsWith('.md')) {
    renderedMarkdown.value = marked.parse(fileContents.value[path])
  } else {
    initEditor(path, fileContents.value[path])
  }
}

// CodeMirror 初始化
function getLanguageExtension(path) {
  const ext = path.split('.').pop().toLowerCase()
  switch (ext) {
    case 'js': case 'jsx': return javascript()
    case 'ts': case 'tsx': return javascript({ typescript: true })
    case 'java': return java()
    case 'md': return markdown()
    case 'html': return html()
    case 'css': return css()
    case 'py': return python()
    default: return []
  }
}

function initEditor(path, content) {
  if (!editorContainer.value) return
  if (editorView) editorView.destroy()
  editorContainer.value.innerHTML = ''

  editorView = new EditorView({
    state: EditorState.create({
      doc: content,
      extensions: [basicSetup, oneDark, EditorView.editable.of(false), getLanguageExtension(path)]
    }),
    parent: editorContainer.value
  })
}

// Markdown 高亮
marked.setOptions({
  highlight: (code, lang) => {
    if (lang && hljs.getLanguage(lang)) return hljs.highlight(code, { language: lang }).value
    return hljs.highlightAuto(code).value
  }
})

onMounted(() => {
  fetchFileTree()
})
</script>

<style>
.editor-container .cm-content {
  padding: 0 !important;
  margin: 0 !important;
  text-align: left !important;
}

.prose pre {
  background-color: #f6f8fa;
  padding: 16px;
  border-radius: 6px;
  overflow: auto;
}

.prose code {
  background-color: #f6f8fa;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Monaco','Menlo','Ubuntu Mono',monospace;
}

/* 移动端优化 */
@media (max-width: 767px) {
  .prose {
    font-size: 14px;
    line-height: 1.6;
  }
  
  .prose pre {
    padding: 12px;
    font-size: 13px;
  }
  
  .editor-container {
    font-size: 14px;
  }
}

/* 文件树移动端遮罩 */
@media (max-width: 767px) {
  aside:not(.hidden) {
    background: rgba(255, 255, 255, 0.95);
  }
  
  [data-theme="dark"] aside:not(.hidden) {
    background: rgba(13, 17, 23, 0.95);
  }
}
</style>
