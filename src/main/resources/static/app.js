let menuIcon = document.getElementById("menu-icon");
menuIcon.addEventListener("click", () => {
    let menu = document.getElementById("menu");
    if (menu.style.display === "flex") {
        menu.style.display = "none";
        menuIcon.style.order = "1";
    } else {
        menu.style.display = "flex";
        menuIcon.style.order = "0";
    }
});

function confirmNewChampionship() {
    if (confirm("Are you sure you want to close the current Championship and start a new one?")) {
        fetch('/fifa/championship/new')
            .then(function(response) {
                return response.text()
            })
            .then(function(html) {
                document.open();
                document.write(html);
                document.close();
            })
            .catch(function(err) {
                console.log('Failed to fetch page: ', err);
            });
    }
}