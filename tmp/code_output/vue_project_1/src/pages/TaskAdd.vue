<template>
  <div class="task-form">
    <h2>{{ isEdit ? '编辑任务' : '新建任务' }}</h2>
    <form @submit.prevent="submit">
      <label>任务标题</label>
      <input v-model="title" placeholder="输入任务内容..." required maxlength="100" />

      <div class="btns">
        <button type="submit" class="btn-save">{{ isEdit ? '保存修改' : '添加任务' }}</button>
        <router-link to="/" class="btn-cancel">取消</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const title = ref('')

if (isEdit.value) {
  const tasks = JSON.parse(localStorage.getItem('tasks') || '[]')
  const task = tasks.find(t => t.id === Number(route.params.id))
  if (task) title.value = task.title
}

function submit() {
  const tasks = JSON.parse(localStorage.getItem('tasks') || '[]')
  const now = new Date().toLocaleString('zh-CN')

  if (isEdit.value) {
    const t = tasks.find(t => t.id === Number(route.params.id))
    if (t) { t.title = title.value; t.time = now }
  } else {
    tasks.unshift({ id: Date.now(), title: title.value, done: false, time: now })
  }

  localStorage.setItem('tasks', JSON.stringify(tasks))
  router.push('/')
}
</script>

<style scoped>
.task-form { background: #fff; padding: 24px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.06); }
h2 { font-size: 18px; margin-bottom: 20px; }
label { display: block; font-size: 14px; color: #666; margin-bottom: 6px; }
input { width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 15px; outline: none; }
input:focus { border-color: #4caf50; }
.btns { display: flex; gap: 10px; margin-top: 20px; }
.btn-save { flex: 1; padding: 10px; background: #4caf50; color: #fff; border: none; border-radius: 6px; font-size: 15px; cursor: pointer; }
.btn-save:hover { background: #43a047; }
.btn-cancel { flex: 1; display: flex; align-items: center; justify-content: center; padding: 10px; background: #f5f5f5; color: #666; text-decoration: none; border-radius: 6px; font-size: 15px; }
</style>
