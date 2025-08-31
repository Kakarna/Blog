<template>
  <div class="register-wrapper">
    <div class="form-box">
      <h2 class="title">注册新用户</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>账号</label>
          <input v-model="username" type="text" placeholder="请输入账号" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="password" type="password" placeholder="请输入密码" required />
        </div>
        <div class="form-group">
          <label>确认密码</label>
          <input v-model="confirmPassword" type="password" placeholder="请再次输入密码" required />
        </div>
        <div class="form-group">
          <label>昵称</label>
          <input v-model="nickname" type="text" placeholder="请输入昵称" required />
        </div>
        <button class="submit-btn" type="submit" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { registerUser } from '@/api/userApi'
import router from '@/router'

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')
const loading = ref(false)
const errorMsg = ref('')

async function handleRegister() {
  errorMsg.value = ''

  if (password.value !== confirmPassword.value) {
    errorMsg.value = '两次密码输入不一致'
    return
  }

  loading.value = true
  try {
    const res = await registerUser({
      username: username.value,
      password: password.value,
      nickname: nickname.value
    })
    if (res.code === 200) {
      alert('注册成功，请登录！')
      router.push('/login')
    } else {
      errorMsg.value = res.message || '注册失败'
    }
  } catch (error) {
    errorMsg.value = '网络或服务器错误'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
}

.form-box {
  background: #fff;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

input {
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  outline: none;
  transition: 0.3s;
}

input:focus {
  border-color: #1677ff;
  box-shadow: 0 0 0 2px rgba(22, 119, 255, 0.2);
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #1677ff;
  color: white;
  font-weight: bold;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition-property: background-color;
  transition-duration: 0.3s;
  transition-timing-function: ease;
  transition-delay: 0s;

}

.submit-btn:disabled {
  background: #a0c5ff;
  cursor: not-allowed;
}

.error-msg {
  margin-top: 16px;
  color: #f5222d;
  text-align: center;
}
</style>
