import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'list', component: () => import('@/pages/TaskList.vue') },
  { path: '/add', name: 'add', component: () => import('@/pages/TaskAdd.vue') },
  { path: '/edit/:id', name: 'edit', component: () => import('@/pages/TaskAdd.vue') }
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
