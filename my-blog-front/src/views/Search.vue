<template>
  <div class="max-w-5xl mx-auto p-6">
    <h2 class="text-2xl font-bold mb-4">搜索结果：{{ keyword }}</h2>

    <section v-if="results.length">
      <ul class="space-y-2">
        <li v-for="item in results" :key="item.id">
          <router-link
            :to="`/techNotes/${item.sectionId}/${item.id}`"
            class="text-blue-600 hover:underline"
          >
            {{ item.title }}
          </router-link>
          <span class="ml-2 text-gray-500">{{ item.updatedTime }}</span>
        </li>
      </ul>
    </section>

    <p v-else class="text-gray-400">没有找到相关内容。</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchSearchResults } from '@/api/dashboard' // 后端接口

const route = useRoute()
const keyword = ref(route.query.q || '')
const results = ref([])

onMounted(async () => {
  if (keyword.value) {
    const res = await fetchSearchResults({ keyword: keyword.value })
    results.value = res.data
  }
})
</script>
