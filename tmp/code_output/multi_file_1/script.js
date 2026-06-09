document.addEventListener('DOMContentLoaded', () => {
  const taskInput = document.getElementById('task-text');
  const prioritySelect = document.getElementById('task-priority');
  const addBtn = document.getElementById('add-task-btn');
  const taskList = document.getElementById('tasks');
  const filterBtns = document.querySelectorAll('.filter-btn');

  let tasks = JSON.parse(localStorage.getItem('tasks')) || [];
  let currentFilter = '全部';

  function saveTasks() {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }

  function renderTasks() {
    const filteredTasks = tasks.filter(task => {
      if (currentFilter === '待完成') return !task.completed;
      if (currentFilter === '已完成') return task.completed;
      return true;
    });

    taskList.innerHTML = filteredTasks.map((task, index) => {
      const originalIndex = tasks.indexOf(task);
      return `
        <li class="task-item" data-index="${originalIndex}">
          <input type="checkbox" class="task-check" ${task.completed ? 'checked' : ''}>
          <span class="task-text ${task.completed ? 'completed' : ''}">${escapeHtml(task.text)}</span>
          <span class="task-priority priority-${task.priority}">${task.priority}</span>
          <button class="delete-btn">&times;</button>
        </li>
      `;
    }).join('');
  }

  function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  function addTask() {
    const text = taskInput.value.trim();
    if (!text) {
      alert('请输入任务内容');
      return;
    }
    const priority = prioritySelect.value;
    tasks.push({ text, priority, completed: false });
    saveTasks();
    renderTasks();
    taskInput.value = '';
  }

  function deleteTask(index) {
    tasks.splice(index, 1);
    saveTasks();
    renderTasks();
  }

  function toggleTask(index) {
    tasks[index].completed = !tasks[index].completed;
    saveTasks();
    renderTasks();
  }

  function setFilter(filter) {
    currentFilter = filter;
    filterBtns.forEach(btn => {
      btn.classList.toggle('active', btn.dataset.filter === filter);
    });
    renderTasks();
  }

  addBtn.addEventListener('click', addTask);
  taskInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') addTask();
  });

  taskList.addEventListener('click', (e) => {
    const item = e.target.closest('.task-item');
    if (!item) return;
    const index = parseInt(item.dataset.index);
    if (e.target.classList.contains('delete-btn')) {
      deleteTask(index);
    } else if (e.target.classList.contains('task-check')) {
      toggleTask(index);
    }
  });

  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => setFilter(btn.dataset.filter));
  });

  // 初始化渲染
  setFilter('全部');
});