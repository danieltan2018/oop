user = localStorage.getItem("user");
if (!user) {
    window.location.replace("login.html");
}

function logout() {
    localStorage.removeItem("user");
    window.location.replace("login.html");
}