// ===================== script.js =====================

(function () {
  'use strict';

  // ---------- 1. 移动端导航切换 ----------
  const navToggle = document.getElementById('navToggle');
  const mainNav = document.getElementById('mainNav');
  const body = document.body;

  if (navToggle && mainNav) {
    // 切换导航打开/关闭
    const toggleNav = (e) => {
      e.stopPropagation();
      mainNav.classList.toggle('open');
      body.classList.toggle('nav-open');
    };

    navToggle.addEventListener('click', toggleNav);

    // 点击导航链接后自动关闭 (对移动端友好)
    const navLinks = mainNav.querySelectorAll('a');
    navLinks.forEach((link) => {
      link.addEventListener('click', () => {
        if (mainNav.classList.contains('open')) {
          mainNav.classList.remove('open');
          body.classList.remove('nav-open');
        }
      });
    });

    // 点击页面其他区域关闭导航 (通过监听 document)
    document.addEventListener('click', (e) => {
      if (mainNav.classList.contains('open')) {
        const target = e.target;
        // 如果点击的不是导航内部和切换按钮，关闭导航
        if (!mainNav.contains(target) && target !== navToggle && !navToggle.contains(target)) {
          mainNav.classList.remove('open');
          body.classList.remove('nav-open');
        }
      }
    });
  }

  // ---------- 2. 动态生成文章卡片 ----------
  const articleGrid = document.getElementById('articleGrid');
  if (articleGrid) {
    // 模拟博客文章数据
    const articles = [
      {
        title: 'CSS Grid 布局完全指南',
        date: '2025-06-10',
        summary: '深入讲解 Grid 布局的核心概念，包括网格容器、项目、轨道、区域等，以及常见的布局模式。',
        imgId: 200
      },
      {
        title: 'JavaScript 闭包与作用域链',
        date: '2025-05-28',
        summary: '理解闭包是如何形成的，作用域链的查找机制，以及在实际开发中的应用场景。',
        imgId: 180
      },
      {
        title: '从零搭建个人博客设计系统',
        date: '2025-04-15',
        summary: '分享如何定义颜色、排版、间距等设计令牌，构建一致且可维护的界面。',
        imgId: 160
      },
      {
        title: 'Flexbox 实战：常见布局解决方案',
        date: '2025-03-22',
        summary: '汇总 Flexbox 解决水平垂直居中、圣杯布局、 sticky footer 等经典场景。',
        imgId: 140
      }
    ];

    // 清空占位
    articleGrid.innerHTML = '';

    articles.forEach((article) => {
      const card = document.createElement('article');
      card.className = 'article-card';

      // 图片
      const img = document.createElement('img');
      img.src = `https://picsum.photos/600/350?random=${article.imgId}`;
      img.alt = `${article.title} 配图`;
      img.loading = 'lazy';

      // 内容容器
      const contentDiv = document.createElement('div');
      contentDiv.className = 'article-card-content';

      const titleEl = document.createElement('h3');
      titleEl.textContent = article.title;

      const meta = document.createElement('div');
      meta.className = 'meta';
      meta.textContent = `📅 ${article.date} · ☕ 5 分钟阅读`;

      const summary = document.createElement('p');
      summary.textContent = article.summary;

      const readMore = document.createElement('a');
      readMore.href = '#';
      readMore.className = 'read-more';
      readMore.textContent = '继续阅读 →';

      // 组装
      contentDiv.appendChild(titleEl);
      contentDiv.appendChild(meta);
      contentDiv.appendChild(summary);
      contentDiv.appendChild(readMore);
      card.appendChild(img);
      card.appendChild(contentDiv);

      articleGrid.appendChild(card);
    });
  }

  // ---------- 3. 联系表单反馈 (模拟提交) ----------
  const contactForm = document.getElementById('contactForm');
  const feedbackEl = document.getElementById('formFeedback');
  if (contactForm && feedbackEl) {
    contactForm.addEventListener('submit', function (e) {
      e.preventDefault();

      // 简单获取输入值
      const name = document.getElementById('name').value.trim();
      const email = document.getElementById('email').value.trim();
      const message = document.getElementById('message').value.trim();

      if (!name || !email || !message) {
        feedbackEl.textContent = '⚠️ 请填写所有字段。';
        feedbackEl.style.color = '#d32f2f';
        return;
      }

      // 模拟成功提示
      feedbackEl.textContent = `✅ 谢谢 ${name}，留言已收到！我会尽快回复。`;
      feedbackEl.style.color = '#2e7d32';

      // 清空表单
      contactForm.reset();

      // 3秒后清除提示
      setTimeout(() => {
        feedbackEl.textContent = '';
      }, 4000);
    });
  }

  // ---------- 4. 导航栏激活状态 (滚动或点击高亮) ----------
  const sections = document.querySelectorAll('section[id]');
  const navLinksAll = document.querySelectorAll('.main-nav a');

  function setActiveNav() {
    let current = '';

    sections.forEach((section) => {
      const sectionTop = section.offsetTop - 100; // 偏移补偿
      if (window.scrollY >= sectionTop) {
        current = section.getAttribute('id');
      }
    });

    navLinksAll.forEach((link) => {
      link.classList.remove('active');
      if (link.getAttribute('href') === `#${current}`) {
        link.classList.add('active');
      }
    });
  }

  // 滚动监听 (防抖降低消耗)
  let ticking = false;
  window.addEventListener('scroll', () => {
    if (!ticking) {
      window.requestAnimationFrame(() => {
        setActiveNav();
        ticking = false;
      });
      ticking = true;
    }
  });

  // 初始设置
  window.addEventListener('load', setActiveNav);

})();