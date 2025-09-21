import { defineStore } from 'pinia'
import { ref, onMounted, onBeforeUnmount } from 'vue'

export const useSidebarStore = defineStore('sidebar', {
  state: () => ({
    visible: window.innerWidth >= 768, // 桌面端默认显示
    isMobile: false,
    breakpoint: 768
  }),
  actions: {
    toggle() {
      this.visible = !this.visible
    },
    show() {
      this.visible = true
    },
    hide() {
      this.visible = false
    },
    checkViewport() {
      this.isMobile = window.innerWidth < this.breakpoint
      if (!this.isMobile) this.show()
    }
  },
  setup() {
    const store = useSidebarStore()
    
    onMounted(() => {
      store.checkViewport()
      window.addEventListener('resize', store.checkViewport)
    })
    
    onBeforeUnmount(() => {
      window.removeEventListener('resize', store.checkViewport)
    })
  }
})