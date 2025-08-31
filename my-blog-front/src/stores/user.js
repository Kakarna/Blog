import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,   // 用户信息
    token: null   // JWT token
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,             // 是否已登录
    nickname: (state) => state.user?.nickname || '',  // 用户昵称
  },
  actions: {
    // 登录成功，保存用户信息和 token
    setUser(user, token) {
      this.user = user
      this.token = token
      localStorage.setItem('token', token)
    },
    // 登出或清空
    clearUser() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user-store',
        storage: localStorage,
        paths: ['user', 'token']  // 持久化这两个字段
      }
    ]
  }
})
