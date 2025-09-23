<template>
  <div :class="['p-8 space-y-8 min-h-screen bg-cover bg-fixed relative overflow-hidden', isDark ? 'dark-theme' : 'light-theme']">
    <!-- 明色主题背景元素 - 固定定位 -->
    <div v-if="!isDark" class="fixed inset-0 bg-gradient-to-br from-blue-100/20 via-cyan-100/15 to-teal-100/10 backdrop-blur-3xl z-0"></div>
    <!-- 阳光效果 - 增强且固定 -->
    <div v-if="!isDark" class="fixed top-20 right-20 w-80 h-80 bg-yellow-300/30 rounded-full blur-4xl animate-pulse z-0"></div>
    <div v-if="!isDark" class="fixed bottom-10 left-10 w-72 h-72 bg-orange-300/25 rounded-full blur-3xl animate-pulse-delayed z-0"></div>
    <!-- 水波纹效果 - 增强且固定 -->
    <div v-if="!isDark" class="fixed bottom-20 left-20 w-60 h-60 bg-blue-400/25 rounded-full blur-3xl animate-ripple z-0"></div>
    <div v-if="!isDark" class="fixed top-1/4 right-1/4 w-56 h-56 bg-cyan-400/22 rounded-full blur-2xl animate-ripple-delayed z-0"></div>
    <!-- 气泡效果 - 增强且固定 -->
    <div v-if="!isDark" class="fixed top-1/3 right-1/4 w-40 h-40 bg-cyan-300/20 rounded-full blur-2xl animate-bubble z-0"></div>
    <div v-if="!isDark" class="fixed bottom-1/4 left-1/3 w-36 h-36 bg-blue-300/20 rounded-full blur-xl animate-bubble-delayed z-0"></div>
    <!-- 新增海洋光点 - 固定 -->
    <div v-if="!isDark" class="fixed top-1/5 left-1/5 w-4 h-4 bg-blue-400/50 rounded-full blur-sm animate-float-slow z-0"></div>
    <div v-if="!isDark" class="fixed top-2/3 right-1/5 w-3.5 h-3.5 bg-cyan-400/45 rounded-full blur-sm animate-float-medium z-0"></div>
    <div v-if="!isDark" class="fixed bottom-1/3 left-2/3 w-3 h-3 bg-teal-400/40 rounded-full blur-sm animate-float-fast z-0"></div>
    <!-- 新增更多光点 - 更显眼 -->
    <div v-if="!isDark" class="fixed top-1/4 right-1/3 w-5 h-5 bg-sky-400/55 rounded-full blur-md animate-float-slow z-0"></div>
    <div v-if="!isDark" class="fixed bottom-1/4 left-1/4 w-4.5 h-4.5 bg-light-blue-400/50 rounded-full blur-sm animate-float-medium z-0"></div>
    <div v-if="!isDark" class="fixed top-3/4 left-1/5 w-4 h-4 bg-aqua-400/48 rounded-full blur-sm animate-float-fast z-0"></div>
    <!-- 新增大型气泡 -->
    <div v-if="!isDark" class="fixed top-1/6 right-1/6 w-48 h-48 bg-white/15 rounded-full blur-2xl animate-bubble-large z-0"></div>
    <div v-if="!isDark" class="fixed bottom-1/6 left-1/6 w-44 h-44 bg-white/12 rounded-full blur-xl animate-bubble-large-delayed z-0"></div>
    <!-- 水面反射光 - 固定 -->
    <div v-if="!isDark" class="fixed bottom-0 left-0 right-0 h-40 bg-gradient-to-t from-blue-300/15 to-transparent backdrop-blur-2xl z-0"></div>
    <!-- 新增阳光射线 -->
    <div v-if="!isDark" class="fixed inset-0 bg-[conic-gradient(from_0deg_at_50%_50%,_rgba(255,255,255,0.3)_0%,_transparent_20%,_transparent_80%,_rgba(255,255,255,0.3)_100%)] z-0"></div>
    
    <!-- 暗色主题背景元素 - 固定定位 -->
    <div v-if="isDark" class="fixed inset-0 bg-gradient-to-br from-blue-900/3 via-purple-900/2 to-indigo-900/1 backdrop-blur-4xl z-0"></div>
    <!-- 水波纹效果 - 固定 -->
    <div v-if="isDark" class="fixed bottom-16 right-24 w-60 h-60 bg-blue-400/5 rounded-full blur-3xl animate-ripple-smooth z-0"></div>
    <!-- 霓虹灯网格背景 - 增强且固定 -->
    <div v-if="isDark" class="fixed inset-0 bg-[radial-gradient(ellipse_80%_80%_at_50%_50%,_rgba(120,119,198,0.4)_0%,_transparent_60%)] z-0"></div>
    <!-- 浮动光点粒子 - 增强且固定 -->
    <div v-if="isDark" class="fixed top-1/4 left-1/4 w-4 h-4 bg-purple-400/50 rounded-full blur-md animate-float-slow z-0"></div>
    <div v-if="isDark" class="fixed top-1/3 right-1/3 w-3 h-3 bg-blue-400/40 rounded-full blur-sm animate-float-medium z-0"></div>
    <div v-if="isDark" class="fixed bottom-1/4 left-1/3 w-3.5 h-3.5 bg-cyan-400/45 rounded-full blur-sm animate-float-fast z-0"></div>
    <!-- 新增光点 - 固定 -->
    <div v-if="isDark" class="fixed top-2/3 left-1/5 w-2.5 h-2.5 bg-pink-400/35 rounded-full blur-sm animate-float-slow z-0"></div>
    <div v-if="isDark" class="fixed bottom-1/3 right-1/4 w-3 h-3 bg-indigo-400/40 rounded-full blur-sm animate-float-medium z-0"></div>
    <!-- 彩色光晕 - 大幅增强且固定 -->
    <div v-if="isDark" class="fixed top-0 right-0 w-[32rem] h-[32rem] bg-purple-500/8 rounded-full blur-4xl z-0"></div>
    <div v-if="isDark" class="fixed bottom-0 left-0 w-[28rem] h-[28rem] bg-blue-500/6 rounded-full blur-4xl z-0"></div>
    <!-- 新增大型光晕 - 固定 -->
    <div v-if="isDark" class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[40rem] h-[40rem] bg-cyan-500/4 rounded-full blur-4xl z-0"></div>
    <div v-if="isDark" class="fixed top-1/4 right-1/4 w-[36rem] h-[36rem] bg-indigo-500/5 rounded-full blur-4xl z-0"></div>
    <!-- 霓虹灯射线效果 - 固定 -->
    <div v-if="isDark" class="fixed inset-0 bg-[conic-gradient(from_90deg_at_50%_50%,_rgba(255,255,255,0.1)_0%,_transparent_20%,_transparent_80%,_rgba(255,255,255,0.1)_100%)] z-0"></div>
    
    <div class="relative z-10">
      <!-- 页面标题 -->
      <h1 class="text-3xl font-bold mb-6 text-white p3-title-glow">个人设置</h1>

      <div class="flex flex-col lg:flex-row gap-8 lg:gap-12 items-start">
        <!-- 左侧个人信息卡片 - 极致透明 -->
      <div class="flex-shrink-0 w-full lg:w-72 bg-gray-800/60 backdrop-blur-xl rounded-xl border border-blue-400/15 p-6 space-y-6 shadow-lg shadow-blue-500/3">
        <!-- 头像 -->
        <div class="flex justify-center">
          <AvatarUpload 
            v-model="user.avatar"
            :default-avatar="defaultAvatar"
            :is-dark="isDark"
            @success="handleAvatarSuccess"
            class="w-28 h-28"
          />
        </div>

        <!-- 用户基础信息 -->
        <div class="text-center space-y-2">
          <p class="font-semibold text-lg text-white">{{ user.nickname || user.username }}</p>
          <p class="text-blue-300 text-sm">@{{ user.username }}</p>
          <p class="text-gray-400 text-xs">加入于 {{ formatDate(user.createTime) }}</p>
        </div>

        <!-- 当前信息展示 -->
        <div class="pt-3 border-t border-blue-400/30 text-sm space-y-3 w-full">
          <div>
            <p class="text-blue-300 text-xs">当前邮箱</p>
            <p class="text-white break-all">{{ user.email }}</p>
          </div>
          <div>
            <p class="text-blue-300 text-xs">个性签名</p>
            <p class="text-gray-300 line-clamp-3">{{ user.signature || '未填写签名' }}</p>
          </div>
        </div>
      </div>

      <!-- 右侧表单 - 极致透明 -->
      <div class="flex-1 flex flex-col gap-8">
        <!-- 基本信息 -->
        <div class="bg-gray-800/60 backdrop-blur-xl rounded-xl border border-blue-400/15 p-6 space-y-4 shadow-lg shadow-blue-500/3">
          <h2 class="text-xl font-semibold text-blue-300 mb-2 p3-subtitle">基本信息</h2>
          <input v-model="form.nickname" @input="user.nickname = form.nickname" type="text" placeholder="昵称"
            class="input" />
          <textarea v-model="form.signature" @input="user.signature = form.signature" rows="3" placeholder="个性签名"
            class="input"></textarea>
          <div class="flex justify-end">
            <button @click="saveProfile" class="btn">保存基本信息</button>
          </div>
        </div>

        <!-- 修改密码 - 极致透明 -->
        <div class="bg-gray-800/55 backdrop-blur-xl rounded-xl border border-blue-400/12 p-6 space-y-4 shadow-lg shadow-blue-500/2">
          <h2 class="text-xl font-semibold text-blue-300 mb-2 p3-subtitle">修改密码</h2>
          <input v-model="form.oldPassword" type="password" placeholder="当前密码" class="input" />
          <input v-model="form.newPassword" type="password" placeholder="新密码" class="input" />
          <input v-model="form.confirmPassword" type="password" placeholder="确认新密码" class="input" />
          <div class="flex justify-end">
            <button @click="savePassword" class="btn">修改密码</button>
          </div>
        </div>

        <!-- 修改邮箱 - 极致透明 -->
        <div class="bg-gray-800/55 backdrop-blur-xl rounded-xl border border-blue-400/12 p-6 shadow-lg shadow-blue-500/2">
          <h2 class="text-xl font-semibold text-blue-300 mb-2 p3-subtitle">修改邮箱</h2>
          <p class="text-blue-300 text-sm">当前邮箱：{{ user.email }}</p>
          <div class="flex justify-end mt-4">
            <button @click="openModal" class="btn">修改邮箱</button>
          </div>
        </div>

        <!-- 主题设置 - 极致透明 -->
        <div class="bg-gray-800/55 backdrop-blur-xl rounded-xl border border-blue-400/12 p-6 shadow-lg shadow-blue-500/2">
          <h2 class="text-xl font-semibold text-blue-300 mb-4 p3-subtitle">主题设置</h2>
          <div class="flex items-center justify-between">
            <span class="text-white">暗色模式</span>
            <button 
              @click="toggleTheme"
              class="relative inline-flex h-6 w-11 items-center rounded-full transition-colors"
              :class="isDark ? 'bg-blue-600' : 'bg-gray-400'"
            >
              <span class="sr-only">切换主题</span>
              <span
                class="inline-block h-4 w-4 transform rounded-full bg-white transition-transform"
                :class="isDark ? 'translate-x-6' : 'translate-x-1'"
              />
            </button>
          </div>
          <p class="text-blue-300 text-sm mt-2">
            {{ isDark ? '当前使用暗色主题（霓虹灯风格）' : '当前使用明色主题（水花、气泡、海浪阳光风格）' }}
          </p>
        </div>
      </div>
      </div>
    </div>
  </div>

  <!-- 邮箱修改弹窗 - 极致透明 -->
    <div v-if="showModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-[8000]">
      <div :class="['backdrop-blur-xl rounded-xl border w-96 p-6 relative z-[8100] shadow-2xl', isDark ? 'bg-gray-800/90 border-blue-400/30 shadow-blue-500/15' : 'bg-white/95 border-blue-300/40 shadow-blue-400/20']">
        <h3 :class="['text-lg font-semibold mb-4', isDark ? 'text-blue-300' : 'text-blue-600']">修改邮箱</h3>
        
        <!-- 当前邮箱验证 -->
        <div class="mb-4">
          <p :class="['text-sm mb-2', isDark ? 'text-blue-300' : 'text-blue-600']">当前邮箱：{{ user.email }}</p>
          <div class="flex gap-2">
            <input 
              v-model="form.oldEmailCode" 
              type="text" 
              placeholder="当前邮箱验证码" 
              :class="['flex-1 rounded border px-3 py-2 outline-none transition-colors', isDark ? 'bg-gray-700/80 border-gray-600 text-white placeholder-gray-400 focus:border-blue-400' : 'bg-white border-gray-300 text-gray-900 placeholder-gray-500 focus:border-blue-500']"
            >
            <button 
              @click="sendCode('old')" 
              :disabled="countdownOld > 0"
              :class="['rounded px-3 py-2 text-sm whitespace-nowrap transition-colors', isDark ? 'bg-blue-600 text-white hover:bg-blue-700 disabled:bg-blue-400' : 'bg-blue-500 text-white hover:bg-blue-600 disabled:bg-blue-300']"
            >
              {{ countdownOld > 0 ? `${countdownOld}s` : '发送验证码' }}
            </button>
          </div>
        </div>

        <!-- 新邮箱设置 -->
        <div class="mb-4">
          <input 
            v-model="form.newEmail" 
            type="email" 
            placeholder="新邮箱地址" 
            :class="['w-full rounded border px-3 py-2 mb-2 outline-none transition-colors', isDark ? 'bg-gray-700/80 border-gray-600 text-white placeholder-gray-400 focus:border-blue-400' : 'bg-white border-gray-300 text-gray-900 placeholder-gray-500 focus:border-blue-500']"
          >
          <div class="flex gap-2">
            <input 
              v-model="form.newEmailCode" 
              type="text" 
              placeholder="新邮箱验证码" 
              :class="['flex-1 rounded border px-3 py-2 outline-none transition-colors', isDark ? 'bg-gray-700/80 border-gray-600 text-white placeholder-gray-400 focus:border-blue-400' : 'bg-white border-gray-300 text-gray-900 placeholder-gray-500 focus:border-blue-500']"
            >
            <button 
              @click="sendCode('new')" 
              :disabled="countdownNew > 0"
              :class="['rounded px-3 py-2 text-sm whitespace-nowrap transition-colors', isDark ? 'bg-blue-600 text-white hover:bg-blue-700 disabled:bg-blue-400' : 'bg-blue-500 text-white hover:bg-blue-600 disabled:bg-blue-300']"
            >
              {{ countdownNew > 0 ? `${countdownNew}s` : '发送验证码' }}
            </button>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex justify-end gap-3">
          <button @click="closeModal" :class="['rounded px-4 py-2 transition-colors', isDark ? 'bg-gray-600 text-white hover:bg-gray-500' : 'bg-gray-200 text-gray-700 hover:bg-gray-300']">取消</button>
          <button @click="confirmEmail" :class="['rounded px-4 py-2 text-white transition-colors', isDark ? 'bg-blue-600 hover:bg-blue-700' : 'bg-blue-500 hover:bg-blue-600']">确认修改</button>
        </div>
      </div>
    </div>


</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { getUserById, updateUserById, sendUpdateEmailCode } from '@/api/userApi'
import { useUserStore } from '@/stores/user'

const user = ref({})

// 主题状态（与NavBar保持一致）
const isDark = ref(false)

// 初始化主题
const initTheme = () => {
  const savedMode = localStorage.getItem('darkMode')
  
  if (savedMode !== null) {
    isDark.value = savedMode === 'true'
  } else {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    isDark.value = prefersDark
  }
  
  document.documentElement.classList.toggle('dark', isDark.value)
}

// 切换主题
const toggleTheme = () => {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('darkMode', isDark.value)
  
  // 更新系统状态栏颜色
  const metaThemeColor = document.querySelector('meta[name="theme-color"]')
  if (metaThemeColor) {
    metaThemeColor.setAttribute('content', isDark.value ? '#2c3e50' : '#ffffff')
  }
  
  // 触发自定义事件通知其他组件主题变化
  window.dispatchEvent(new CustomEvent('themeChanged', {
    detail: { isDark: isDark.value }
  }))
}

const defaultAvatar = 'https://cdn.example.com/default-avatar.png'

const form = ref({
  nickname: '',
  signature: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  oldEmailCode: '',
  newEmail: '',
  newEmailCode: ''
})

const countdownOld = ref(0)
const countdownNew = ref(0)
const showModal = ref(false)
const isLoading = ref(false)

const userStore = useUserStore()
const userId = userStore.user?.id

// 保留handleAvatarSuccess用于处理上传成功后的逻辑

// 由于AvatarUpload组件现在直接处理上传，这个方法只需处理上传成功后的状态更新
const handleAvatarSuccess = async (avatar) => {
  try {
    // 不再显示Toast，因为组件中已经显示了
    console.log('头像上传成功，更新用户状态')
    
    // 重新加载用户信息
    await loadUser()
    
    // 强制更新userStore状态
    userStore.setUser({...userStore.user, avatar}, userStore.token)
  } catch (error) {
    console.error('更新用户状态错误:', error)
    // 这里不再显示Toast
  }
}

// 加载用户信息
const loadUser = async () => {
  const res = await getUserById(userId)
  if (res.status === 'success') {
    user.value = res.data
    form.value.nickname = res.data.nickname
    form.value.signature = res.data.signature
  }
}

// 保存基本信息
const saveProfile = async () => {
  try {
    isLoading.value = true
    showToast('正在保存信息...', 'info')
    
    console.log('准备更新用户信息，用户ID:', userId)
    console.log('更新数据:', { nickname: form.value.nickname, signature: form.value.signature })
    
    const res = await updateUserById({ 
      id: userId, 
      nickname: form.value.nickname,
      signature: form.value.signature,
      updateType: 'profile' // 明确指定更新类型
    })
    
    console.log('接口响应:', res)
    
    if (res.status === 'success') {
      await loadUser()
      // 更新userStore状态
      userStore.setUser({
        ...userStore.user, 
        nickname: form.value.nickname,
        signature: form.value.signature
      }, userStore.token)
      showToast('信息更新成功')
    } else {
      showToast(res.info || '信息更新失败', 'error')
    }
  } catch (error) {
    console.error('信息更新错误:', error)
    showToast(`信息更新失败: ${error.message}`, 'error')
  } finally {
    isLoading.value = false
  }
}

// 保存密码
const savePassword = async () => {
  try {
    // 表单验证
    if (!form.value.oldPassword) {
      return showToast('请输入当前密码', 'error')
    }
    if (!form.value.newPassword) {
      return showToast('请输入新密码', 'error')
    }
    if (form.value.newPassword !== form.value.confirmPassword) {
      return showToast('两次输入的新密码不一致', 'error')
    }
    if (form.value.newPassword.length < 6) {
      return showToast('新密码长度不能少于6位', 'error')
    }

    isLoading.value = true
    showToast('正在更新密码...', 'info')
    
    const res = await updateUserById({
      id: userId,
      oldPassword: form.value.oldPassword,
      newPassword: form.value.newPassword,
      updateType: 'password'
    })
    
    if (res.status === 'success') {
      // 检查是否需要重新登录
      if (res.info && res.info.includes('请重新登录')) {
        showToast('密码修改成功，请重新登录', 'success')
        // 清空用户状态，跳转到登录页
        userStore.clearUser()
        // 延迟跳转，让用户看到提示
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      } else {
        showToast('密码修改成功')
      }
      // 清空表单
      form.value.oldPassword = ''
      form.value.newPassword = ''
      form.value.confirmPassword = ''
    } else {
      showToast(res.info || '密码修改失败', 'error')
    }
  } catch (error) {
    console.error('密码修改错误:', error)
    showToast(`密码修改失败: ${error.message}`, 'error')
  } finally {
    isLoading.value = false
  }
}

// 打开修改邮箱弹窗
const openModal = () => {
  showModal.value = true
  form.value.oldEmailCode = ''
  form.value.newEmail = ''
  form.value.newEmailCode = ''
}

// 关闭修改邮箱弹窗
const closeModal = () => {
  showModal.value = false
}

// 发送验证码
const sendCode = async (type) => {
  try {
    if (type === 'old') {
      if (countdownOld.value > 0) return
      
      const res = await sendUpdateEmailCode({
        email: user.value.email,
        userId: userId
      })
      
      if (res.status === 'success') {
        showToast('验证码已发送至当前邮箱')
        countdownOld.value = 60
        const timer = setInterval(() => {
          countdownOld.value--
          if (countdownOld.value <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } else {
        showToast(res.info || '验证码发送失败', 'error')
      }
    } else if (type === 'new') {
      if (countdownNew.value > 0) return
      if (!form.value.newEmail) {
        return showToast('请输入新邮箱', 'error')
      }
      
      // 简单的邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(form.value.newEmail)) {
        return showToast('请输入有效的邮箱地址', 'error')
      }
      
      const res = await sendUpdateEmailCode({
        email: form.value.newEmail,
        userId: userId
      })
      
      if (res.status === 'success') {
        showToast('验证码已发送至新邮箱')
        countdownNew.value = 60
        const timer = setInterval(() => {
          countdownNew.value--
          if (countdownNew.value <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } else {
        showToast(res.info || '验证码发送失败', 'error')
      }
    }
  } catch (error) {
    console.error('发送验证码错误:', error)
    showToast(`验证码发送失败: ${error.message}`, 'error')
  }
}

// 确认修改邮箱
const confirmEmail = async () => {
  try {
    // 表单验证
    if (!form.value.oldEmailCode) {
      return showToast('请输入当前邮箱验证码', 'error')
    }
    if (!form.value.newEmail) {
      return showToast('请输入新邮箱', 'error')
    }
    if (!form.value.newEmailCode) {
      return showToast('请输入新邮箱验证码', 'error')
    }

    isLoading.value = true
    showToast('正在更新邮箱...', 'info')
    
    const res = await updateUserById({
      id: userId,
      oldEmailCode: form.value.oldEmailCode,
      newEmail: form.value.newEmail,
      newEmailCode: form.value.newEmailCode,
      updateType: 'email'
    })
    
    if (res.status === 'success') {
      await loadUser()
      // 更新userStore状态
      userStore.setUser({
        ...userStore.user, 
        email: form.value.newEmail
      }, userStore.token)
      showToast('邮箱修改成功')
      closeModal()
    } else {
      showToast(res.info || '邮箱修改失败', 'error')
    }
  } catch (error) {
    console.error('邮箱修改错误:', error)
    showToast(`邮箱修改失败: ${error.message}`, 'error')
  } finally {
    isLoading.value = false
  }
}

const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

// 显示通知
const showToast = (message, type = 'success') => {
  const toast = document.createElement('div')
  toast.className = `fixed top-4 right-4 px-4 py-2 rounded-md shadow-md text-white z-[8500] ${type === 'success' ? 'bg-green-500' : type === 'info' ? 'bg-blue-500' : 'bg-red-500'
    }`
  toast.textContent = message
  document.body.appendChild(toast)
  setTimeout(() => toast.remove(), 3000)
}

onMounted(() => {
  initTheme()
  loadUser()
  
  // 监听来自其他组件的主题变化事件
  window.addEventListener('themeChanged', (event) => {
    isDark.value = event.detail.isDark
    document.documentElement.classList.toggle('dark', isDark.value)
  })
})
</script>

<style scoped>
/* 明色主题样式 */
.light-theme {
  background: linear-gradient(135deg, #e0f2fe 0%, #bbdefb 50%, #90caf9 100%);
}

.light-theme .input {
  border: 1px solid rgba(96, 165, 250, 0.4);
  border-radius: 0.5rem;
  padding: 0.75rem;
  outline: none;
  transition: all 0.3s ease;
  width: 100%;
  background: rgba(255, 255, 255, 0.7);
  color: #1e293b;
  backdrop-filter: blur(12px);
}

.light-theme .input::placeholder {
  color: rgba(100, 116, 139, 0.7);
}

.light-theme .input:focus {
  border-color: rgba(59, 130, 246, 0.8);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
  background: rgba(255, 255, 255, 0.9);
}

.light-theme .input:hover {
  border-color: rgba(59, 130, 246, 0.6);
  background: rgba(255, 255, 255, 0.8);
}

/* 暗色主题样式 */
.dark-theme {
  background-color: rgba(17, 24, 39, 0.95);
}

.dark-theme .input {
  border: 1px solid rgba(75, 85, 99, 0.6);
  border-radius: 0.5rem;
  padding: 0.75rem;
  outline: none;
  transition: all 0.3s ease;
  width: 100%;
  background: rgba(31, 41, 55, 0.5);
  color: white;
  backdrop-filter: blur(12px);
}

.dark-theme .input::placeholder {
  color: rgba(156, 163, 175, 0.8);
}

.dark-theme .input:focus {
  border-color: rgba(96, 165, 250, 0.8);
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.2);
  background: rgba(31, 41, 55, 0.7);
}

.dark-theme .input:hover {
  border-color: rgba(96, 165, 250, 0.7);
  background: rgba(31, 41, 55, 0.6);
}

.p3-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  backdrop-filter: blur(8px);
}

/* 明色主题按钮 */
.light-theme .p3-btn-primary {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.9), rgba(99, 102, 241, 0.9));
  color: white;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
  border: 1px solid rgba(96, 165, 250, 0.5);
}

.light-theme .p3-btn-primary:hover {
  background: linear-gradient(135deg, rgba(37, 99, 235, 1), rgba(79, 70, 229, 1));
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.5);
  transform: translateY(-1px);
}

.light-theme .p3-btn-secondary {
  background: rgba(100, 116, 139, 0.6);
  color: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(148, 163, 184, 0.5);
}

.light-theme .p3-btn-secondary:hover {
  background: rgba(100, 116, 139, 0.8);
  color: white;
}

/* 暗色主题按钮 */
.dark-theme .p3-btn-primary {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.8), rgba(99, 102, 241, 0.8));
  color: white;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
  border: 1px solid rgba(96, 165, 250, 0.4);
}

.dark-theme .p3-btn-primary:hover {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.9), rgba(79, 70, 229, 0.9));
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
  transform: translateY(-1px);
}

.dark-theme .p3-btn-secondary {
  background: rgba(107, 114, 128, 0.5);
  color: rgba(229, 231, 235, 0.9);
  border: 1px solid rgba(156, 163, 175, 0.4);
}

.dark-theme .p3-btn-secondary:hover {
  background: rgba(107, 114, 128, 0.7);
  color: white;
}

.p3-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 明色主题标题 */
.light-theme .p3-title-glow {
  text-shadow: 0 0 20px rgba(59, 130, 246, 0.6), 0 0 40px rgba(59, 130, 246, 0.3);
  font-family: 'Arial Rounded MT Bold', 'Segoe UI', sans-serif;
  color: #1e40af;
}

.light-theme .p3-subtitle {
  text-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #3b82f6;
}

/* 暗色主题标题 */
.dark-theme .p3-title-glow {
  text-shadow: 0 0 20px rgba(96, 165, 250, 0.6), 0 0 40px rgba(96, 165, 250, 0.3);
  font-family: 'Arial Rounded MT Bold', 'Segoe UI', sans-serif;
  color: white;
}

.dark-theme .p3-subtitle {
  text-shadow: 0 0 10px rgba(96, 165, 250, 0.5);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #93c5fd;
}

/* 明色主题卡片样式 */
.light-theme .bg-gray-800\/60 {
  background: rgba(255, 255, 255, 0.7) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #1e293b;
}

.light-theme .border-blue-400\/15 {
  border-color: rgba(59, 130, 246, 0.3) !important;
}

.light-theme .text-blue-300 {
  color: #1d4ed8 !important;
}

.light-theme .text-white {
  color: #1e293b !important;
}

.light-theme .text-gray-400 {
  color: #64748b !important;
}

.light-theme .border-blue-400\/30 {
  border-color: rgba(59, 130, 246, 0.4) !important;
}

/* 新增动画效果 */
@keyframes bubble {
  0% {
    transform: translateY(0) scale(1);
    opacity: 0.3;
  }
  50% {
    transform: translateY(-20px) scale(1.1);
    opacity: 0.6;
  }
  100% {
    transform: translateY(-40px) scale(1.2);
    opacity: 0;
  }
}

@keyframes bubble-delayed {
  0% {
    transform: translateY(0) scale(1);
    opacity: 0.2;
  }
  33% {
    transform: translateY(-15px) scale(1.05);
    opacity: 0.4;
  }
  66% {
    transform: translateY(-30px) scale(1.1);
    opacity: 0.6;
  }
  100% {
    transform: translateY(-45px) scale(1.15);
    opacity: 0;
  }
}

.animate-bubble {
  animation: bubble 3s ease-in-out infinite;
}

.animate-bubble-delayed {
  animation: bubble-delayed 4s ease-in-out infinite;
  animation-delay: 1s;
}

.animate-ripple {
  animation: ripple-slow 4s ease-in-out infinite;
}

.animate-pulse {
  animation: pulse-slow 3s ease-in-out infinite;
}

/* 旋转动画 */
@keyframes rotate-slow {
  0% {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

.animate-rotate-slow {
  animation: rotate-slow 20s linear infinite;
}

/* 浮动动画 */
@keyframes float-slow {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  33% {
    transform: translateY(-20px) translateX(10px);
  }
  66% {
    transform: translateY(10px) translateX(-15px);
  }
}

@keyframes float-medium {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  50% {
    transform: translateY(15px) translateX(-10px);
  }
}

@keyframes float-fast {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-15px) translateX(8px);
  }
  75% {
    transform: translateY(8px) translateX(-12px);
  }
}

.animate-float-slow {
  animation: float-slow 6s ease-in-out infinite;
}

.animate-float-medium {
  animation: float-medium 4s ease-in-out infinite;
  animation-delay: 1s;
}

.animate-float-fast {
  animation: float-fast 5s ease-in-out infinite;
  animation-delay: 2s;
}


</style>
