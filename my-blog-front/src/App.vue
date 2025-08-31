<template>
  <div class="app-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <Navbar @toggleSidebar="toggleMobileSidebar" />
    </header>

    <!-- 主内容区 -->
    <main class="main-layout">
      <!-- 桌面侧边栏 -->
      <aside class="sidebar desktop-sidebar">
        <Sidebar />
      </aside>

      <!-- 移动端抽屉侧边栏 -->
      <transition name="slide">
        <aside v-if="mobileSidebarOpen" class="sidebar mobile-sidebar">
          <Sidebar @close="toggleMobileSidebar" />
        </aside>
      </transition>

      <!-- 内容区 -->
      <section class="content">
        <div class="content-container">
          <router-view />
        </div>
      </section>
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <Footer />
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar.vue'
import Footer from './components/Footer.vue'

const mobileSidebarOpen = ref(false)

const toggleMobileSidebar = () => {
  mobileSidebarOpen.value = !mobileSidebarOpen.value
}
</script>

<style scoped>
/* ================= 基础布局 ================= */
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  overflow-y: scroll; /* 始终显示滚动条，防止横跳 */
}

* {
  box-sizing: border-box;
}

.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100vw;
  overflow-x: hidden;
}

/* 顶部导航 */
.header {
  height: 64px;
  width: 100%;
  background-color: #001529;
  color: white;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 0 16px;
}

/* 主内容区 */
.main-layout {
  display: flex;
  flex: 1;
  min-height: calc(100vh - 128px);
  width: 100%;
}

/* ================= 桌面侧边栏 ================= */
.desktop-sidebar {
  flex: 0 0 220px; /* 固定宽度 */
  min-width: 220px;
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  height: 100%;
  overflow-y: auto;
  display: block;
}

/* ================= 移动端抽屉侧边栏 ================= */
.mobile-sidebar {
  position: fixed;
  top: 64px;
  left: 0;
  width: 220px;
  height: calc(100vh - 64px);
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  z-index: 999;
  overflow-y: auto;
}

/* 抽屉动画 */
.slide-enter-from {
  transform: translateX(-100%);
}
.slide-enter-to {
  transform: translateX(0);
}
.slide-enter-active {
  transition: transform 0.3s;
}
.slide-leave-from {
  transform: translateX(0);
}
.slide-leave-to {
  transform: translateX(-100%);
}
.slide-leave-active {
  transition: transform 0.3s;
}

/* 内容区域 */
.content {
  flex: 1;
  min-width: 0;
  padding: 24px;
  background-color: #ffffff;
  height: 100%;
  overflow-y: auto;
}

/* 底部 */
.footer {
  height: 64px;
  width: 100%;
  background-color: #f0f2f5;
  text-align: center;
  line-height: 64px;
  flex-shrink: 0;
}

/* ================= 响应式 ================= */
@media (max-width: 768px) {
  .desktop-sidebar {
    display: none; /* 手机端隐藏桌面侧边栏 */
  }
}
</style>
