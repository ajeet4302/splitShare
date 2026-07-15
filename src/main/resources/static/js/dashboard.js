const API_URL = "http://localhost:8096";

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

window.onload = function () {

    loadUser();

    loadGroups();

    loadRecentExpenses();

};

function loadUser() {

    const userName = localStorage.getItem("userName");

    document.getElementById("username").innerHTML = userName;

    document.getElementById("topUserName").innerHTML = userName;

    document.querySelector(".avatar").innerHTML =
        userName.charAt(0).toUpperCase();

}

function loadGroups() {

    fetch(API_URL + "/groups", {

        method: "GET",

        headers: {

            "Authorization": "Bearer " + token

        }

    })

    .then(response => {

        if (!response.ok) {

            throw new Error("Unable to load groups");

        }

        return response.json();

    })

    .then(data => {

        document.getElementById("groupCount").innerHTML = data.length;

        let html = "";

        if (data.length === 0) {

            html = `
                <div class="card">
                    <h4>No Groups Found</h4>
                    <p>Create your first group to start tracking expenses.</p>
                </div>
            `;

        } else {

            data.forEach(group => {

                html += `

                <div class="group-card">

                    <h4>${group.groupName}</h4>

                    <p>${group.description}</p>

                    <small>
                        Created By : ${group.createdBy}
                    </small>

                    <div class="group-actions">

                        <button class="view-btn"
                                onclick="viewGroup(${group.id})">

                            View

                        </button>

                        <button class="delete-btn"
                                onclick="deleteGroup(${group.id})">

                            Delete

                        </button>

                    </div>

                </div>

                `;

            });

        }

        document.getElementById("groupContainer").innerHTML = html;

    })

    .catch(error => {

        console.log(error);

        alert("Unable to load groups.");

    });

}

function logout() {

    localStorage.clear();

    window.location.href = "login.html";

}

function viewGroup(id) {

    localStorage.setItem("selectedGroupId", id);

    window.location.href = "expenses.html";

}

async function deleteGroup(id) {

    const confirmDelete = confirm("Are you sure you want to delete this group?");

    if (!confirmDelete) {

        return;

    }

    try {

        const response = await fetch(`${API_URL}/groups/${id}`, {

            method: "DELETE",

            headers: {

                "Authorization": "Bearer " + token

            }

        });

        if (response.ok) {

            const message = await response.text();

            alert(message);

            loadGroups();

        } else {

            const errorMessage = await response.text();

            alert(errorMessage);

        }

    }

    catch (error) {

        console.log(error);

        alert("Unable to delete group.");

    }

}

async function loadRecentExpenses() {

    try {

        const groupResponse = await fetch(API_URL + "/groups", {

            headers: {
                "Authorization": "Bearer " + token
            }

        });

        const groups = await groupResponse.json();

        let allExpenses = [];

        for (const group of groups) {

            const expenseResponse = await fetch(

                API_URL + "/expenses/group/" + group.id,

                {

                    headers: {
                        "Authorization": "Bearer " + token
                    }

                });

            const expenses = await expenseResponse.json();

            allExpenses = allExpenses.concat(expenses);

        }

        document.getElementById("expenseCount").innerHTML =
            allExpenses.length;
			
			let totalAmount = 0;

			allExpenses.forEach(expense => {

			    totalAmount += Number(expense.amount);

			});

			document.getElementById("balanceAmount").innerHTML =
			    "₹" + totalAmount.toFixed(2);

        allExpenses.sort((a, b) =>
            new Date(b.expenseDate) - new Date(a.expenseDate));

        const table =
            document.getElementById("expenseTable");

        if (allExpenses.length === 0) {

            table.innerHTML = `

                <tr>

                    <td colspan="4" class="text-center">

                        No Expenses Found

                    </td>

                </tr>

            `;

            return;

        }

        table.innerHTML = "";

        allExpenses.slice(0,5).forEach(expense => {

            table.innerHTML += `

                <tr>

                    <td>${expense.title}</td>

                    <td>${expense.paidBy}</td>

                    <td>₹${expense.amount}</td>

                    <td>${expense.expenseDate}</td>

                </tr>

            `;

        });

    }

    catch(error){

        console.log(error);

    }

}
function showNotifications() {

    alert(
`Notifications

• Welcome to SplitShare 🎉

• Keep adding expenses regularly.

• Use Settlement to clear balances.

• Check your Profile for updated details.`
    );

}