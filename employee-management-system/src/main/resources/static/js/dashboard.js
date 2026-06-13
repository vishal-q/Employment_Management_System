/* ==============================
   DARK MODE
   ============================== */
function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDark);
    const btn = document.querySelector('.dark-toggle');
    if (btn) btn.textContent = isDark ? '\u2600\uFE0F' : '\uD83C\uDF19';
}

document.addEventListener('DOMContentLoaded', function () {
    if (localStorage.getItem('darkMode') === 'true') {
        document.body.classList.add('dark-mode');
        const btn = document.querySelector('.dark-toggle');
        if (btn) btn.textContent = '\u2600\uFE0F';
    }
});

/* ==============================
   COUNTER ANIMATION
   ============================== */
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.counter').forEach(function (el) {
        var target = parseInt(el.innerText) || 0;
        if (target === 0) return;
        var step = Math.ceil(target / 40);
        var current = 0;
        var timer = setInterval(function () {
            current += step;
            if (current >= target) { el.innerText = target; clearInterval(timer); }
            else el.innerText = current;
        }, 20);
    });
});

/* ==============================
   AUTO-HIDE ALERTS
   ============================== */
document.addEventListener('DOMContentLoaded', function () {
    var alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (a) {
        setTimeout(function () {
            a.style.transition = 'opacity 0.5s';
            a.style.opacity = '0';
            setTimeout(function () { a.style.display = 'none'; }, 500);
        }, 4000);
    });
});

/* ==============================
   ACTIVE SIDEBAR LINK
   ============================== */
document.addEventListener('DOMContentLoaded', function () {
    var path = window.location.pathname;
    document.querySelectorAll('.sidebar a').forEach(function (link) {
        if (link.getAttribute('href') === path) {
            document.querySelectorAll('.sidebar a').forEach(function (l) { l.classList.remove('active'); });
            link.classList.add('active');
        }
    });
});

console.log('EMS loaded');
