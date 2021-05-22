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