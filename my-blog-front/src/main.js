import { createApp } from 'vue'
import Antd, { message } from 'ant-design-vue'
import './style.css'
import './assets/styles/theme.css'
import './assets/styles/components.css'
import App from './App.vue'
import router from './router/index'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { useUserStore } from '@/stores/user.js'

const app = createApp(App)

// Pinia
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

// Router
app.use(router)

// Ant Design Vue
app.use(Antd)

// 全局 message
app.config.globalProperties.$message = message

// 用户状态
const userStore = useUserStore()
userStore.loadFromStorage?.()  // 可选方法

app.mount('#app')
