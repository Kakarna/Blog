import { defineStore } from 'pinia'
import { loginUser, getUserById } from '@/api/userApi'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,   // 用户信息
    token: null   // JWT token
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,             // 是否已登录
    nickname: (state) => state.user?.nickname || '',  // 用户昵称
    role: (state) => state.user?.role || 'USER',      // 角色
    isPublic: (state) => state.user?.isPublic || 0    // 是否公共账号
  },
  actions: {
    setUser(user, token) {
      // 确保用户对象包含必要字段
      this.user = {
        id: user.id,
        username: user.username,
        nickname: user.nickname || user.username,
        avatar: user.avatar || 'https://ui-avatars.com/api/?name='+encodeURIComponent(user.nickname||user.username)+'&background=random',
        email: user.email,
        role: user.role || 'USER',
        ...user
      }
      this.token = token
      localStorage.setItem('token', token)
    },
    clearUser() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
    },
    
    async loginWithUserInfo(credentials) {
      try {
        const loginRes = await loginUser(credentials)
        if (loginRes.status === 'success' && loginRes.data) {
          const userRes = await getUserById(loginRes.data.userId || 0)
          if (userRes.status === 'success') {
            this.setUser(userRes.data, loginRes.data)
            return true
          }
        }
        return false
      } catch (error) {
        console.error('登录失败:', error)
        return false
      }
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user-store',
        storage: localStorage,
        paths: ['user', 'token'],
        beforeRestore: (ctx) => {
          console.log('正在恢复用户状态', ctx)
        },
        afterRestore: (ctx) => {
          console.log('用户状态恢复完成', ctx)
        }
      }
    ]
  }
})
