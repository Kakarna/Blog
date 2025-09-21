<template>
  <div class="min-h-screen p-4 md:p-6 theme-bg-primary">

    <!-- 统计指标卡片 -->
    <section class="w-full grid grid-cols-2 sm:grid-cols-3 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-4 md:gap-6 mb-8 auto-rows-fr grid-flow-row-dense">
      <div
        v-for="(value, key, idx) in stats"
        :key="key"
        class="p-4 md:p-5 theme-card text-center fade-in hover:theme-shadow-md transition-all flex flex-col justify-center w-full h-full"
        :class="{'theme-gradient': idx % 2 === 0}"
        ref="fadeElements"
      >
        <div class="flex-1 flex flex-col justify-center w-full">
          <p class="theme-text-secondary capitalize text-sm md:text-base mb-2">{{ key.replace(/([A-Z])/g, ' $1') }}</p>
          <p class="text-xl md:text-2xl font-bold theme-text">{{ animatedValues[key] }}</p>
        </div>
      </div>
    </section>

    <!-- 图表区域 -->
    <section class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full mb-8">
      <div class="p-6 theme-card fade-in hover:theme-shadow-lg transition-shadow" ref="fadeElements">
        <h2 class="text-lg font-semibold mb-4 theme-text flex items-center">
          <span class="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
          文章分类比例
        </h2>
        <div ref="pieChart" class="h-64 flex items-center justify-center theme-text-tertiary">[饼图占位]</div>
      </div>
      <div class="p-6 theme-card fade-in hover:theme-shadow-lg transition-shadow" ref="fadeElements">
        <h2 class="text-lg font-semibold mb-4 theme-text flex items-center">
          <span class="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
          文章发布趋势
        </h2>
        <div ref="lineChart" class="h-64 flex items-center justify-center theme-text-tertiary">[折线图占位]</div>
      </div>
    </section>

    <!-- 最近更新列表 -->
    <section class="mt-8">
      <h2 class="text-2xl font-semibold mb-4 title-fx theme-title">📝 最近更新</h2>
      <ul class="space-y-4">
        <li v-for="(item, idx) in recentActivities" :key="idx">
          <div
            class="p-4 theme-card flex flex-col md:flex-row justify-between items-start md:items-center fade-in hover:theme-shadow-md transition-shadow"
            :class="{'theme-gradient-horizontal': idx % 2 === 0}"
            ref="fadeElements"
          >
            <div class="w-full">
              <div class="flex items-center">
                <span class="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                <p class="theme-text-secondary text-sm">{{ item.updatedTime }}</p>
              </div>
              <p class="theme-text font-medium mt-2 flex items-start">
                <span class="inline-block w-4"></span>
                <a :href="`/techNotes/${item.sectionId}/${item.id}`" class="theme-link hover:underline text-left">
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
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { fetchStats, fetchRecentActivities, fetchNoteCategoryStats, fetchNoteTrend } from '@/api/dashboard'

const stats = ref({})
const recentActivities = ref([])
const animatedValues = ref({})

const pieChart = ref(null)
const lineChart = ref(null)
const fadeElements = ref([])

// 数字滚动动画
const animateValue = (key, target) => {
  const duration = 1000
  const frameRate = 30
  let current = 0
  const increment = target / (duration / frameRate)
  const interval = setInterval(() => {
    current += increment
    if (current >= target) {
      current = target
      clearInterval(interval)
    }
    animatedValues.value[key] = Math.floor(current)
  }, frameRate)
}

onMounted(async () => {
  const statsRes = await fetchStats()
  stats.value = statsRes.data

  Object.keys(statsRes.data).forEach(key => {
    animatedValues.value[key] = 0
    animateValue(key, statsRes.data[key])
  })

  const recentRes = await fetchRecentActivities()
  recentActivities.value = recentRes.data

  const categoryRes = await fetchNoteCategoryStats()
  if (pieChart.value) {
    const pie = echarts.init(pieChart.value)
    pie.setOption({
      tooltip: { trigger: 'item' },
      series: [
        { type: 'pie', radius: '50%', data: categoryRes.data.map(i => ({ name: i.name, value: i.value })) }
      ]
    })
  }

  const trendRes = await fetchNoteTrend()
  if (lineChart.value) {
    const line = echarts.init(lineChart.value)
    line.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: trendRes.data.map(i => i.date) },
      yAxis: { type: 'value' },
      series: [{ data: trendRes.data.map(i => i.count), type: 'line', smooth: true }]
    })
  }

  // 页面滚动到可视区域才触发动画
  nextTick(() => {
    const elements = document.querySelectorAll('.fade-in')
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('opacity-100', 'translate-y-0', 'scale-100')
          observer.unobserve(entry.target)
        }
      })
    }, { threshold: 0.2 })

    elements.forEach(el => observer.observe(el))
  })
})
</script>

<style>
.fade-in {
  opacity: 0;
  transform: translateY(1rem) scale(0.95);
  transition: opacity 0.5s ease-out, transform 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}
.fade-in.opacity-100 {
  opacity: 1;
  transform: translateY(0) scale(1);
}

/* 主题相关样式 */
.theme-gradient {
  background: linear-gradient(to bottom right, var(--bg-translucent-light), var(--card-bg));
}

.theme-gradient-horizontal {
  background: linear-gradient(to right, var(--bg-translucent-light), var(--card-bg));
}

/* 卡片样式增强 */
.hover\:theme-shadow-md:hover {
  box-shadow: 0 6px 12px var(--shadow-color);
}
.hover\:theme-shadow-lg:hover {
  box-shadow: 0 10px 15px var(--shadow-color);
}

/* 标题特效 */
.title-fx {
  opacity: 0;
  transform: scale(0.9);
  background: linear-gradient(90deg, var(--accent-color), var(--accent-hover), #06b6d4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: titleFadeIn 1s forwards, titleColor 2s ease-in-out infinite alternate;
}
@keyframes titleFadeIn {
  to { opacity: 1; transform: scale(1); }
}
@keyframes titleColor {
  0% { background-position: 0% 50%; }
  100% { background-position: 100% 50%; }
}

/* 圆点指示器动画 */
.w-2.h-2.bg-blue-500, .w-2.h-2.bg-green-500 {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 暗色模式下的图表适配 */
:global(.dark) .echarts {
  filter: brightness(0.9) contrast(1.1);
}
</style>