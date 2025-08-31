<!-- FileTree.vue -->
<template>
  <ul class="pl-3">
    <li v-for="item in files" :key="item.path" class="mb-0.5">
      <div
        class="flex items-center gap-2 cursor-pointer hover:bg-gray-100 rounded px-2 py-0.5"
        @click="handleItemClick(item)"
      >
        <!-- 文件夹图标 -->
        <span v-if="item.isDir" class="select-none">
          <svg v-if="item.open" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 inline-block" viewBox="0 0 20 20" fill="currentColor">
            <path d="M2 6a2 2 0 012-2h3l2 2h7a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 inline-block" viewBox="0 0 20 20" fill="currentColor">
            <path d="M2 6a2 2 0 012-2h10a2 2 0 012 2v2H4a2 2 0 01-2-2z"/>
            <path d="M4 10h12v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z" />
          </svg>
        </span>
        
        <!-- 文件图标 -->
        <span v-else class="w-4 inline-block text-center">📄</span>

        <!-- 文件/目录名称 -->
        <span class="truncate text-sm">{{ item.name }}</span>
      </div>

      <!-- 递归渲染子目录 -->
      <FileTree
        v-if="item.isDir && item.children && item.children.length"
        :files="item.children"
        @selectFile="$emit('selectFile', $event)"
        v-show="item.open"
      />
    </li>
  </ul>
</template>

<script setup>
const props = defineProps({
  files: { type: Array, default: () => [] }
})
const emit = defineEmits(['selectFile'])

function handleItemClick(item) {
  if (item.isDir) {
    // 切换文件夹展开状态
    item.open = !item.open
  } else {
    // 选中文件
    emit('selectFile', item.path)
  }
}
</script>