document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const submitButton = form.querySelector('button[type="submit"]');

 
    const emailError = document.getElementById('email-error'); 
    const passwordError = document.getElementById('password-error');

    const emailLabel = document.getElementById('emailLabel');
    const passwordLabel = document.getElementById('passwordLabel');
    
    let emailCheckTimeout = null;
    let isEmailAvailable = false;

    // --- FUNZIONI DI UTILITÀ ---
    function showError(element, message, errorDiv, labelElement) {
        element.classList.remove('input-success');
        element.classList.add('input-error');
        if (labelElement) {
            labelElement.classList.remove('label-success');
            labelElement.classList.add('label-error');
        }
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }

    function showSuccess(element, labelElement) {
        element.classList.remove('input-error');
        element.classList.add('input-success');
        if (labelElement) {
            labelElement.classList.remove('label-error');
            labelElement.classList.add('label-success');
        }
    }

    function hideError(element, errorDiv, labelElement) {
        element.classList.remove('input-error');
        if (labelElement) {
            labelElement.classList.remove('label-error');
            labelElement.classList.remove('label-success');
        }
        errorDiv.textContent = '';
        errorDiv.style.display = 'none';
    }

    // --- VALIDAZIONE EMAIL (REGEX) ---
    function validateEmail(callback) {
        const email = emailInput.value.trim();
        // Regex standard per email
        const emailRegex = /^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/;

        if (email === '') {
            showError(emailInput, 'L\'email non può essere vuota.', emailError, emailLabel);
            if (callback) callback(false);
            return false;
        } else if (!emailRegex.test(email)) {
            showError(emailInput, 'Formato email non valido.', emailError, emailLabel);
            if (callback) callback(false);
            return false;
        } else {
            clearTimeout(emailCheckTimeout);
            emailCheckTimeout = setTimeout(() => {
                fetch(`./CheckEmailServlet?email=${encodeURIComponent(email)}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.error || !data.available) {
                            showError(emailInput, data.error || 'Email già registrata.', emailError, emailLabel);
                            isEmailAvailable = false;
                        } else {
                            showSuccess(emailInput, emailLabel);
                            hideError(emailInput, emailError, emailLabel);
                            isEmailAvailable = true;
                        }
                        updateSubmitButtonState();
                        if (callback) callback(isEmailAvailable);
                    })
                    .catch(err => {
                        showError(emailInput, 'Errore di connessione.', emailError, emailLabel);
                        isEmailAvailable = false;
                        updateSubmitButtonState();
                    });
            }, 500);
            return true;
        }
    }

    // --- VALIDAZIONE PASSWORD (REGEX) ---
    function validatePassword() {
        const password = passwordInput.value;
        // Regex: min 8 caratteri, almeno una lettera, almeno un numero
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;

        if (password === '') {
            showError(passwordInput, 'La password è obbligatoria.', passwordError, passwordLabel);
            return false;
        } else if (!passwordRegex.test(password)) {
            showError(passwordInput, 'Min 8 caratteri, almeno una lettera e un numero.', passwordError, passwordLabel);
            return false;
        } else {
            showSuccess(passwordInput, passwordLabel);
            hideError(passwordInput, passwordError, passwordLabel);
            return true;
        }
    }

    function updateSubmitButtonState() {
        const isPasswordValid = validatePassword();
        submitButton.disabled = !(isPasswordValid && isEmailAvailable);
    }

    // Event Listeners
    emailInput.addEventListener('input', () => validateEmail());
    passwordInput.addEventListener('input', () => updateSubmitButtonState());

    form.addEventListener('submit', function(event) {
        if (!isEmailAvailable || !validatePassword()) {
            event.preventDefault();
        }
    });
});