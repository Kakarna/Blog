<template>
  <div class="space-y-4">
    <h1 class="text-3xl font-bold text-gray-900">欢迎来到我的博客</h1>
    <p class="text-gray-700">
      这是一个用 Vue 3 + Vite + Tailwind CSS 构建的个人博客网站。你可以在这里查看文章、项目，或了解我更多的信息。
    </p>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
      <div class="p-4 bg-white shadow rounded-xl">
        <h2 class="text-xl font-semibold">📝 最新文章</h2>
        <ul class="list-disc list-inside text-gray-600 mt-2">
          <li>如何从零开始搭建博客网站</li>
          <li>Vue 3 和 Tailwind 的最佳实践</li>
          <li>我的项目开发心得</li>
        </ul>
      </div>
      <div class="p-4 bg-white shadow rounded-xl">
        <h2 class="text-xl font-semibold">💻 项目展示</h2>
        <ul class="list-disc list-inside text-gray-600 mt-2">
          <li>代码生成器</li>
          <li>校园活动管理系统</li>
          <li>语音识别对话系统</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>
