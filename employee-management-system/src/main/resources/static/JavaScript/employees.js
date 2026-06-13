document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("employeeSearch");
    const tableRows = document.querySelectorAll("#employeeTable tbody tr");

    searchInput.addEventListener("keyup", function () {

        const searchValue = searchInput.value.toLowerCase();

        tableRows.forEach(function (row) {

            const name = row.children[0].textContent.toLowerCase();
            const email = row.children[1].textContent.toLowerCase();
            const department = row.children[2].textContent.toLowerCase();

            if (
                name.includes(searchValue) ||
                email.includes(searchValue) ||
                department.includes(searchValue)
            ) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }

        });

    });

});