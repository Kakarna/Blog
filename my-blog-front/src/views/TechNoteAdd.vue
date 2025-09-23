<template>
  <div class="p-6 max-w-6xl mx-auto theme-bg-primary">
    <!-- 头部 -->
    <div class="flex justify-between items-start mb-6">
      <div>
        <h2 class="text-2xl font-bold theme-text-primary">✏️ 撰写笔记</h2>
        <p class="text-sm theme-text-secondary">{{ section }} 专区</p>
      </div>
      <div>
        <RouterLink
          :to="`/techNotes/${section}`"
          class="px-3 py-1.5 text-sm border rounded-lg theme-shadow hover:theme-bg-hover transition"
        >
          返回列表
        </RouterLink>
      </div>
    </div>

    <!-- 两栏布局 -->
    <div class="flex gap-6">
      <!-- 左侧编辑区 -->
      <div class="flex-1">
        <!-- 标题输入框 -->
        <input
          v-model="title"
          class="w-full theme-input px-3 py-3 text-lg font-medium rounded-lg 
                 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-4 transition"
          placeholder="输入笔记标题..."
        />

        <!-- 工具栏 -->
        <div class="flex flex-wrap gap-1 mb-4 p-2 theme-bg-input border rounded-lg shadow-sm">
          <button @click="editor?.chain().focus().toggleBold().run()" class="toolbar-btn"><strong>B</strong></button>
          <button @click="editor?.chain().focus().toggleItalic().run()" class="toolbar-btn"><em>I</em></button>
          <button @click="editor?.chain().focus().toggleHeading({ level: 2 }).run()" class="toolbar-btn">标题</button>
          <button @click="editor?.chain().focus().toggleBulletList().run()" class="toolbar-btn">• 列表</button>
          <button @click="editor?.chain().focus().toggleCodeBlock().run()" class="toolbar-btn">{ }</button>
          <button @click="triggerImageUpload" class="toolbar-btn">图片</button>
          <button @click="setLink" class="toolbar-btn">链接</button>
          <button @click="setColor" class="toolbar-btn">颜色</button>
        </div>

        <!-- 隐藏文件上传 -->
        <input type="file" ref="fileInput" accept="image/*" class="hidden" @change="handleImageUpload" />

        <!-- 编辑器 -->
        <div class="editor-wrapper">
          <EditorContent :editor="editor" class="editor-content" />
        </div>

        <!-- 保存按钮 -->
        <button
          @click="handleSubmit"
          class="mt-4 theme-btn-primary text-white px-5 py-2 rounded-lg shadow hover:theme-btn-hover hover:shadow-md transition"
        >
          保存笔记
        </button>
      </div>

      <!-- 右侧预览 -->
      <div class="w-72 border-l pl-6">
        <h3 class="text-sm font-medium theme-text-secondary mb-2">预览</h3>
        <div
          class="prose prose-sm max-w-none theme-text-primary whitespace-pre-wrap text-left p-3 rounded-lg"
          :class="{'bg-blue-50/80 dark:bg-blue-900/30 border border-blue-200 dark:border-blue-700': editor?.getHTML(), 'bg-gray-50/50 dark:bg-gray-800/30 border border-gray-200 dark:border-gray-700': !editor?.getHTML()}"
          v-html="editor?.getHTML() || defaultPlaceholder"
        ></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Image from '@tiptap/extension-image'
import Link from '@tiptap/extension-link'
import TaskList from '@tiptap/extension-task-list'
import TaskItem from '@tiptap/extension-task-item'
import CodeBlock from '@tiptap/extension-code-block'
import Blockquote from '@tiptap/extension-blockquote'
import HorizontalRule from '@tiptap/extension-horizontal-rule'
import { TextStyle } from '@tiptap/extension-text-style'
import { Color } from '@tiptap/extension-color'

import { addTechNote, getTechNoteById, updateTechNote } from '@/api/techNote'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const section = route.params.section
const title = ref('')
const noteId = route.params.id
const isEdit = !!noteId
const fileInput = ref(null)
const defaultPlaceholder = '<p class="text-gray-500 dark:text-gray-400 italic">暂无内容，开始编写吧...</p>'

const editor = useEditor({
  extensions: [
    StarterKit.configure({ codeBlock: false }),
    TextStyle,
    Color,
    Image,
    Link.configure({ openOnClick: false }),
    TaskList,
    TaskItem.configure({ nested: true }),
    CodeBlock,
    Blockquote,
    HorizontalRule,
  ],
  content: '<p>开始编写你的笔记...</p>',
})

// 编辑器初始化时加载已有内容
onMounted(async () => {
  if (isEdit && noteId) {
    try {
      const res = await getTechNoteById(noteId)
      const data = res?.data
      title.value = data?.title || ''
      editor.value?.commands.setContent(data?.content || '<p></p>')
    } catch (e) {
      alert('加载笔记失败')
    }
  }
})

function triggerImageUpload() {
  fileInput.value?.click()
}

function handleImageUpload(event) {
  const file = event.target.files[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = () => {
    editor.value?.chain().focus().setImage({ src: reader.result }).run()
  }
  reader.readAsDataURL(file)
  event.target.value = ''
}

function setLink() {
  const previousUrl = editor.value?.getAttributes('link').href
  const url = window.prompt('输入链接 URL', previousUrl)
  if (url === null) return
  if (url === '') {
    editor.value?.chain().focus().unsetLink().run()
    return
  }
  editor.value?.chain().focus().setLink({ href: url }).run()
}

function setColor() {
  const color = window.prompt('输入颜色代码（例如 red 或 #f00）')
  if (color) editor.value?.chain().focus().setColor(color).run()
}

// 保存笔记
async function handleSubmit() {
  if (!editor.value) return

  const contentHTML = editor.value.getHTML()

  if (!title.value.trim() || !contentHTML.trim()) {
    alert('请输入完整信息')
    return
  }

  const params = {
    id: noteId,
    sectionName: section,
    title: title.value,
    content: contentHTML,
    userId: userStore.user?.id // 添加用户ID
  }

  try {
    console.log('传递的笔记参数:', JSON.stringify(params, null, 2))
    let res
    if (isEdit) {
      res = await updateTechNote(params)
    } else {
      res = await addTechNote(params)
    }

    if (res?.code === 200) {
      alert(isEdit ? '笔记更新成功' : '笔记添加成功')
      router.push({ name: 'TechNoteList', params: { section } })
    } else {
      alert(res?.message || '保存失败')
    }
  } catch (err) {
    if (!err.handledByInterceptor) {
      alert('请求失败，请检查控制台')
    }
  }
}

onBeforeUnmount(() => {
  editor.value?.destroy()
})
</script>

<style scoped>
/* 编辑器整体容器 */
.editor-wrapper {
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--card-bg);
  box-shadow: inset 0 1px 3px var(--shadow-color);
  transition: all 0.2s;
}
.editor-wrapper:focus-within {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-color);
}

/* 编辑器内容 */
.editor-content {
  min-height: 400px;
  padding: 1rem;
  font-size: 14px;
  line-height: 1.6;
  outline: none;
  color: var(--text-primary);
}

/* 工具栏按钮 */
.toolbar-btn {
  padding: 0.35rem 0.6rem;
  border-radius: 6px;
  font-size: 0.85rem;
  color: var(--text-primary);
  transition: all 0.2s;
}
.toolbar-btn:hover {
  background: var(--bg-hover);
  transform: scale(1.05);
}

/* ProseMirror 默认样式 */
.ProseMirror h2 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: var(--text-primary);
}
.ProseMirror pre {
  background: var(--code-bg) !important;
  color: var(--code-text) !important;
  padding: 0.75rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  overflow-x: auto;
  border: 1px solid var(--border-color);
}
</style>
