<template>
  <div class="app">
    <nav>
      <router-link to="/" class="logo">📋 任务记录</router-link>
      <router-link to="/add" class="add-btn">+ 新建</router-link>
    </nav>
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; color: #333; }
.app { max-width: 600px; margin: 0 auto; padding: 16px; }
nav { display: flex; justify-content: space-between; align-items: center; padding: 16px 0; margin-bottom: 20px; border-bottom: 2px solid #e0e0e0; }
.logo { font-size: 20px; font-weight: bold; text-decoration: none; color: #333; }
.add-btn { text-decoration: none; background: #4caf50; color: #fff; padding: 8px 16px; border-radius: 6px; font-size: 14px; }
.add-btn:hover { background: #43a047; }
main { min-height: 60vh; }
</style>
