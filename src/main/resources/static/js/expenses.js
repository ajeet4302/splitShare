const API_URL = "https://splitshare-vfh9.onrender.com";

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

window.onload = function () {

    loadGroups();

};

// =========================
// Load Groups
// =========================

async function loadGroups() {

    try {
		const response = await fetch(API_URL + "/groups", {
       

            headers: {
                "Authorization": "Bearer " + token
            }

        });

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

        // Auto select group from Dashboard

        const selectedGroupId = localStorage.getItem("selectedGroupId");

        if (selectedGroupId) {

            select.value = selectedGroupId;

            loadExpenses();

        }

    }

    catch (error) {

        alert("Unable to load groups.");

    }

}

// =========================
// Load Expenses
// =========================

document
    .getElementById("loadExpenseBtn")
    .addEventListener("click", loadExpenses);

async function loadExpenses() {

    const groupId =
        document.getElementById("groupSelect").value;

    if (groupId === "") {

        alert("Please select a group.");

        return;

    }

    document.getElementById("loading").style.display = "block";

    try {

        const response = await fetch(

            `${API_URL}/expenses/group/${groupId}`,

            {

                headers: {

                    "Authorization": "Bearer " + token

                }

            });

        const expenses = await response.json();

        const table =
            document.getElementById("expenseTable");

        table.innerHTML = "";

        if (expenses.length === 0) {

            table.innerHTML = `

                <tr>

                    <td colspan="5" class="text-center">

                        No Expenses Found

                    </td>

                </tr>

            `;

        }

        else {

            expenses.forEach(expense => {

                table.innerHTML += `

                    <tr>

                        <td>${expense.title}</td>

                        <td>${expense.description ?? "-"}</td>

                        <td class="amount">₹${expense.amount}</td>

                        <td>${expense.paidBy}</td>

                        <td>${expense.expenseDate}</td>

                    </tr>

                `;

            });

        }

    }

    catch (error) {

        alert("Unable to load expenses.");

    }

    finally {

        document.getElementById("loading").style.display = "none";

    }

}

// =========================
// Logout
// =========================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("selectedGroupId");

    window.location.href = "login.html";

}