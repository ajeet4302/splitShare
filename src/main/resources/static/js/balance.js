const API_URL = "https://splitshare-vfh9.onrender.com";
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

// =========================
// Load Groups
// =========================

async function loadGroups() {

    try {

        const response = await fetch(
            API_URL + "/groups",
            {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        const groups = await response.json();

        const select = document.getElementById("groupSelect");

        select.innerHTML =
            `<option value="">Select Group</option>`;

        groups.forEach(group => {

            select.innerHTML +=
            `
                <option value="${group.id}">
                    ${group.groupName}
                </option>
            `;

        });

    }
    catch(error){

        alert("Unable to load groups.");

    }

}

loadGroups();


// =========================
// Load Balance
// =========================

document
.getElementById("loadBalanceBtn")
.addEventListener("click", loadBalance);

async function loadBalance(){

    const groupId =
        document.getElementById("groupSelect").value;

    if(groupId===""){

        alert("Please select a group.");

        return;

    }

    try{

        const response = await fetch(

            `${API_URL}/expenses/balance/${groupId}`,

            {

                headers:{

                    "Authorization":"Bearer "+token

                }

            });

        const balances = await response.json();

        const table =
            document.getElementById("balanceTable");

        table.innerHTML = "";

        if(balances.length===0){

            table.innerHTML=
            `
            <tr>
                <td colspan="4" class="text-center">
                    No Balance Found
                </td>
            </tr>
            `;

            return;

        }

        balances.forEach(balance=>{

            let balanceColor =
                balance.balance >= 0
                ? "text-success"
                : "text-danger";

            table.innerHTML +=

            `
            <tr>

                <td>${balance.userName}</td>

                <td>₹ ${balance.paid}</td>

                <td>₹ ${balance.owes}</td>

                <td class="${balanceColor}">
                    ₹ ${balance.balance}
                </td>

            </tr>
            `;

        });

    }

    catch(error){

        alert("Unable to load balances.");

    }

}


// =========================
// Logout
// =========================

function logout(){

    localStorage.removeItem("token");

    window.location.href="login.html";

}