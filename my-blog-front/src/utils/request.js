import axios from 'axios'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import router from '@/router'
const request = axios.create({
  baseURL: '/api',
  timeout: 60000 // 设置为 60 秒，避免大文件超时
})


// 请求拦截器：自动带上 JWT
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)


// 响应拦截器：统一处理 401 / 403
request.interceptors.response.use(
  response => response.data,
  error => {
    const res = error.response?.data

    if (res?.code === 401) {
      alert(res.info || '未登录，请重新登录')
      // 清理用户状态
      localStorage.removeItem('token')
      return Promise.reject({ handledByInterceptor: true })
    } else if (res?.code === 403) {
      alert(res.info || '权限不足')
      return Promise.reject({ handledByInterceptor: true })
    } else {
      alert(res?.info || '请求失败，请稍后重试')
      return Promise.reject(error)
    }
  }
)



export default request
