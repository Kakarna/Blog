<template>
  <div class="relative w-28 h-28">
    <transition name="avatar-fade" mode="out-in">
      <img 
        :src="modelValue || defaultAvatar" 
        :class="['w-full h-full rounded-full border-2 object-cover shadow-md', 
                isDark ? 'border-gray-700 shadow-blue-500/10' : 'border-white shadow-gray-300/50']" 
      />
    </transition>

    <!-- 上传按钮 - 使用渐变色与整体设计统一 -->
    <label 
      class="absolute bottom-0 right-0 -translate-x-1/4 translate-y-1/4 bg-gradient-to-r from-indigo-500 to-blue-500 hover:from-indigo-600 hover:to-blue-600 text-white w-8 h-8 rounded-full flex items-center justify-center cursor-pointer shadow-lg transition-all duration-300 hover:scale-110 hover:shadow-indigo-500/30"
    >
      <input type="file" class="hidden" @change="handleFileSelect" />
      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
        <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
      </svg>
    </label>
  </div>

  <!-- 裁剪弹窗 - 在Setting页面内显示，使用teleport -->
  <teleport to="body">
    <div v-if="showCropper" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-60 z-[9000]">
      <div :class="['backdrop-blur-lg rounded-xl border w-full max-w-3xl p-4 sm:p-6 relative z-[9100] shadow-xl mx-auto my-auto overflow-y-auto max-h-[90vh]', isDark ? 'bg-gray-800/95 border-blue-500/40 shadow-blue-500/20' : 'bg-white/98 border-blue-300/40 shadow-blue-400/20']">
        <h3 :class="['text-lg font-semibold mb-4', isDark ? 'text-blue-200' : 'text-blue-600']">裁剪头像</h3>
        
        <!-- 在移动设备上使用不同的布局 -->
        <div class="hidden md:flex md:flex-row gap-8">
          <!-- 裁剪区域 - 桌面版 -->
          <div class="flex-1">
            <vue-cropper
              ref="cropper"
              :src="cropperImage"
              :aspect-ratio="1"
              :view-mode="2"
              drag-mode="move"
              auto-crop-area="0.8"
              background="false"
              style="width: 100%; height: 450px;"
              @crop="updatePreview"
            />
          </div>
          
          <!-- 预览区域 - 桌面版 -->
          <div class="w-48 flex flex-col items-center">
            <h3 :class="['text-sm font-medium mb-4', isDark ? 'text-blue-200' : 'text-blue-600']">预览</h3>
            <div :class="['w-40 h-40 rounded-full overflow-hidden shadow-lg', isDark ? 'border-blue-500/30 shadow-blue-500/20' : 'border-blue-300/40 shadow-blue-400/10']" style="border-width: 2px;">
              <img :src="previewData || cropperImage" class="w-full h-full object-cover" />
            </div>
          </div>
        </div>
        
        <!-- 移动设备布局 - 增大裁剪区域 -->
        <div class="flex flex-col md:hidden gap-4">
          <!-- 裁剪区域 - 移动版 -->
          <div class="w-full">
            <vue-cropper
              ref="cropperMobile"
              :src="cropperImage"
              :aspect-ratio="1"
              :view-mode="2"
              drag-mode="move"
              auto-crop-area="0.8"
              background="false"
              style="width: 100%; height: 360px;"
              @crop="updatePreview"
            />
          </div>
          
          <!-- 预览区域 - 移动版 -->
          <div class="w-full flex flex-col items-center mt-2">
            <h3 :class="['text-sm font-medium mb-2', isDark ? 'text-blue-200' : 'text-blue-600']">预览</h3>
            <div :class="['w-32 h-32 rounded-full overflow-hidden shadow-lg', isDark ? 'border-blue-500/30 shadow-blue-500/20' : 'border-blue-300/40 shadow-blue-400/10']" style="border-width: 2px;">
              <img :src="previewData || cropperImage" class="w-full h-full object-cover" />
            </div>
          </div>
        </div>
        
        <div class="mt-4 md:mt-6 flex justify-end gap-3">
          <button @click="cancelCrop" :class="['rounded-md px-3 py-1.5 sm:px-4 sm:py-2 text-sm sm:text-base transition-all duration-300', isDark ? 'bg-gray-700 text-white hover:bg-gray-600 border border-gray-600' : 'bg-gray-200 text-gray-700 hover:bg-gray-300 border border-gray-300']">取消</button>
          <button @click="confirmCrop" :disabled="isUploading" :class="['rounded-md px-3 py-1.5 sm:px-4 sm:py-2 text-sm sm:text-base text-white transition-all duration-300 shadow-md', isUploading ? 'opacity-70 cursor-not-allowed' : '', isDark ? 'bg-gradient-to-r from-indigo-600 to-blue-600 hover:from-indigo-700 hover:to-blue-700 disabled:from-indigo-400 disabled:to-blue-400 shadow-blue-500/30' : 'bg-gradient-to-r from-indigo-500 to-blue-500 hover:from-indigo-600 hover:to-blue-600 disabled:from-indigo-300 disabled:to-blue-300 shadow-blue-400/20']">
            <span class="flex items-center justify-center">
              <svg v-if="isUploading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ isUploading ? '上传中...' : '确认裁剪' }}
            </span>
          </button>
        </div>
      </div>
    </div>
  </teleport>
</template>


<script setup>
import { ref, computed } from 'vue'
import VueCropper from 'vue-cropperjs'
import '/node_modules/cropperjs/dist/cropper.css'
import { updateUserById } from '@/api/userApi'
import { useUserStore } from '@/stores/user'

// 接收主题状态
const props = defineProps({
  modelValue: String,
  defaultAvatar: {
    type: String,
    default: 'https://cdn.example.com/default-avatar.png'
  },
  isDark: {
    type: Boolean,
    default: false
  }
})

// 计算当前主题状态

const isDark = computed(() => props.isDark)

const emit = defineEmits(['update:modelValue', 'success'])

const showCropper = ref(false)
const cropper = ref(null)
const cropperMobile = ref(null)
const cropperImage = ref('')
const previewData = ref('')
const isUploading = ref(false)

// 获取用户ID
const userStore = useUserStore()
const userId = userStore.user?.id

const handleFileSelect = (e) => {
  const file = e.target.files[0]
  if (!file) return
  
  const validTypes = ['image/jpeg', 'image/png', 'image/webp']
  if (!validTypes.includes(file.type)) {
    showToast('仅支持 JPG/PNG/WEBP 格式图片', 'error')
    return
  }
  if (file.size > 2 * 1024 * 1024) {
    showToast('图片大小不能超过2MB', 'error')
    return
  }

  const reader = new FileReader()
  reader.onload = (event) => {
    cropperImage.value = event.target.result
    showCropper.value = true
  }
  reader.readAsDataURL(file)
  e.target.value = ''
}

const updatePreview = () => {
  // 根据当前活动的裁剪器更新预览
  const activeCropper = window.innerWidth < 768 ? cropperMobile.value : cropper.value
  if (activeCropper) {
    previewData.value = activeCropper.getCroppedCanvas({
      width: 200,
      height: 200,
      imageSmoothingQuality: 'high'
    }).toDataURL('image/png')
  }
}

const confirmCrop = async () => {
  // 根据当前活动的裁剪器获取裁剪结果
  const activeCropper = window.innerWidth < 768 ? cropperMobile.value : cropper.value
  if (!activeCropper || !userId) return
  
  const canvas = activeCropper.getCroppedCanvas({
    width: 256,
    height: 256,
    imageSmoothingQuality: 'high'
  })
  const base64 = canvas.toDataURL('image/png')
  
  try {
    isUploading.value = true
    showToast('正在上传头像...', 'info')
    
    console.log('准备更新头像，用户ID:', userId)
    console.log('头像数据前50字符:', base64.substring(0, 50))
    
    // 直接调用API上传头像
    const res = await updateUserById({ 
      id: userId, 
      avatar: base64,
      updateType: 'avatar'
    })
    
    console.log('头像上传接口响应:', res)
    
    if (res.status === 'success') {
      // 更新本地显示
      emit('update:modelValue', base64)
      // 通知父组件上传成功
      emit('success', base64)
      showToast('头像上传成功')
      cancelCrop()
    } else {
      showToast(res.info || '头像上传失败', 'error')
    }
  } catch (error) {
    console.error('头像上传错误:', error)
    showToast(`头像上传失败: ${error.message}`, 'error')
  } finally {
    isUploading.value = false
  }
}

const cancelCrop = () => {
  showCropper.value = false
  cropperImage.value = ''
  previewData.value = ''
}

// 显示美化的通知 - 使用Tailwind类替代硬编码样式
const showToast = (message, type = 'success') => {
  // 移除可能存在的旧通知
  const existingToasts = document.querySelectorAll('.avatar-toast')
  existingToasts.forEach(toast => {
    if (document.body.contains(toast)) {
      document.body.removeChild(toast)
    }
  })
  
  // 根据类型设置颜色
  const typeConfig = {
    success: {
      bgColor: 'bg-emerald-500',
      iconBg: 'bg-emerald-500',
      icon: `<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>`
    },
    info: {
      bgColor: 'bg-blue-500',
      iconBg: 'bg-blue-500',
      icon: `<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>`
    },
    error: {
      bgColor: 'bg-red-500',
      iconBg: 'bg-red-500',
      icon: `<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>`
    }
  }
  
  const config = typeConfig[type] || typeConfig.success
  
  // 创建通知容器
  const toast = document.createElement('div')
  toast.className = `avatar-toast fixed top-4 right-4 z-[9500] transition-all duration-300 transform translate-x-full 
                    bg-white dark:bg-gray-800 border-l-4 ${config.bgColor.replace('bg-', 'border-')} 
                    rounded-md shadow-lg min-w-[250px] max-w-[320px] flex items-center py-3 px-4 mx-2`
  
  // 创建图标
  const iconElement = document.createElement('div')
  iconElement.className = `${config.iconBg} text-white w-6 h-6 rounded-full flex items-center justify-center mr-3 flex-shrink-0`
  iconElement.innerHTML = config.icon
  
  // 创建消息文本
  const messageElement = document.createElement('div')
  messageElement.className = 'text-gray-800 dark:text-gray-200 text-sm flex-grow'
  messageElement.textContent = message
  
  // 创建关闭按钮
  const closeButton = document.createElement('button')
  closeButton.className = 'text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 ml-2 flex-shrink-0 focus:outline-none'
  closeButton.innerHTML = `<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
  </svg>`
  
  closeButton.onclick = () => {
    toast.classList.add('opacity-0', 'translate-x-full')
    setTimeout(() => {
      if (document.body.contains(toast)) {
        document.body.removeChild(toast)
      }
    }, 300)
  }
  
  // 组装通知
  toast.appendChild(iconElement)
  toast.appendChild(messageElement)
  toast.appendChild(closeButton)
  document.body.appendChild(toast)
  
  // 动画显示
  setTimeout(() => {
    toast.classList.remove('translate-x-full')
  }, 10)
  
  // 自动关闭
  const timeout = setTimeout(() => {
    toast.classList.add('opacity-0', 'translate-x-full')
    setTimeout(() => {
      if (document.body.contains(toast)) {
        document.body.removeChild(toast)
      }
    }, 300)
  }, 3000)
  
  // 鼠标悬停时暂停自动关闭
  toast.addEventListener('mouseenter', () => {
    clearTimeout(timeout)
  })
  
  // 鼠标离开时恢复自动关闭
  toast.addEventListener('mouseleave', () => {
    setTimeout(() => {
      toast.classList.add('opacity-0', 'translate-x-full')
      setTimeout(() => {
        if (document.body.contains(toast)) {
          document.body.removeChild(toast)
        }
      }, 300)
    }, 2000)
  })
}
</script>