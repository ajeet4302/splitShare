const API_URL = "https://splitshare-vfh9.onrender.com";

async function login() {

    const email = document.getElementById("email").value.trim();

    const password = document.getElementById("password").value.trim();

    if (email === "") {
        alert("Please enter your email.");
        return;
    }

    if (password === "") {
        alert("Please enter your password.");
        return;
    }

    try {

        const response = await fetch(API_URL + "/users/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                password: password
            })

        });

        const data = await response.json();

        if (response.ok) {

            localStorage.setItem("token", data.token);
            localStorage.setItem("userId", data.id);
            localStorage.setItem("userName", data.name);
            localStorage.setItem("email", data.email);

            alert("Login Successful");

            window.location.href = "dashboard.html";

        } else {

            alert(data.message || "Invalid Email or Password");

        }

    } catch (error) {

        console.log(error);

        alert("Unable to connect to server.");

    }

}