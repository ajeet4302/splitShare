const token = localStorage.getItem("token");

if (!token) {

    window.location.href = "login.html";

}

// =======================
// Load Profile
// =======================

async function loadProfile() {

    try {

        const response = await fetch(

            "http://localhost:8096/users/profile",

            {

                headers: {

                    "Authorization": "Bearer " + token

                }

            }

        );

        if (!response.ok) {

            alert("Unable to load profile.");

            return;

        }

        const user = await response.json();

        document.getElementById("name").value = user.name;

        document.getElementById("email").value = user.email;

        document.getElementById("phone").value = user.phone;

    }

    catch (error) {

        alert("Server Error");

    }

}

loadProfile();


// =======================
// Update Profile
// =======================

document
    .getElementById("profileForm")
    .addEventListener("submit", updateProfile);

async function updateProfile(e) {

    e.preventDefault();

    const profile = {

        name: document.getElementById("name").value,

        phone: document.getElementById("phone").value

    };

    try {

        const response = await fetch(

            "http://localhost:8096/users/profile",

            {

                method: "PUT",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": "Bearer " + token

                },

                body: JSON.stringify(profile)

            }

        );

        if (response.ok) {

            alert("✅ Profile Updated Successfully");

        }

        else {

            const message = await response.text();

            alert(message);

        }

    }

    catch (error) {

        alert("Server Error");

    }

}


// =======================
// Logout
// =======================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}