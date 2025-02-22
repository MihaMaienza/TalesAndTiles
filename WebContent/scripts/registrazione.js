const wrapper = document.querySelector('.wrapper');
const loginLink = document.querySelector('.login-link');
const registerLink = document.querySelector('.register-link');
const btnPopup = document.querySelector('.btnLogin-popup');
const iconClose = document.querySelector('.icon-close');
const overlay = document.getElementById('overlay');
const registerForm = document.getElementById('registerForm');
const loginForm = document.querySelector('.form-box.login form');

registerLink.addEventListener('click', () => {
    wrapper.classList.add('active');
});

loginLink.addEventListener('click', () => {
    wrapper.classList.remove('active');
}); 

btnPopup.addEventListener('click', () => {
    wrapper.classList.add('active-popup');
    overlay.classList.add('active');
    document.body.classList.add('no-scroll'); // Aggiungi la classe no-scroll
});

iconClose.addEventListener('click', () => {
    wrapper.classList.remove('active-popup');
    wrapper.classList.remove('active');
    overlay.classList.remove('active');
    document.body.classList.remove('no-scroll'); // Rimuovi la classe no-scroll
    resetForms();
});

function resetForms() {
    registerForm.reset();
    loginForm.reset();
    document.getElementById("usernameError").style.display = "none";
    document.getElementById("emailError").style.display = "none";
    document.getElementById("passwordError").style.display = "none";
    const loginError = document.querySelector(".error-message");
    if (loginError) {
        loginError.style.display = "none";
    }
}

// Mantieni il popup aperto sulla sezione di login in caso di errore
document.addEventListener('DOMContentLoaded', () => {
    const loginError = document.querySelector(".error-message");
    if (loginError) {
        wrapper.classList.add('active-popup');
        overlay.classList.add('active');
        document.body.classList.add('no-scroll'); // Aggiungi la classe no-scroll
        wrapper.classList.remove('active'); // Assicurati che il form di login sia attivo
    }
});

// Controlli su email, username e password
document.getElementById("registerForm").addEventListener("submit", function(event) {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const usernameError = document.getElementById("usernameError");
    const emailError = document.getElementById("emailError");
    const passwordError = document.getElementById("passwordError");

    let valid = true;

    // Check if username meets pattern requirements
    const usernamePattern = /^[a-zA-Z0-9]{1,29}$/; // solo lettere e numeri consentiti
    if (!usernamePattern.test(username)) {
        usernameError.textContent = "Invalid username format";
        usernameError.style.display = "block";
        valid = false;
    } else {
        usernameError.style.display = "none";
    }

    // Check if email meets pattern requirements
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,39}$/;
    if (!emailPattern.test(email)) {
        emailError.textContent = "Invalid email format";
        emailError.style.display = "block";
        valid = false;
    } else {
        emailError.style.display = "none";
    }

    // Check if password meets pattern requirements
    const passwordPattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&]{8,63}(?!.*\s)$/; // minimo 8 caratteri, almeno 1 numero, miuscola e carattere speciale. spazio escluso
    if (!passwordPattern.test(password)) {
        passwordError.textContent = "Invalid password format";
        passwordError.style.display = "block";
        valid = false;
    } else {
        passwordError.style.display = "none";
    }

    if (!valid) {
        event.preventDefault();
        return;
    }
});
