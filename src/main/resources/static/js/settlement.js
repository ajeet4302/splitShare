const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

// =======================
// Load Groups
// =======================

async function loadGroups() {

    try {

        const response = await fetch("http://localhost:8096/groups", {

            headers: {
                "Authorization": "Bearer " + token
            }

        });

        const groups = await response.json();

        const groupSelect = document.getElementById("groupSelect");

        groupSelect.innerHTML =
            '<option value="">Select Group</option>';

        groups.forEach(group => {

            groupSelect.innerHTML += `
                <option value="${group.id}">
                    ${group.groupName}
                </option>
            `;

        });

    } catch (error) {

        alert("Unable to load groups.");

    }

}

loadGroups();


// =======================
// Load Members
// =======================

document
    .getElementById("groupSelect")
    .addEventListener("change", loadMembers);

async function loadMembers() {

    const groupId = document.getElementById("groupSelect").value;

    const receiverSelect =
        document.getElementById("receiverSelect");

    receiverSelect.innerHTML =
        '<option value="">Select Member</option>';

    if (groupId === "") return;

    try {

        const response = await fetch(

            `http://localhost:8096/groups/${groupId}/members`,

            {

                headers: {
                    "Authorization": "Bearer " + token
                }

            }

        );

        const members = await response.json();

        members.forEach(member => {

            receiverSelect.innerHTML += `

                <option value="${member.userId}">

                    ${member.userName}

                </option>

            `;

        });

    } catch (error) {

        alert("Unable to load members.");

    }

}


// =======================
// Settlement
// =======================

document
    .getElementById("settlementForm")
    .addEventListener("submit", settlePayment);

async function settlePayment(e) {

    e.preventDefault();

    const settlement = {

        groupId: document.getElementById("groupSelect").value,

        receiverId: document.getElementById("receiverSelect").value,

        amount: document.getElementById("amount").value

    };

    try {

        const response = await fetch(

            "http://localhost:8096/settlements",

            {

                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": "Bearer " + token

                },

                body: JSON.stringify(settlement)

            }

        );

        if (response.ok) {

            alert("✅ Settlement Successful");

            document.getElementById("settlementForm").reset();

            document.getElementById("receiverSelect").innerHTML =
                '<option value="">Select Member</option>';

        } else {

            const message = await response.text();

            alert(message);

        }

    } catch (error) {

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