<template>
  <aside class="sidebar-container transition-all duration-300 ease-in-out" :class="{
    'sidebar-expanded': visible,
    'sidebar-collapsed': !visible,
    'md:flex': true
  }">
    <!-- 移动端关闭按钮 -->
    <div v-if="isMobile" class="mobile-close-btn" @click="handleClose">
      <span class="close-icon">×</span>
    </div>

    <div class="sidebar-content">
      <!-- 侧边栏顶部部分 -->
      <div class="sidebar-top">
        <ul class="space-y-2">
          <!-- 首页 -->
          <li>
            <router-link to="/" class="menu-item" active-class="menu-item-active" @click="isMobile && handleClose()">
              首页
            </router-link>
          </li>

          <!-- 技术笔记 -->
          <li>
            <router-link to="/techNotes" class="menu-item" active-class="menu-item-active" @click="toggle('技术笔记')">
              技术笔记
            </router-link>

            <ul v-if="open === '技术笔记'" class="ml-3 mt-2 space-y-1 border-l border-gray-200 pl-2">
              <li v-for="section in techNoteSections" :key="section.id" class="flex items-center gap-2 group relative">
                <div class="flex items-center gap-2 w-full">
                  <!-- 分区名或输入框 -->
                  <template v-if="editingId === section.id">
                    <input v-model="editName" @keyup.enter="handleUpdate(section.id)" @blur="handleUpdate(section.id)"
                      class="w-full px-2 py-1 text-base border rounded focus:outline-none focus:ring-1 focus:ring-blue-400 bg-white text-gray-800 dark:bg-gray-600 dark:text-gray-100"
                      autofocus />
                  </template>
                  <template v-else>
                    <router-link :to="`/techNotes/${section.name}`" class="sub-menu-item flex-1" title="点击查看笔记"
                      @click="isMobile && handleClose()">
                      {{ section.name }}
                    </router-link>
                  </template>

                  <!-- 操作按钮（编辑/删除） -->
                  <div v-if="editingId !== section.id" class="flex items-center">
                    <!-- 操作菜单按钮（所有设备） -->
                    <button @click="showActionMenu = showActionMenu === section.id ? null : section.id"
                      class="action-menu-button icon-btn text-gray-500 hover:text-blue-500 ml-2" title="操作">
                      ⋮
                    </button>

                    <!-- 操作菜单 -->
                    <div v-if="showActionMenu === section.id" 
                      class="action-menu-container absolute right-0 top-full mt-1 bg-white dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded shadow-lg z-10">
                      <button @click="startEdit(section.id, section.name); showActionMenu = null"
                        class="block w-full px-3 py-2 text-left text-sm hover:bg-gray-100 dark:hover:bg-gray-600">
                        编辑
                      </button>
                      <button @click="confirmDelete(section.id, section.name); showActionMenu = null"
                        class="block w-full px-3 py-2 text-left text-sm text-red-500 hover:bg-gray-100 dark:hover:bg-gray-600">
                        删除
                      </button>
                    </div>
                  </div>
                </div>
              </li>

              <!-- 添加按钮 -->
              <li v-if="!showInput">
                <button @click="showInput = true" class="sub-menu-add">➕ 添加分区</button>
              </li>

              <!-- 输入框 -->
              <li v-else>
                <input v-model="newSection" @keyup.enter="handleAddSection" @blur="cancelAdd" placeholder="输入新分区名称"
                  class="w-full px-2 py-1 text-base border rounded focus:outline-none focus:ring-1 focus:ring-blue-400 bg-white text-gray-800"
                  autofocus />
              </li>
            </ul>
          </li>

          <!-- 日记 -->
          <li>
            <router-link to="/StudyRecord" class="menu-item" active-class="menu-item-active"
              @click="isMobile && handleClose()">
              日记
            </router-link>
          </li>

          <!-- 项目 -->
          <li>
            <router-link to="/MyProjects" class="menu-item" active-class="menu-item-active"
              @click="isMobile && handleClose()">
              我的项目
            </router-link>
          </li>

          <!-- 关于我 -->
          <li>
            <router-link to="/about" class="menu-item" active-class="menu-item-active"
              @click="isMobile && handleClose()">
              关于我
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted, watch, computed, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useSidebarStore } from '@/stores/sidebar'

const sidebarStore = useSidebarStore()
const collapsed = computed(() => sidebarStore.collapsed)
const router = useRouter()
const route = useRoute()
import { getPublicSections, getPrivateSections, addSection, updateSection, deleteSection } from '@/api/techSection'
import { useUserStore } from '@/stores/user'

// 接收父组件传递的属性和事件
const props = defineProps({
  visible: {
    type: Boolean,
    default: true
  },
  isMobile: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close'])

const userStore = useUserStore()
const open = ref('技术笔记')
const techNoteSections = ref([])
const showInput = ref(false)
const newSection = ref('')
const editingId = ref(null)
const editName = ref('')
const showActionMenu = ref(null) // 移动端操作菜单显示状态

const fetchSections = async () => {
  try {
    let res
    if (!userStore.isLoggedIn) {
      res = await getPublicSections()
      techNoteSections.value = res?.data || []
    } else {
      res = userStore.isPublic === 1 ? await getPublicSections() : await getPrivateSections()
      techNoteSections.value = res?.data?.list || res?.data || []
    }
  } catch (err) {
    console.error(err)
    alert('获取分区列表失败')
  }
}
watch(() => userStore.isLoggedIn, async () => { await fetchSections() }, { immediate: true })

const toggle = (sectionName) => { open.value = open.value === sectionName ? '' : sectionName }
const cancelAdd = () => { newSection.value = ''; showInput.value = false }
const startEdit = (id, name) => {
  // 检查登录状态 - 使用 localStorage 直接检查，避免依赖 userStore 的响应式更新问题
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.token) {
    alert('请先登录后再修改分区')
    return
  }
  editingId.value = id
  editName.value = name
}
const handleAddSection = async () => {
  // 检查登录状态 - 使用 localStorage 直接检查
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.token) {
    alert('请先登录后再添加分区')
    return
  }

  if (!newSection.value.trim()) {
    alert('分区名称不能为空')
    return
  }

  try {
    const res = await addSection({ name: newSection.value.trim() })
    if (res.status === 'success') {
      newSection.value = ''
      showInput.value = false
      await fetchSections() // 重新获取分区列表
      alert('分区添加成功')
    } else {
      alert(res.info || '添加分区失败')
    }
  } catch (error) {
    console.error('添加分区错误:', error)
    alert('添加分区失败，请重试')
  }
}

const handleUpdate = async (id) => {
  // 检查登录状态 - 使用 localStorage 直接检查
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.token) {
    alert('请先登录后再修改分区')
    return
  }

  if (!editName.value.trim()) {
    alert('分区名称不能为空')
    return
  }

  try {
    // 保存重命名前的分区名称
    const oldName = techNoteSections.value.find(section => section.id === id)?.name
    const newName = editName.value.trim()
    
    const res = await updateSection(id, { name: newName })
    if (res.status === 'success') {
      editingId.value = null
      editName.value = ''
      await fetchSections() // 重新获取分区列表
      alert('分区更新成功')

      // 检查当前路由是否在该分区内，如果是则更新路由
      if (route.params.section === oldName) {
        // 根据当前路由类型进行不同的路由更新
        if (route.name === 'TechNoteList') {
          // 分区列表页
          router.replace(`/techNotes/${newName}`)
        } else if (route.name === 'TechNoteDetail' || route.name === 'TechNoteEdit') {
          // 笔记详情页或编辑页，保持相同的笔记ID
          router.replace(`/techNotes/${newName}/${route.params.id}`)
        } else if (route.name === 'TechNoteAdd') {
          // 新建笔记页
          router.replace(`/techNotes/${newName}/new`)
        }
      }
    } else {
      alert(res.info || '更新分区失败')
    }
  } catch (error) {
    console.error('更新分区错误:', error)
    alert('更新分区失败，请重试')
  }
}

const confirmDelete = async (id, name) => {
  // 检查登录状态 - 使用 localStorage 直接检查
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.token) {
    alert('请先登录后再删除分区')
    return
  }

  if (!confirm(`确定要删除分区 "${name}" 吗？此操作不可恢复！`)) {
    return
  }

  try {
    const res = await deleteSection(id)
    if (res.status === 'success') {
      await fetchSections() // 重新获取分区列表
      alert('分区删除成功')

      // 检查当前路由是否在被删除的分区内，如果是则重定向到技术笔记首页
      if (route.params.section === name) {
        router.replace('/techNotes')
      }
    } else {
      alert(res.info || '删除分区失败')
    }
  } catch (error) {
    console.error('删除分区错误:', error)
    alert('删除分区失败，请重试')
  }
}

// 处理移动端关闭事件
const handleClose = () => {
  emit('close')
}

// 点击外部关闭操作菜单
const handleActionMenuClickOutside = (e) => {
  // 如果点击的是操作按钮本身，不关闭菜单
  if (e.target.closest('.action-menu-button')) {
    return
  }
  
  if (showActionMenu.value && !e.target.closest('.action-menu-container')) {
    showActionMenu.value = null
  }
}

onMounted(() => { 
  fetchSections()
  document.addEventListener('click', handleActionMenuClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleActionMenuClickOutside)
})
</script>

<style scoped>
.sidebar-container {
  background: var(--bg-primary);
  border-right: 1px solid var(--border-color);
  height: 100vh;
  /* 使用视口高度 */
  overflow-y: auto;
  overflow-x: hidden;
  /* 避免横向滚动 */
  padding: 1rem;
  box-shadow: 2px 0 8px var(--shadow-color);
  position: fixed;
  /* 固定定位，不随滚动 */
  top: var(--header-height);
  /* 从头部下方开始 */
  left: 0;
  z-index: 100;
  /* 确保在内容上方 */
  transition: all 0.3s ease;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  color: var(--text-primary);
}

/* 暗色模式下的侧边栏 */
:global(.dark) .sidebar-container {
  background: var(--bg-translucent-light);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

/* 移动端关闭按钮暗色模式适配 */
:global(.dark) .mobile-close-btn {
  background-color: var(--bg-translucent-medium);
}

:global(.dark) .close-icon {
  color: var(--text-primary);
}

.sidebar-expanded {
  flex: 0 0 var(--sidebar-width);
  min-width: 200px;
  max-width: var(--sidebar-width);
  width: var(--sidebar-width);
  transform: translateX(0);
  opacity: 1;
}

.sidebar-collapsed {
  flex: 0 0 0;
  min-width: 0;
  max-width: 0;
  width: 0;
  padding: 0;
  border-right: none;
  transform: translateX(-100%);
  opacity: 0;
  overflow: hidden;
}

/* 移动端关闭按钮 */
.mobile-close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: var(--bg-translucent-medium);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
}

.close-icon {
  font-size: 20px;
  color: var(--text-primary);
}

/* 一级菜单 */
.menu-item {
  display: block;
  padding: 0.6rem 0.8rem 0.6rem 2rem;
  border-radius: 8px;
  font-weight: 600;
  color: var(--text-primary);
  transition: all 0.3s;
  white-space: nowrap;
  /* 不换行 */
  overflow: hidden;
  text-overflow: ellipsis;
  position: relative;
}

.menu-item::before {
  content: "🌙";
  position: absolute;
  left: 0.6rem;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.9rem;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.dark .menu-item::before {
  content: "🌑";
  opacity: 0.8;
}

.menu-item:hover::before {
  opacity: 1;
}

.menu-item:hover {
  background-color: var(--bg-translucent-medium);
  color: var(--accent-color);
}

.menu-item-active {
  background-color: var(--accent-color);
  color: white !important;
}

.menu-item-active:hover {
  background-color: var(--accent-hover);
  color: white !important;
}

/* 二级菜单 */
.sub-menu-item {
  display: block;
  padding: 0.4rem 0.6rem 0.4rem 1.8rem;
  border-radius: 6px;
  font-size: 0.85rem;
  color: var(--text-primary);
  transition: all 0.2s;
  white-space: nowrap;
  /* 不换行 */
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  /* 确保文本不会溢出 */
  position: relative;
}

.sub-menu-item::before {
  content: "🌙";
  position: absolute;
  left: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.8rem;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.sub-menu-item:hover {
  background-color: var(--bg-translucent-light);
  color: var(--accent-color);
}

.sub-menu-item:hover::before {
  opacity: 1;
}

.dark .sub-menu-item::before {
  content: "🌑";
  opacity: 0.7;
}

/* 添加按钮 */
.sub-menu-add {
  font-size: 0.8rem;
  color: var(--accent-color);
  transition: color 0.2s;
  white-space: nowrap;
}

.sub-menu-add:hover {
  text-decoration: underline;
}

/* 小按钮（编辑/删除） */
.icon-btn {
  font-size: 0.8rem;
  opacity: 0;
  transition: opacity 0.2s;
  flex-shrink: 0;
}

.group:hover .icon-btn {
  opacity: 1;
}

/* 移动端样式适配 */
@media (max-width: 768px) {
  .sidebar-container {
    width: min(240px, 80vw);
    max-width: 80vw;
    position: fixed;
    top: var(--header-height);
    left: 0;
    height: calc(100vh - var(--header-height));
    z-index: 100;
  }

  /* 在移动端始终显示编辑/删除按钮 */
  .icon-btn {
    opacity: 1;
  }


}
</style>