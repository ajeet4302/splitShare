const API_URL = "https://splitshare-vfh9.onrender.com";

document
.getElementById("registerForm")
.addEventListener("submit", registerUser);

async function registerUser(event) {

    event.preventDefault();

    const user = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        password: document.getElementById("password").value.trim()
    };

    try {

        const response = await fetch(API_URL + "/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {

            const result = await response.json();

            alert("✅ User Registered Successfully.");

            document.getElementById("registerForm").reset();

        } else {

            const message = await response.text();

            alert(message);

        }

    } catch (error) {

        console.log(error);

        alert("Server Error");

    }

}