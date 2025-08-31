<template>
  <div class="flex border rounded overflow-hidden">
    <!-- 左侧文件树 -->
    <aside class="w-1/4 bg-gray-50 border-r max-h-screen overflow-auto">
      <div class="p-3 font-semibold border-b bg-gray-100">
        文件目录
      </div>
      <FileTree :files="fileTree" @selectFile="selectFile" />
    </aside>

    <!-- 右侧文件内容 -->
    <main class="w-3/4 p-6 overflow-auto">
      <template v-if="selectedFile.endsWith('.md')">
        <div class="prose max-w-none" v-html="renderedMarkdown"></div>
      </template>
      <template v-else>
        <div ref="editorContainer" class="editor-container" style="height: calc(100vh - 100px); border:1px solid #ddd;"></div>
      </template>
    </main>
  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import FileTree from '../components/FileTree.vue'
import { getProjectFiles, getFileContent } from '@/api/projectFile'

// === CodeMirror 相关 ===
import { EditorState } from '@codemirror/state'
import { EditorView, basicSetup } from 'codemirror'
import { oneDark } from '@codemirror/theme-one-dark'
import { javascript } from '@codemirror/lang-javascript'
import { java } from '@codemirror/lang-java'
import { markdown } from '@codemirror/lang-markdown'

const editorContainer = ref(null)
let editorView = null


const route = useRoute()
const router = useRouter()
const projectId = ref(route.params.projectId)

const fileTree = ref([])
const fileContents = ref({})
const selectedFile = ref('')
const renderedMarkdown = ref('')
const codeBlock = ref(null)

// 给每个节点加 name 字段
import { reactive } from 'vue'

// 添加 name 和 open 字段，并递归处理所有子节点
function addName(vo, isRoot = false) {
  // 转为响应式
  const node = reactive(vo)
  node.name = node.path.split('/').pop()
  node.open = isRoot // 根目录默认展开
  node.children = node.children || []

  // 遍历子节点
  node.children.forEach(child => {
    // 如果父层是 root 或 open，则默认展开
    const childNode = reactive(child)
    // 二级及以下节点也可以默认展开
    childNode.open = node.open && childNode.isDir
    addName(childNode)
  })

  return node
}




// 获取项目文件树
// 获取项目文件树
async function fetchFileTree() {
  if (!projectId.value) return
  try {
    const res = await getProjectFiles(projectId.value)
    
    // 后端已经返回了树形结构，直接使用即可
    fileTree.value = processFileTree(res.data || [])
    
    // 默认选中第一个文件
    const firstFile = findFirstFile(fileTree.value)
    if (firstFile) selectFile(firstFile.path)
  } catch (err) {
    console.error('获取文件列表失败', err)
  }
}

// 处理文件树，添加必要的字段
function processFileTree(files) {
  return files.map(file => {
    // 使用 Vue 的 reactive 确保响应性
    const processedFile = reactive({
      ...file,
      name: file.path.split('/').pop(),
      open: file.isDir && file.children && file.children.length > 0 // 有子节点的目录默认展开
    })
    
    // 递归处理子节点
    if (file.children && file.children.length > 0) {
      processedFile.children = processFileTree(file.children)
    }
    
    return processedFile
  })
}


// 递归找到第一个文件
// 递归找到第一个文件
function findFirstFile(files) {
  for (const f of files) {
    if (!f.isDir) return f
    if (f.children && f.children.length > 0) {
      const child = findFirstFile(f.children)
      if (child) return child
    }
  }
  return null
}

// 获取文件内容
async function fetchFileContent(path) {
  try {
    const res = await getFileContent(projectId.value, path)
    fileContents.value = { ...fileContents.value, [path]: res.data || '' }
  } catch (err) {
    console.error('获取文件内容失败', err)
    fileContents.value = { ...fileContents.value, [path]: '' }
  }
}

// 选中文件
async function selectFile(path) {
  selectedFile.value = path
  if (!fileContents.value[path]) {
    await fetchFileContent(path)
  }

  if (path.endsWith('.md')) {
    renderedMarkdown.value = marked.parse(fileContents.value[path])
  } else {
    // 用 CodeMirror 渲染
    initEditor(path, fileContents.value[path])
  }
}

function initEditor(path, content) {
  if (!editorContainer.value) return

  // 销毁旧实例
  if (editorView) {
    editorView.destroy()
  }

  // 根据文件类型设置语言
  let lang = []
  if (path.endsWith('.js')) lang = [javascript()]
  else if (path.endsWith('.java')) lang = [java()]
  else if (path.endsWith('.md')) lang = [markdown()]

  editorView = new EditorView({
    state: EditorState.create({
      doc: content,
      extensions: [
        basicSetup,
        oneDark,
        EditorView.editable.of(false), // 只读
        ...lang
      ]
    }),
    parent: editorContainer.value
  })
}


// 高亮非 Markdown 文件
watchEffect(async () => {
  if (!selectedFile.value.endsWith('.md')) {
    await nextTick()
    if (codeBlock.value) {
      hljs.highlightElement(codeBlock.value)
    }
  }
})



onMounted(fetchFileTree)
</script>
<style lang="css">
.editor-container .cm-content {
  padding: 0 !important;        /* 去掉内边距 */
  margin: 0 !important;         /* 去掉外边距 */
  text-align: left !important;  /* 左对齐 */
}

</style>
