// script.js —— 单次交互，反馈友好
(function() {
  'use strict';

  const btn = document.getElementById('actionBtn');
  const feedback = document.getElementById('feedback');

  // 简洁的交互反馈 —— 不超过20行核心逻辑 (含注释)
  function handleClick() {
    // 随机生成一句极简短句
    const messages = [
      '✨ 轻触即达 · 少即是多',
      '🌱 此刻专注 · 无扰',
      '⚡ 20行内 · 意蕴足',
      '🧘 极简 · 源于本心',
      '📐 结构即美学'
    ];
    const randomMsg = messages[Math.floor(Math.random() * messages.length)];
    feedback.textContent = randomMsg;

    // 给按钮增加一个微反馈 (200ms 后自动消失)
    btn.style.transform = 'scale(0.94)';
    setTimeout(() => {
      btn.style.transform = 'scale(1)';
    }, 150);
  }

  // 绑定事件
  btn.addEventListener('click', handleClick);

  // 初始空反馈 —— 但保留占位 (aria-live 会自然处理)
  // 无需额外初始化，体现简洁
})();