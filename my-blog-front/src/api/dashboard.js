import request from '@/utils/request'

// 核心统计数据
export function fetchStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

// 最近更新列表
export function fetchRecentActivities() {
  return request({
    url: '/dashboard/recentActivities',
    method: 'get'
  })
}

// 文章分类饼图
export function fetchNoteCategoryStats() {
  return request({
    url: '/dashboard/noteCategoryStats',
    method: 'get'
  })
}

// 文章发布趋势折线图
export function fetchNoteTrend() {
  return request({
    url: '/dashboard/noteTrend',
    method: 'get'
  })
}
