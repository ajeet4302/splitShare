const API_URL = "http://localhost:8096";

const token = localStorage.getItem("token");

const groupId = localStorage.getItem("selectedGroupId");

if (!token) {

    window.location.href = "login.html";

}

if (!groupId) {

    alert("No Group Selected");

    window.location.href = "groups.html";

}

// ==========================
// Load Members
// ==========================

async function loadMembers() {

    try {

        const response = await fetch(

            `${API_URL}/groups/${groupId}/members`,

            {

                headers: {

                    "Authorization": "Bearer " + token

                }

            }

        );

        if (!response.ok) {

            throw new Error("Unable to load members");

        }

        const members = await response.json();

        const table = document.getElementById("memberTable");

        table.innerHTML = "";

        if (members.length === 0) {

            table.innerHTML = `

                <tr>

                    <td colspan="2" class="text-center">

                        No Members Found

                    </td>

                </tr>

            `;

            return;

        }

        members.forEach((member, index) => {

            table.innerHTML += `

                <tr>

                    <td>${index + 1}</td>

                    <td>${member.userName}</td>

                </tr>

            `;

        });

    }

    catch (error) {

        console.log(error);

        alert("Unable to load members.");

    }

}

loadMembers();

// ==========================
// Add Member
// ==========================

document
    .getElementById("addMemberBtn")
    .addEventListener("click", addMember);

async function addMember() {

    const email = document
        .getElementById("memberEmail")
        .value
        .trim();

    if (email === "") {

        alert("Please enter member email.");

        return;

    }

    try {

        const response = await fetch(

            `${API_URL}/groups/${groupId}/members`,

            {

                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": "Bearer " + token

                },

                body: JSON.stringify({

                    email: email

                })

            }

        );

        const message = await response.text();

        alert(message);

        if (response.ok) {

            document.getElementById("memberEmail").value = "";

            loadMembers();

        }

    }

    catch (error) {

        console.log(error);

        alert("Unable to add member.");

    }

}

// ==========================
// Logout
// ==========================

function logout() {

    localStorage.removeItem("token");

    window.location.href = "login.html";

}