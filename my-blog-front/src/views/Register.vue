<template>
  <div class="register-wrapper">
    <div class="form-box fade-in">
      <h2 class="title title-fx">加入我们</h2>

      <!-- 分块卡片 -->
      <form @submit.prevent="handleRegister" class="form space-y-6">
        <!-- 邮箱 + 验证码卡片 -->
        <div class="card-block card-animate">
          <div class="card-header">
            <i class="icon-mail"></i> 邮箱验证
          </div>
          <div class="flex flex-col md:flex-row gap-3">
            <div class="flex-1 relative">
              <input
                v-model="email"
                type="email"
                placeholder="请输入邮箱"
                required
                class="floating-input"
              />
              <label>邮箱</label>
              <p v-if="errors.email" class="error-msg">{{ errors.email }}</p>
            </div>
            <div class="flex-1 relative">
              <input
                v-model="code"
                type="text"
                placeholder="验证码"
                required
                class="floating-input"
              />
              <label>验证码</label>
              <button
                type="button"
                class="code-btn"
                @click="handleSendCode"
                :disabled="sending || codeSent"
                :style="codeSent ? `background: linear-gradient(90deg, #4f46e5, #3b82f6 ${countdown}%, #06b6d4)` : ''"
              >
                {{ sending ? '发送中...' : codeSent ? countdown + '秒后重发' : '发送验证码' }}
              </button>
              <p v-if="errors.code" class="error-msg">{{ errors.code }}</p>
            </div>
          </div>
        </div>

        <!-- 账号 + 昵称卡片 -->
        <div class="card-block card-animate">
          <div class="card-header">
            <i class="icon-user"></i> 基本信息
          </div>
          <div class="flex flex-col md:flex-row gap-3">
            <div class="flex-1 relative">
              <input
                v-model="username"
                type="text"
                placeholder="请输入账号"
                required
                class="floating-input"
              />
              <label>账号</label>
              <p v-if="errors.username" class="error-msg">{{ errors.username }}</p>
            </div>
            <div class="flex-1 relative">
              <input
                v-model="nickname"
                type="text"
                placeholder="请输入昵称"
                required
                class="floating-input"
              />
              <label>昵称</label>
              <p v-if="errors.nickname" class="error-msg">{{ errors.nickname }}</p>
            </div>
          </div>
        </div>

        <!-- 密码卡片 -->
        <div class="card-block card-animate">
          <div class="card-header">
            <i class="icon-lock"></i> 设置密码
          </div>
          <div class="flex flex-col md:flex-row gap-3">
            <div class="flex-1 relative">
              <input
                v-model="password"
                type="password"
                placeholder="请输入密码"
                required
                class="floating-input"
              />
              <label>密码</label>
              <p v-if="errors.password" class="error-msg">{{ errors.password }}</p>
            </div>
            <div class="flex-1 relative">
              <input
                v-model="confirmPassword"
                type="password"
                placeholder="请再次输入密码"
                required
                class="floating-input"
              />
              <label>确认密码</label>
              <p v-if="errors.confirmPassword" class="error-msg">{{ errors.confirmPassword }}</p>
            </div>
          </div>
        </div>

        <!-- 提交按钮 -->
        <button
          class="submit-btn fade-in"
          type="submit"
          :disabled="loading"
          @mousedown="buttonActive = true"
          @mouseup="buttonActive = false"
          :class="{ 'btn-active': buttonActive }"
        >
          {{ loading ? '注册中...' : '立即注册' }}
        </button>

        <!-- 全局提示 -->
        <p v-if="successMsg" class="success-msg fade-in">{{ successMsg }}</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { registerUser, sendEmailCode } from '@/api/userApi'
import router from '@/router'

const email = ref('')
const code = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')

const loading = ref(false)
const sending = ref(false)
const codeSent = ref(false)
const countdown = ref(60)
const buttonActive = ref(false)
let timer = null

const errors = ref({
  email: '',
  code: '',
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})
const successMsg = ref('')

// 发送验证码
async function handleSendCode() {
  errors.value.email = ''
  if (!email.value) {
    errors.value.email = '请输入邮箱'
    return
  }

  sending.value = true
  try {
    const res = await sendEmailCode({ email: email.value })
    if (res.code === 200) {
      successMsg.value = '验证码已发送，请检查邮箱'
      codeSent.value = true
      countdown.value = 60
      timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
          codeSent.value = false
        }
      }, 1000)
    } else {
      errors.value.email = res.message || res.info || res.msg || '发送失败'
    }
  } catch {
    errors.value.email = '网络或服务器错误'
  } finally {
    sending.value = false
  }
}

// 注册
async function handleRegister() {
  Object.keys(errors.value).forEach(key => (errors.value[key] = ''))
  successMsg.value = ''

  if (!email.value) errors.value.email = '邮箱不能为空'
  if (!code.value) errors.value.code = '请输入验证码'
  if (!username.value) errors.value.username = '账号不能为空'
  if (!password.value) errors.value.password = '请输入密码'
  if (password.value !== confirmPassword.value)
    errors.value.confirmPassword = '两次密码输入不一致'
  if (!nickname.value) errors.value.nickname = '昵称不能为空'

  if (Object.values(errors.value).some(msg => msg)) return

  loading.value = true
  try {
    const res = await registerUser({
      email: email.value,
      code: code.value,
      username: username.value,
      password: password.value,
      nickname: nickname.value
    })

    if (res.code === 200) {
      successMsg.value = '注册成功，请登录！'
      setTimeout(() => router.push('/login'), 1500)
    } else {
      if (res.field && errors.value[res.field] !== undefined) {
        errors.value[res.field] = res.message || res.info || res.msg
      } else {
        errors.value.username = res.message || res.info || res.msg || '注册失败'
      }
    }
  } catch {
    errors.value.username = '网络或服务器错误'
  } finally {
    loading.value = false
  }
}

// 元素滚动动画
onMounted(() => {
  nextTick(() => {
    const elements = document.querySelectorAll('.fade-in, .card-animate')
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('opacity-100', 'translate-y-0', 'scale-100', 'rotate-0')
          observer.unobserve(entry.target)
        }
      })
    }, { threshold: 0.2 })
    elements.forEach(el => observer.observe(el))
  })
})
</script>

<style scoped>
.register-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: var(--bg-primary);
  padding: 20px;
}

.form-box {
  background: var(--card-bg);
  padding: 40px;
  width: 100%;
  max-width: 700px;
  border-radius: 24px;
  box-shadow: 0 16px 32px var(--shadow-color);
  transform: translateY(1rem) scale(0.95) rotate(-2deg);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}
.fade-in.opacity-100 {
  opacity: 1;
  transform: translateY(0) scale(1) rotate(0deg);
}

.title {
  font-size: 32px;
  margin-bottom: 28px;
  text-align: center;
  font-weight: bold;
  color: var(--text-primary);
}
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

/* 卡片块动画 */
.card-block {
  background: var(--panel-bg);
  border-radius: 16px;
  padding: 20px;
  border-left: 5px solid var(--accent-color);
  box-shadow: 0 4px 16px var(--shadow-color);
  transform: translateY(1rem) scale(0.95) rotate(-2deg);
  opacity: 0;
  transition: all 0.6s ease-out;
}
.card-animate.opacity-100 {
  opacity: 1;
  transform: translateY(0) scale(1) rotate(0deg);
}
.card-header {
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

/* 输入框浮动标签 */
.floating-input {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  outline: none;
  transition: all 0.3s;
  background: var(--bg-translucent-light);
  color: var(--text-primary);
}
.floating-input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 8px var(--accent-color);
}
.floating-input:focus + label,
.floating-input:not(:placeholder-shown) + label {
  transform: translateY(-28px);
  font-size: 13px;
  color: var(--accent-color);
  background: var(--panel-bg);
  padding: 0 4px;
  left: 8px;
  position: absolute;
  pointer-events: none;
}

label {
  position: absolute;
  left: 12px;
  top: 12px;
  color: var(--text-tertiary);
  font-size: 14px;
  transition: all 0.3s;
}

input::placeholder { color: transparent; }

/* 按钮 */
.submit-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(90deg, var(--accent-color), var(--accent-hover));
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
}
.submit-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, var(--accent-hover), #06b6d4);
  box-shadow: 0 6px 20px var(--accent-color);
}
.submit-btn:disabled {
  background: var(--bg-tertiary);
  cursor: not-allowed;
}
.btn-active { transform: scale(0.97); }

/* 验证码按钮 */
.code-btn {
  min-width: 120px;
  border-radius: 8px;
  font-size: 14px;
  background: var(--accent-color);
  color: #fff;
  transition: 0.3s;
}
.code-btn:disabled {
  background: var(--bg-tertiary);
  cursor: not-allowed;
}

/* 错误提示 */
.error-msg {
  margin-top: 4px;
  font-size: 13px;
  color: var(--error-color);
}
/* 全局成功提示 */
.success-msg {
  margin-top: 12px;
  font-size: 14px;
  color: var(--success-color);
  text-align: center;
  font-weight: 500;
}
</style>
