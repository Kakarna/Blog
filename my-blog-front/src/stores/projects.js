import { defineStore } from 'pinia'
import { ref } from 'vue'
import { addWithFiles } from '../api/project' // 后端接口

export const useProjectsStore = defineStore('projects', () => {
  const projects = ref([])

  // 模拟初始数据，可为空
  // projects.value = [...]

  async function uploadProject(projectData) {
    // 调用后端接口
    await addWithFiles(projectData)
    // 成功后推入本地状态
    projects.value.unshift({
      ...projectData,
      id: Date.now()  // 先用时间戳做id，后续可改成后端返回的id
    })
  }

  function findProjectById(id) {
    return projects.value.find(p => p.id === id)
  }

  return {
    projects,
    uploadProject,
    findProjectById
  }
})
