const API_URL = "http://localhost:8096";

const token = localStorage.getItem("token");

let allGroups = [];

if (!token) {
    window.location.href = "login.html";
}

window.onload = function () {
    loadGroups();
};

// =========================
// Load Groups
// =========================

function loadGroups() {

    fetch(API_URL + "/groups", {

        method: "GET",

        headers: {
            "Authorization": "Bearer " + token
        }

    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Failed to load groups");
        }

        return response.json();

    })

	.then(data => {

	    console.log(data);

	    allGroups = data;

	    displayGroups(allGroups);

	    console.log(document.getElementById("groupContainer").innerHTML);

	})

    .catch(error => {

        console.log(error);

        alert("Unable to load groups.");

    });

}

// =========================
// Display Groups
// =========================

function displayGroups(groups) {

    let html = "";

    if (groups.length === 0) {

        html = `
            <div class="card">
                <h3>No Groups Found</h3>
                <p>Create your first group.</p>
            </div>
        `;

    } else {

        groups.forEach(group => {

            html += `

            <div class="group-card">

                <h4>${group.groupName}</h4>

                <p>${group.description}</p>

                <small>
                    <strong>Created By:</strong> ${group.createdBy}
                </small>

                <div class="group-actions">

                    <button
                        class="btn-primary"
                        onclick="viewGroup(${group.id})">

                        View

                    </button>

                    <button
                        class="btn-danger"
                        onclick="deleteGroup(${group.id})">

                        Delete

                    </button>

                </div>

            </div>

            `;

        });

    }

    document.getElementById("groupContainer").innerHTML = html;

}

// =========================
// Search Groups
// =========================

function searchGroup() {

    const keyword = document
        .getElementById("searchGroup")
        .value
        .toLowerCase();

		const filteredGroups = allGroups.filter(group =>

		    group.groupName.toLowerCase().includes(keyword) ||

		    group.description.toLowerCase().includes(keyword) ||

		    group.createdBy.toLowerCase().includes(keyword)

		);

    displayGroups(filteredGroups);

}

// =========================
// View Group
// =========================

function viewGroup(id) {

    localStorage.setItem("selectedGroupId", id);

    window.location.href = "group-members.html";

}

// =========================
// Delete Group
// =========================

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

            const message = await response.text();

            alert(message);

        }

    }

    catch (error) {

        console.log(error);

        alert("Unable to delete group.");

    }

}