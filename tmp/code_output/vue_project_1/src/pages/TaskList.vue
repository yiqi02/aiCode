<template>
  <div class="task-list">
    <div class="stats">
      <span>全部 {{ tasks.length }}</span>
      <span>待办 {{ tasks.filter(t => !t.done).length }}</span>
      <span>已完成 {{ tasks.filter(t => t.done).length }}</span>
    </div>

    <div v-if="tasks.length === 0" class="empty">还没有任务，去新建一条吧 ✨</div>

    <div v-for="task in tasks" :key="task.id" class="task-item" :class="{ done: task.done }">
      <input type="checkbox" :checked="task.done" @change="toggle(task.id)" />
      <div class="info">
        <div class="title">{{ task.title }}</div>
        <div class="meta">{{ task.time }} · {{ task.done ? '已完成' : '待办' }}</div>
      </div>
      <div class="actions">
        <router-link :to="'/edit/' + task.id" class="btn-edit">编辑</router-link>
        <button @click="remove(task.id)" class="btn-del">删除</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tasks = ref(JSON.parse(localStorage.getItem('tasks') || '[]'))

function save() { localStorage.setItem('tasks', JSON.stringify(tasks.value)) }

function toggle(id) {
  const t = tasks.value.find(t => t.id === id)
  if (t) { t.done = !t.done; save() }
}

function remove(id) {
  tasks.value = tasks.value.filter(t => t.id !== id)
  save()
}
</script>

<style scoped>
.stats { display: flex; gap: 16px; font-size: 14px; color: #666; margin-bottom: 16px; }
.stats span { background: #fff; padding: 6px 14px; border-radius: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.06); }
.empty { text-align: center; padding: 60px 0; color: #999; font-size: 16px; }
.task-item { display: flex; align-items: center; gap: 12px; background: #fff; padding: 14px 16px; border-radius: 8px; margin-bottom: 10px; box-shadow: 0 1px 3px rgba(0,0,0,0.06); }
.task-item.done { opacity: 0.6; }
.task-item.done .title { text-decoration: line-through; }
.info { flex: 1; }
.title { font-size: 15px; font-weight: 500; }
.meta { font-size: 12px; color: #999; margin-top: 4px; }
.actions { display: flex; gap: 8px; }
.btn-edit, .btn-del { padding: 4px 10px; border-radius: 4px; font-size: 13px; border: none; cursor: pointer; text-decoration: none; }
.btn-edit { background: #e3f2fd; color: #1976d2; }
.btn-del { background: #fbe9e7; color: #d32f2f; }
input[type="checkbox"] { width: 18px; height: 18px; cursor: pointer; }
</style>
