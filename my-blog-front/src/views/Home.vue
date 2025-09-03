<template>
  <div class="max-w-6xl mx-auto p-6 space-y-12">


    <!-- 统计指标卡片 -->
    <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div class="p-4 bg-white shadow rounded-lg text-center">
        <p class="text-gray-500">笔记总数</p>
        <p class="text-2xl font-bold">{{ stats.articleCount }}</p>
      </div>
      <div class="p-4 bg-white shadow rounded-lg text-center">
        <p class="text-gray-500">项目总数</p>
        <p class="text-2xl font-bold">{{ stats.projectCount }}</p>
      </div>
      <div class="p-4 bg-white shadow rounded-lg text-center">
        <p class="text-gray-500">最近更新</p>
        <p class="text-2xl font-bold">{{ stats.recentUpdates }}</p>
      </div>
      <div class="p-4 bg-white shadow rounded-lg text-center">
        <p class="text-gray-500">访问量</p>
        <p class="text-2xl font-bold">{{ stats.visits }}</p>
      </div>
    </section>

    <!-- 图表区域 -->
    <section class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="p-4 bg-white shadow rounded-lg">
        <h2 class="text-lg font-semibold mb-2">文章分类比例</h2>
        <!-- 这里可以放 Chart.js 或 ECharts 饼图 -->
        <div ref="pieChart" class="h-48 flex items-center justify-center text-gray-400">[饼图占位]</div>
      </div>
      <div class="p-4 bg-white shadow rounded-lg">
        <h2 class="text-lg font-semibold mb-2">文章发布趋势</h2>
        <!-- 折线图占位 -->
        <div ref="lineChart" class="h-48 flex items-center justify-center text-gray-400">[折线图占位]</div>
      </div>
    </section>

    <!-- 最近更新列表 -->
<section>
  <h2 class="text-2xl font-semibold mb-4">📝 最近更新</h2>
  <ul class="space-y-4">
    <li v-for="(item, idx) in recentActivities" :key="idx">
      <div
        class="p-4 bg-white shadow rounded-lg flex flex-col md:flex-row justify-between items-start md:items-center">
        <div>
          <!-- 更新时间 -->
          <p class="text-gray-500 text-sm">{{ item.updatedTime }}</p>
          <!-- 标题 + 链接跳转 -->
          <p class="text-gray-800 font-medium">
            <a :href="`/techNotes/${item.sectionId}/${item.id}`" class="text-blue-600 hover:underline">
              {{ item.title }}
            </a>
          </p>
        </div>
      </div>
    </li>
  </ul>
</section>



  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { fetchStats, fetchRecentActivities, fetchNoteCategoryStats, fetchNoteTrend } from '@/api/dashboard'

const stats = ref({})
const recentActivities = ref([])

// 饼图容器
const pieChart = ref(null)
const lineChart = ref(null)

onMounted(async () => {
  // 核心统计数据
  const statsRes = await fetchStats()
  stats.value = statsRes.data

  // 最近更新
  const recentRes = await fetchRecentActivities()
  recentActivities.value = recentRes.data

  // 文章分类饼图
  const categoryRes = await fetchNoteCategoryStats()
  if (pieChart.value) {
    const pie = echarts.init(pieChart.value)
    pie.setOption({
      tooltip: { trigger: 'item' },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: categoryRes.data.map(item => ({ name: item.name, value: item.value }))
        }
      ]
    })
  }

  // 发布趋势折线图
  const trendRes = await fetchNoteTrend()
  if (lineChart.value) {
    const line = echarts.init(lineChart.value)
    line.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: trendRes.data.map(item => item.date)
      },
      yAxis: { type: 'value' },
      series: [
        {
          data: trendRes.data.map(item => item.count),
          type: 'line',
          smooth: true
        }
      ]
    })
  }
})

</script>
