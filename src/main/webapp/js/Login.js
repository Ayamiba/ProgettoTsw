document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const submitButton = form.querySelector('button[type="submit"]');

    const emailError = document.getElementById('error-message');
    const passwordError = document.getElementById('error-message');

    const emailLabel = document.getElementById('labelEmail');
    const passwordLabel = document.getElementById('labelPassword');


    function validateEmail() { // Controlla la validità dell'email inserita dall'utente
        const email = emailInput.value.trim();
        const emailPattern = /^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/;

        if (email === '' || !emailPattern.test(email)) {
            return false;
        } else {
            return true;
        }
    }

    function validatePassword() { //Controlla la validità della password inserita dall'utente
        const password = passwordInput.value;
        const minLength = 8;

        if (password === '' || password.length < minLength) {
            return false;
        } else {
            return true;
        }
    }

    function updateSubmitButtonState() { // Evita di cliccare Submit se i parametri di Email e Password non sono corretti
        const isFormValid =
            validateEmail() &&
            validatePassword();

        submitButton.disabled = !isFormValid;
    }

    submitButton.disabled = true;

    emailInput.addEventListener('input', () => {
        validateEmail();
        updateSubmitButtonState();
    });

    passwordInput.addEventListener('input', () => {
        validatePassword();
        updateSubmitButtonState();
    });

    validateEmail();
    validatePassword();
    updateSubmitButtonState();

    form.addEventListener('submit', function(event) {
        if (!(
            validateEmail() &&
            validatePassword()
        )) {
            event.preventDefault();
        }
    });
});