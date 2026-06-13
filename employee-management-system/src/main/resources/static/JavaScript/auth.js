document.addEventListener("DOMContentLoaded", function(){

const form = document.getElementById("loginForm");
const username = document.getElementById("username");
const password = document.getElementById("password");
const errorMsg = document.getElementById("errorMsg");

form.addEventListener("submit", function(e){

if(username.value.trim()==="" || password.value.trim()===""){

e.preventDefault();

errorMsg.style.display="block";
errorMsg.innerText="Please enter username and password";

}

});

});

function togglePassword(){

const passwordField = document.getElementById("password");

if(passwordField.type==="password"){

passwordField.type="text";

}else{

passwordField.type="password";

}

}