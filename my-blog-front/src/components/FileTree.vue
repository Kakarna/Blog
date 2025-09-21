<!-- FileTree.vue -->
<template>
  <ul class="pl-3">
    <li v-for="item in files" :key="item.path" class="mb-0.5">
      <div
        :class="[
          'flex items-center gap-2 cursor-pointer rounded px-2 py-1.5 transition-all duration-200',
          item.isDir 
            ? 'hover:bg-translucent-medium'
            : props.selectedFile === item.path 
              ? 'bg-accent-color text-white shadow-md'
              : 'hover:bg-translucent-medium',
          props.selectedFile === item.path && 'ring-2 ring-accent-color'
        ]"
        :style="{
          color: item.isDir 
            ? 'var(--text-primary)'
            : props.selectedFile === item.path 
              ? 'white'
              : 'var(--text-secondary)'
        }"
        @click="handleItemClick(item)"
      >
        <!-- 展开/折叠指示器 -->
        <span v-if="item.isDir && item.children && item.children.length" class="shrink-0 text-xs mr-1" style="color: var(--text-tertiary)">
          {{ item.open ? '▼' : '►' }}
        </span>

        <!-- 文件夹图标 -->
        <span v-if="item.isDir" class="select-none" style="color: var(--accent-color)">
          <svg v-if="item.open" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
            <path d="M2 6a2 2 0 012-2h3l2 2h7a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
            <path d="M2 6a2 2 0 012-2h10a2 2 0 012 2v2H4a2 2 0 01-2-2z"/>
            <path d="M4 10h12v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z" />
          </svg>
        </span>
        
        <!-- 文件图标 -->
        <span v-else class="w-4 text-center" style="color: var(--text-secondary)">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 6a1 1 极速统计0 011-1h6a1 1 0 110 2H7a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H7z" clip-rule="evenodd" />
          </svg>
        </span>



        <!-- 文件/目录名称 -->
        <span 
          class="flex-1 text-sm font-medium whitespace-nowrap" 
          :title="item.name"
        >{{ item.name }}</span>
      </div>

      <!-- 递归渲染子目录 -->
      <div v-if="item.isDir && item.children && item.children.length" v-show="item.open" class="ml-6 border-l-2 pl-2" style="border-color: var(--border-color)">
        <FileTree
          :files="item.children"
          :selected-file="props.selectedFile"
          @selectFile="$emit('selectFile', $event)"
        />
      </div>
    </li>
  </ul>
</template>

<script setup>
const props = defineProps({
  files: { type: Array, default: () => [] },
  selectedFile: { type: String, default: '' }
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

