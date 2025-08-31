<template>
  <div class="p-6 max-w-6xl mx-auto">
    <!-- 头部 -->
    <div class="flex justify-between items-start mb-6">
      <div>
        <h2 class="text-xl font-semibold text-gray-800">撰写笔记</h2>
        <p class="text-sm text-gray-500">{{ section }}专区</p>
      </div>
      <div class="flex gap-2">
        <RouterLink :to="`/techNotes/${section}`" class="px-3 py-1 text-sm border rounded hover:bg-gray-100 transition">
          返回列表
        </RouterLink>
      </div>
    </div>

    <!-- 两栏 -->
    <div class="flex gap-6">
      <!-- 左侧编辑区 -->
      <div class="flex-1">
        <input v-model="title"
          class="w-full border-b px-2 py-3 text-lg font-medium focus:outline-none focus:border-blue-500 mb-4"
          placeholder="输入笔记标题..." />

        <!-- 工具栏 -->
        <div class="flex flex-wrap gap-1 mb-4 p-2 border-b">
          <button @click="editor?.chain().focus().toggleBold().run()"
            class="p-1 rounded hover:bg-gray-100"><strong>B</strong></button>
          <button @click="editor?.chain().focus().toggleItalic().run()"
            class="p-1 rounded hover:bg-gray-100"><em>I</em></button>
          <button @click="editor?.chain().focus().toggleHeading({ level: 2 }).run()"
            class="p-1 rounded hover:bg-gray-100">标题</button>
          <button @click="editor?.chain().focus().toggleBulletList().run()" class="p-1 rounded hover:bg-gray-100">•
            列表</button>
          <button @click="editor?.chain().focus().toggleCodeBlock().run()" class="p-1 rounded hover:bg-gray-100">{
            }</button>
          <button @click="triggerImageUpload" class="p-1 rounded hover:bg-gray-100">图片</button>
          <button @click="setLink" class="p-1 rounded hover:bg-gray-100">链接</button>
          <button @click="setColor" class="p-1 rounded hover:bg-gray-100">颜色</button>
        </div>

        <!-- 隐藏文件上传input -->
        <input type="file" ref="fileInput" accept="image/*" class="hidden" @change="handleImageUpload" />

        <!-- 编辑器 -->
        <div class="border rounded-lg overflow-hidden">
          <EditorContent :editor="editor" class="min-h-[400px] px-4 py-3 text-sm" />
        </div>

        <!-- 保存 -->
        <button @click="handleSubmit"
          class="mt-4 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition text-sm">
          保存笔记
        </button>
      </div>

      <!-- 右侧预览 -->
      <div class="w-64 border-l pl-6">
        <h3 class="text-sm font-medium text-gray-700 mb-2">预览</h3>
        <div class="prose prose-sm max-w-none text-gray-600 whitespace-pre-wrap text-left"
          v-html="editor?.getHTML() || defaultPlaceholder"></div>
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

const defaultPlaceholder = '<p class="text-gray-400">输入内容将显示在这里</p>'

const route = useRoute()
const router = useRouter()
const section = route.params.section
const title = ref('')

const noteId = route.params.id
const isEdit = !!noteId

const fileInput = ref(null)

const editor = useEditor({
  extensions: [
    StarterKit.configure({
      codeBlock: false,
    }),
    TextStyle,
    Color,
    Image,
    Link.configure({
      openOnClick: false,
    }),
    TaskList,
    TaskItem.configure({
      nested: true,
    }),
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
    const base64 = reader.result
    editor.value?.chain().focus().setImage({ src: base64 }).run()
  }
  reader.readAsDataURL(file)

  // 清空input，方便下次上传相同文件
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
  if (color) {
    editor.value?.chain().focus().setColor(color).run()
  }
}

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
  }

  try {
    let res
    if (isEdit) {
      res = await updateTechNote(params)
    } else {
      res = await addTechNote(params)
    }

    if (res?.code === 200) {
      alert(isEdit ? '笔记更新成功' : '笔记添加成功')
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

<style>
.ProseMirror {
  font-size: 14px;
  line-height: 1.6;
  padding: 1rem;
  min-height: 400px;
  white-space: pre-wrap;
  /* 多空格换行显示 */
  text-align: left;
  /* 文字靠左 */
  outline: none;
}

.ProseMirror:focus {
  outline: none;
}

.ProseMirror h2 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
}

/* 代码块样式 */
.ProseMirror pre {
  background-color: #1f2937;
  color: #f3f4f6;
  padding: 0.75rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  white-space: pre;
  /* 保持代码块格式 */
  overflow-x: auto;
}

/* 预览区 */
.prose-sm {
  font-size: 13px;
  line-height: 1.5;
  color: #4b5563;
  white-space: pre-wrap;
  /* 多空格换行显示 */
  text-align: left;
  /* 文字靠左 */
}

.prose-sm h2 {
  font-size: 1rem;
  font-weight: 500;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
}

.prose-sm ul {
  padding-left: 1.25rem;
  list-style-type: disc;
}

.prose-sm ol {
  padding-left: 1.25rem;
  list-style-type: decimal;
}
</style>
