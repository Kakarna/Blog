<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 w-full max-w-md">
      <h2 class="text-2xl font-bold mb-6 text-center">登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">账号</label>
          <input v-model="form.username" type="text"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            placeholder="请输入账号" required />
        </div>
        <div class="mb-6">
          <label class="block text-gray-700 text-sm font-bold mb-2">密码</label>
          <input v-model="form.password" type="password"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            placeholder="请输入密码" required />
        </div>
        <div class="flex items-center justify-between">
          <button type="submit"
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            登录
          </button>
          <router-link to="/register"
            class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
            注册账号
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/api/userApi'
import { useUserStore } from '@/stores/user'
import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const userStore = useUserStore()
const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const res = await loginUser(form)
    if (res.code === 200) {
      const token = res.data
      // 保存 token
      localStorage.setItem('token', token)
      // 解析 token 获取用户信息
      const userInfo = jwtDecode(token)  // payload: { role, id, username, exp }
      userStore.setUser(
        {
          id: userInfo.id,
          username: userInfo.username,
          role: userInfo.role,
          nickname: userInfo.nickname || userInfo.username
        },
        token   // ✅ 第二个参数
      )

      router.push('/')
    } else {
      alert(res.info)
    }
  } catch (err) {
    console.error(err)
    alert('请求失败')
  }
}
</script>
