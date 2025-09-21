<template>
  <div class="about-container max-w-4xl mx-auto py-8 px-4">
    <h1 class="text-3xl font-bold mb-8">关于我和这个项目</h1>
    
    <div class="grid md:grid-cols-2 gap-8">
      <!-- 个人信息部分 -->
      <div class="bg-white/80 p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4">联系我</h2>
        <div class="space-y-4">
          <div>
            <span class="font-medium">邮箱：</span>
            <a href="mailto:2128576561lol@gamil.com" class="text-blue-600 hover:underline">2128576561lol@gamil.com</a>
          </div>
          <div>
            <span class="font-medium">QQ：</span>
            <span>2128576561</span>
          </div>
        </div>
      </div>

      <!-- 项目简介部分 -->
      <div class="bg-white/80 p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4">项目简介</h2>
        <div class="prose">
          <p>这是一个个人博客项目，主要功能包括：</p>
          <ul class="list-disc pl-6 mt-2">
            <li>文章发布与管理</li>
            <li>用户注册与登录</li>
            <li>个人资料设置</li>
            <li>响应式设计，适配各种设备</li>
          </ul>
          <p class="mt-4">技术栈：Vue 3 + Pinia + Tailwind CSS</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 可以在这里添加组件逻辑
</script>

<style scoped>
.about-container {
  min-height: calc(100vh - var(--header-height));
}
</style>