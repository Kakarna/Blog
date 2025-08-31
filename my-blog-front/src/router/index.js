import { createRouter, createWebHistory } from 'vue-router'

// 视图组件导入
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'


const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'UserLogin',
    component: Login
  },
  {
    path: '/register',
    name: 'UserRegister',
    component: Register
  },
  {
    path: '/techNotes/:section',
    name: 'TechNoteList',
    component: () => import('@/views/TechNoteList.vue'),
    props: true
  },
  {
    path: '/techNotes/:section/new',
    name: 'TechNoteAdd',
    component: () => import('@/views/TechNoteAdd.vue'),
    props: true
  },
  {
    path: '/techNotes/:section/:id/edit',
    name: 'TechNoteEdit',
    component: () => import('@/views/TechNoteAdd.vue'),
    props: true
  },
  {
    path: '/techNotes/:section/:id',
    name: 'TechNoteDetail',
    component: () => import('@/views/TechNoteDetail.vue')
  },
  {
    path: '/LearningTimeline',
    name: 'LearningTimeline',
    component: () => import('@/views/LearningTimeline.vue')
  },
  {
    path: '/MyProjects',
    name: 'MyProjects',
    component: () => import('@/views/MyProjects.vue')
  },
   {
    path: '/MyProjects/:projectId',
    component: () => import('@/views/ProjectDetail.vue'),
    props: true
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
