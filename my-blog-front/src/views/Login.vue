<template>
  <div class="login-wrapper">
    <!-- 背景图片 -->
    <div class="bg-image"></div>

    <!-- 动态漂浮光斑 -->
    <div v-for="(light, index) in lights" 
         :key="index" 
         class="bg-light"
         :style="light.style">
    </div>

    <div class="form-box fade-in">
      <h2 class="title title-fx mb-6">登录</h2>

      <form @submit.prevent="handleLogin" class="form space-y-5">
        <!-- 账号输入 -->
        <div class="form-group fade-in">
          <label>账号 / 邮箱</label>
          <input
            v-model="form.username"
            type="text"
            placeholder="请输入账号或邮箱"
          />
          <p v-if="errors.username" class="error-msg">{{ errors.username }}</p>
        </div>

        <!-- 密码输入 -->
        <div class="form-group fade-in">
          <label>密码</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
          />
          <p v-if="errors.password" class="error-msg">{{ errors.password }}</p>
        </div>

        <!-- 提交按钮 + 注册链接 -->
        <div class="flex justify-between items-center fade-in">
          <button
            type="submit"
            class="submit-btn"
            @mousedown="buttonActive = true"
            @mouseup="buttonActive = false"
            :class="{ 'btn-active': buttonActive }"
          >
            登录
          </button>
          <router-link
            to="/register"
            class="theme-link hover:underline font-medium"
          >
            注册账号
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/api/userApi'
import { useUserStore } from '@/stores/user'
import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const userStore = useUserStore()
const buttonActive = ref(false)

const form = reactive({
  username: '',
  password: ''
})
const errors = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  errors.username = ''
  errors.password = ''

  if (!form.username) {
    errors.username = '请输入账号或邮箱'
    return
  }
  if (!form.password) {
    errors.password = '请输入密码'
    return
  }

  try {
    const res = await loginUser(form)
    if (res.code === 200) {
      const token = res.data
      const userInfo = jwtDecode(token)
      userStore.setUser(
        {
          id: userInfo.id,
          username: userInfo.username,
          role: userInfo.role,
          isPublic: userInfo.isPublic,
          nickname: userInfo.nickname || userInfo.username
        },
        token
      )
      router.push('/')
    } else {
      if (res.info.includes('密码')) {
        errors.password = res.info
      } else {
        errors.username = res.info
      }
    }
  } catch (err) {
    console.error(err)
    errors.username = '服务器错误，请稍后再试'
  }
}

// 弹出淡入效果
onMounted(() => {
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

// 初始化随机光斑
const lights = ref([])
const generateLights = (num = 6) => {
  const arr = []
  for (let i = 0; i < num; i++) {
    const size = Math.random() * 300 + 200 // 光斑大小 200~500px
    const top = Math.random() * 80 + 10 + '%' // 随机top 10~90%
    const left = Math.random() * 80 + 10 + '%' // 随机left 10~90%
    const duration = Math.random() * 30 + 20 // 20~50s动画周期
    const color = `rgba(${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, 0.25)`
    arr.push({
      style: {
        width: size + 'px',
        height: size + 'px',
        top,
        left,
        background: `radial-gradient(circle, ${color} 0%, transparent 70%)`,
        animationDuration: duration + 's'
      }
    })
  }
  lights.value = arr
}
generateLights()
</script>

<style scoped>
.login-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  padding-top: 8vh;
  background: var(--bg-primary);
  overflow: hidden;
}

/* 背景图片 */
.bg-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('/images/login-illustration.svg') center/cover no-repeat;
  opacity: 0.15;
  z-index: 0;
  pointer-events: none;
}

/* 动态光斑 */
.bg-light {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  z-index: 0;
  pointer-events: none;
  animation: floatLight linear infinite alternate;
}

@keyframes floatLight {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(5%, -5%) rotate(45deg); }
  50% { transform: translate(-5%, 5%) rotate(90deg); }
  75% { transform: translate(5%, 5%) rotate(135deg); }
  100% { transform: translate(0, 0) rotate(360deg); }
}

/* 表单 */
.form-box {
  position: relative;
  background: var(--bg-translucent-light);
  backdrop-filter: blur(15px);
  padding: 50px 40px;
  width: 100%;
  max-width: 600px;
  border-radius: 16px;
  box-shadow: 0 12px 32px var(--shadow-color);
  transform: translateY(1rem) scale(0.95);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.68,-0.55,0.27,1.55);
  z-index: 10;
}

.title {
  font-size: 28px;
  text-align: center;
  font-weight: bold;
  color: var(--text-primary);
}

/* 标题渐变效果 */
.title-fx {
  background: linear-gradient(90deg, var(--accent-color), var(--accent-hover), #06b6d4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: titleColor 2s ease-in-out infinite alternate;
}
@keyframes titleColor {
  0% { background-position: 0% 50%; }
  100% { background-position: 100% 50%; }
}

.form-group {
  margin-bottom: 18px;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 6px;
  color: var(--text-primary);
  font-weight: 500;
}

input {
  padding: 12px 14px;
  font-size: 15px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  outline: none;
  transition: all 0.3s ease;
  background: var(--bg-translucent-light);
  color: var(--text-primary);
}

input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 2px var(--accent-color);
  background: var(--bg-translucent-medium);
}

.submit-btn {
  padding: 14px 22px;
  background: var(--accent-color);
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}
.submit-btn:hover {
  background: var(--accent-hover);
}
.btn-active {
  transform: scale(0.97);
}

/* 字段错误提示 */
.error-msg {
  margin-top: 6px;
  font-size: 13px;
  color: #f5222d;
}

/* 弹出淡入效果 */
.fade-in {
  opacity: 0;
  transform: translateY(1rem) scale(0.95);
  transition: all 0.6s cubic-bezier(0.68,-0.55,0.27,1.55);
}
.fade-in.opacity-100 {
  opacity: 1;
  transform: translateY(0) scale(1);
}
</style>
