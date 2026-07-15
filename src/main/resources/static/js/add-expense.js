const API_URL = "https://splitshare-vfh9.onrender.com";
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

// =======================
// Load Groups
// =======================

async function loadGroups() {

    try {

		const response = await fetch(API_URL + "/groups", {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token
            }

        });

        if (!response.ok) {

            alert("Unable to load groups.");

            return;
        }

        const groups = await response.json();

        const select = document.getElementById("groupSelect");

        select.innerHTML =
            '<option value="">Select Group</option>';

        groups.forEach(group => {

            select.innerHTML += `
                <option value="${group.id}">
                    ${group.groupName}
                </option>
            `;

        });

    }

    catch (error) {

        console.error(error);

        alert("Unable to load groups.");

    }

}

loadGroups();


// =======================
// Save Expense
// =======================

document
    .getElementById("expenseForm")
    .addEventListener("submit", saveExpense);

async function saveExpense(e) {

    e.preventDefault();

    const expense = {

        title: document.getElementById("title").value.trim(),

        description: document.getElementById("description").value.trim(),

        amount: parseFloat(document.getElementById("amount").value),

        splitType: document.getElementById("splitType").value,

        groupId: parseInt(document.getElementById("groupSelect").value)

    };

    try {

		const response = await fetch(API_URL + "/expenses", {

            method: "POST",

            headers: {

                "Content-Type": "application/json",

                "Authorization": "Bearer " + token

            },

            body: JSON.stringify(expense)

        });

        if (response.ok) {

            alert("✅ Expense Added Successfully");

            document.getElementById("expenseForm").reset();

        }

        else {

            const message = await response.text();

            alert(message);

        }

    }

    catch (error) {

        console.error(error);

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