/*
   script.js - 个人主页交互
   原生 JavaScript, 无外部依赖
   功能: 移动端菜单切换, 打招呼按钮交互
*/

// 等待 DOM 内容加载完成
document.addEventListener('DOMContentLoaded', function () {

    // ---------- 移动端菜单切换 ----------
    const toggleButton = document.getElementById('menuToggle');
    const navMenu = document.querySelector('.main-nav');

    if (toggleButton && navMenu) {
        // 点击汉堡按钮切换菜单显示
        toggleButton.addEventListener('click', function (event) {
            event.stopPropagation(); // 防止事件冒泡干扰
            navMenu.classList.toggle('open');

            // 更新 aria-expanded 属性 (辅助无障碍)
            const isExpanded = navMenu.classList.contains('open');
            toggleButton.setAttribute('aria-expanded', isExpanded);
        });

        // 点击导航链接后自动关闭菜单 (增强移动体验)
        const navLinks = navMenu.querySelectorAll('.nav-list a');
        navLinks.forEach(function (link) {
            link.addEventListener('click', function () {
                // 当窗口宽度小于等于768px 才关闭, 避免桌面误触
                if (window.innerWidth <= 768) {
                    navMenu.classList.remove('open');
                    toggleButton.setAttribute('aria-expanded', 'false');
                }
            });
        });

        // 点击页面其他区域也关闭菜单 (可选, 提升易用性)
        document.addEventListener('click', function (event) {
            if (window.innerWidth <= 768) {
                const isClickInsideMenu = navMenu.contains(event.target);
                const isClickOnToggle = toggleButton.contains(event.target);
                if (!isClickInsideMenu && !isClickOnToggle) {
                    navMenu.classList.remove('open');
                    toggleButton.setAttribute('aria-expanded', 'false');
                }
            }
        });
    }

    // ---------- “打个招呼” 按钮 ----------
    const greetBtn = document.getElementById('greetBtn');
    const greetMessage = document.getElementById('greetMessage');

    if (greetBtn && greetMessage) {
        // 默认隐藏消息，由按钮触发显示
        greetBtn.addEventListener('click', function () {
            // 切换显示一条友好的消息 (简单交互)
            if (greetMessage.style.display === 'none' || greetMessage.style.display === '') {
                greetMessage.textContent = '🎉 你好呀！感谢来访，欢迎随时交流～';
                greetMessage.style.display = 'block';
                greetBtn.textContent = '👋 隐藏消息';
            } else {
                greetMessage.style.display = 'none';
                greetBtn.textContent = '👋 打个招呼';
            }
        });
    }

    // ---------- 可选的: 页面加载后控制台小彩蛋 (非必须) ----------
    console.log('🌟 个人主页 · 记录 · 用 ❤️ 和原生三件套构建');
    console.log('📬 欢迎联系: chenyao@example.com');
});