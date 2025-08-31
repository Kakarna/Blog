import { createApp } from 'vue'
import Antd, { Button, message } from 'ant-design-vue'
import './style.css'
import App from './App.vue'
import router from './router/index'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { useUserStore } from '@/stores/user.js'

const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(router).use(pinia).use(Antd).use(Button)

const userStore = useUserStore()
userStore.loadFromStorage?.()  // 如果你实现了这个方法

app.mount('#app')

app.config.globalProperties.$message = message
