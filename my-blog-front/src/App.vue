<template>
  <div class="app-container">
    <header class="app-header">
      <Navbar @toggle-sidebar="toggleSidebar" />
    </header>

    <div class="app-layout">
      <AppSidebar 
        :visible="sidebarVisible" 
        @close="sidebarVisible = false"
      />
      
      <main class="app-main" :class="{ 'full-width': !sidebarVisible, 'with-sidebar': sidebarVisible }">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import Navbar from '@/components/NavBar.vue'
import AppSidebar from '@/components/AppSidebar.vue'

const sidebarVisible = ref(true)

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value
}

const handleResize = () => {
  if (window.innerWidth < 1024) {
    sidebarVisible.value = false
  } else {
    sidebarVisible.value = true
  }
}

onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style>
@import '@/assets/styles/github.css';

:root {
  --sidebar-width: 276px;
  --header-height: 64px;
  --content-padding: 24px;
  --transition-duration: 0.3s;
}

html, body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.app-container {
  min-height: 100vh;
  flex-direction: column;
  background-color: var(--bg-primary);
  color: var(--text-primary);
}

.app-header {
  position: sticky;
  top: 0;
  height: var(--header-height);
  z-index: 100;
}

.app-layout {
  display: flex;
  flex: 1;
  min-height: calc(100vh - var(--header-height));
  position: relative;
}

.app-main {
  flex: 1;
  padding: var(--content-padding);
  transition: margin-left var(--transition-duration) ease, background-color 0.3s ease;
  overflow-y: auto;
  height: calc(100vh - var(--header-height));
  margin-left: 0; /* 默认无边距 */
  background-color: var(--bg-primary);
}

.app-main.with-sidebar {
  margin-left: var(--sidebar-width); /* 侧边栏可见时添加左边距 */
}

.app-main.full-width {
  margin-left: 0;
}

@media (max-width: 1024px) {
  .app-main {
    margin-left: 0;
    padding: 16px;
  }
}

@media (max-width: 768px) {
  .app-main {
    padding: 12px;
  }
}

@media (max-width: 480px) {
  .app-main {
    padding: 8px;
  }
}
</style>