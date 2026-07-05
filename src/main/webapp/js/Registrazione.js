document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const submitButton = form.querySelector('button[type="submit"]');

    const emailError = document.getElementById('error-message');
    const passwordError = document.getElementById('error-message');

    const emailLabel = document.getElementById('emailLabel');
    const passwordLabel = document.getElementById('passwordLabel');
    let emailCheckTimeout = null;

    function showError(element, message, errorDiv, labelElement) { // Gestisce gli errori all'interno del form
        element.classList.remove('input-success');
        element.classList.add('input-error');
        if (labelElement) {
            labelElement.classList.remove('label-success');
            labelElement.classList.add('label-error');
        }
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }

    function showSuccess(element, labelElement) { // Gestisce il "successo" all'interno del form
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

    function validateEmail(callback) { // Validazione email in base al Pattern
        const email = emailInput.value.trim();
        const emailPattern = /^[\w!#$%&'*+/=?`{|}~^-]+(?:\.[\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,6}$/;

        if (email === '') { // Controlla che L'email non sia vuota
            showError(emailInput, 'L\'email non può essere vuota.', emailError, emailLabel);
            if (callback) callback(false);
            return false;
        } else if (!emailPattern.test(email)) { // Controlla il pattern dell'email
            showError(emailInput, 'Formato email non valido.', emailError, emailLabel);
            if (callback) callback(false);
            return false;
        } else {
            showSuccess(emailInput, emailLabel);
            hideError(emailInput, emailError, emailLabel);

            clearTimeout(emailCheckTimeout);
            emailCheckTimeout = setTimeout(() => {
                fetch(`./CheckEmailServlet?email=${encodeURIComponent(email)}`) // Verifica se l'email è valida e non presente gia nel DB
                    .then(response => {
                        if (!response.ok) { // Gestione errore di connessione
                            throw new Error(`Errore HTTP! Stato: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.error) { // Gestione errore logico (es. formato della mail) 
                            showError(emailInput, data.error, emailError, emailLabel);
                            if (callback) callback(false);
                        } else if (!data.available) { // Gestione errore in caso di email gia presente nel DB
                            showError(emailInput, 'Questa email è già registrata.', emailError, emailLabel);
                            if (callback) callback(false);
                        } else { // Non ci sono errori quindi "Success"
                            showSuccess(emailInput, emailLabel);
                            hideError(emailInput, emailError, emailLabel);
                            if (callback) callback(true);
                        }
                        updateSubmitButtonState();
                    })
                    .catch(error => { // Gestione di errori come perdita di connessione
                        console.error('Errore durante la verifica email:', error);
                        showError(emailInput, 'Errore di connessione o del server. Riprova.', emailError, emailLabel);
                        if (callback) callback(false);
                        updateSubmitButtonState();
                    });
            }, 500);

            return true;
        }
    }

    function validatePassword() { // Validazione della password
        const password = passwordInput.value;
        const minLength = 8;
        const passwordPattern = /^.{8,}$/;

        if (password === '') { // Gestione password vuota
            showError(passwordInput, 'La password non può essere vuota.', passwordError, passwordLabel);
            return false;
        } else if (password.length < minLength) { // Gestione password corta
            showError(passwordInput, `La password deve essere di almeno ${minLength} caratteri.`, passwordError, passwordLabel);
            return false;
        } else if (!passwordPattern.test(password)) { // Gestione pattern password
            showError(passwordInput, `La password deve essere di almeno ${minLength} caratteri.`, passwordError, passwordLabel);
            return false;
        } else {
            showSuccess(passwordInput, passwordLabel);
            hideError(passwordInput, passwordError, passwordLabel);
            return true;
        }
    }

    let isEmailAvailable = false;

    function updateSubmitButtonState() {
        const isFormValidExcludingEmail = validatePassword();

        submitButton.disabled = !(isFormValidExcludingEmail && isEmailAvailable);
    }

    submitButton.disabled = true;

    emailInput.addEventListener('input', () => {
        validateEmail((isValid) => {
            isEmailAvailable = isValid;
            updateSubmitButtonState();
        });
    });
	
    passwordInput.addEventListener('input', () => {
        validatePassword();
        updateSubmitButtonState();
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const isSyncValid = validatePassword();

        if (isSyncValid) {
            validateEmail((emailIsValid) => {
                if (emailIsValid) {
                    form.submit();
                } else {
                    console.log('Email non valida o non disponibile. Submit bloccato.');
                    updateSubmitButtonState();
                }
            });
        } else {
            console.log('Validazione sincrona fallita. Submit bloccato.');
            updateSubmitButtonState();
        }
    });
});