const API_URL = "https://splitshare-vfh9.onrender.com";

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

function createGroup(){

    const groupName = document.getElementById("groupName").value.trim();

    const description = document.getElementById("description").value.trim();

    if(groupName===""){

        alert("Please enter Group Name");

        return;

    }

    fetch(API_URL+"/groups",{

        method:"POST",

        headers:{

            "Content-Type":"application/json",

            "Authorization":"Bearer "+token

        },

        body:JSON.stringify({

            groupName:groupName,

            description:description

        })

    })

    .then(response=>{

        if(!response.ok){

            throw new Error("Failed");

        }

        return response.json();

    })

    .then(data=>{

        alert("✅ Group Created Successfully");

        window.location="groups.html";

    })

    .catch(error=>{

        alert("Unable to create group.");

    });

}