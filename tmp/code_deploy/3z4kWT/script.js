// script.js — 任务记录工具 (不超过20行核心逻辑)
"use strict";

// 获取 DOM 元素
const taskInput = document.getElementById('taskInput');
const addBtn = document.getElementById('addBtn');
const taskList = document.getElementById('taskList');
const taskCount = document.getElementById('taskCount');

// 存储任务数据 (数组)
let tasks = [];

// 渲染任务列表 (更新界面 + 计数)
function renderTasks() {
  // 清空列表，重新构建 (保持简单)
  taskList.innerHTML = '';
  // 遍历 tasks 数组生成 DOM
  tasks.forEach((task, index) => {
    const li = document.createElement('li');
    li.className = `task-item${task.done ? ' completed' : ''}`;
    li.dataset.index = index;

    // 复选框
    const check = document.createElement('input');
    check.type = 'checkbox';
    check.className = 'task-check';
    check.checked = task.done;
    // 切换完成状态
    check.addEventListener('change', () => {
      task.done = check.checked;
      renderTasks();
    });

    // 任务文本
    const span = document.createElement('span');
    span.className = 'task-text';
    span.textContent = task.text;

    // 删除按钮
    const delBtn = document.createElement('button');
    delBtn.className = 'delete-btn';
    delBtn.innerHTML = '✕';
    delBtn.setAttribute('aria-label', '删除任务');
    delBtn.addEventListener('click', () => {
      tasks.splice(index, 1);   // 从数组移除
      renderTasks();
    });

    li.appendChild(check);
    li.appendChild(span);
    li.appendChild(delBtn);
    taskList.appendChild(li);
  });

  // 更新底部计数
  const total = tasks.length;
  taskCount.textContent = `${total} 项任务`;
}

// 添加新任务
function addTask() {
  const text = taskInput.value.trim();
  if (text === '') {
    taskInput.focus();
    return;
  }
  // 推入新任务 (默认未完成)
  tasks.push({ text: text, done: false });
  taskInput.value = '';       // 清空输入框
  taskInput.focus();
  renderTasks();
}

// 清除所有已完成任务
function clearCompleted() {
  tasks = tasks.filter(task => !task.done);
  renderTasks();
}

// 绑定事件
addBtn.addEventListener('click', addTask);
taskInput.addEventListener('keydown', (e) => {
  if (e.key === 'Enter') addTask();
});
document.getElementById('clearBtn').addEventListener('click', clearCompleted);

// 初始空状态渲染 (也作为导出示例)
renderTasks();