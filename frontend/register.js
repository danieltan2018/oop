async function register() {
    button = document.getElementById("submitButton");
    button.textContent = 'Please wait...';
    const user = document.getElementById("inputEmail").value;
    const pass = document.getElementById("inputPassword").value;
    const url = window.location.protocol + "//" + window.location.hostname + ":8080/" + "register?user=" + user + "&pass=" + pass;
    const response = await fetch(url, { method: 'POST' }).then(response => response.json())
        .then(data => {
            if (data) {
                window.location.replace("registersuccess.html");
            } else {
                alert("Unable to register");
                location.reload();
            }
        });
}