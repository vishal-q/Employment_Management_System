/* ==============================
   DASHBOARD JAVASCRIPT
   ============================== */


/* ==============================
   DARK MODE
   ============================== */

function toggleDarkMode() {

    document.body.classList.toggle("dark-mode");

}


/* ==============================
   NOTIFICATION BELL
   ============================== */

document.addEventListener("DOMContentLoaded", function () {

    const bell = document.querySelector(".notification");

    if (bell) {

        bell.addEventListener("click", function () {

            alert("No new notifications");

        });

    }

});


/* ==============================
   CARD COUNTER ANIMATION
   ============================== */

document.addEventListener("DOMContentLoaded", function () {

    const counters = document.querySelectorAll(".counter");

    counters.forEach(counter => {

        const target = parseInt(counter.innerText);

        let count = 0;

        const increment = target / 60;

        function updateCounter() {

            count += increment;

            if (count < target) {

                counter.innerText = Math.ceil(count);

                requestAnimationFrame(updateCounter);

            } else {

                counter.innerText = target;

            }

        }

        updateCounter();

    });

});


/* ==============================
   PAGE LOAD LOG (Optional)
   ============================== */

document.addEventListener("DOMContentLoaded", function () {

    console.log("Dashboard Loaded Successfully");

});