async function login() {
    const button = document.getElementById("submitButton");
    button.textContent = 'Please wait...';
    const user = document.getElementById("inputEmail").value;
    const pass = document.getElementById("inputPassword").value;
    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "login?user=" + user + "&pass=" + pass;
    await fetch(url, { method: 'POST' }).then(response => response.json())
        .then(data => {
            if (data) {
                localStorage.setItem("user", user);
                window.location.replace("index.html");
            } else {
                alert("Incorrect username or password");
                location.reload(); //reload page
            }
        });
}