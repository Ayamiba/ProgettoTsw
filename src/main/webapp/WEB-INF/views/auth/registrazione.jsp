<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String errore = (String) request.getAttribute("errore");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Crea un Account</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/loginRegistrazione.css">
    <script src="<%= request.getContextPath() %>/js/Registrazione.js"></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="auth-page-container">
        <div class="auth-card" style="max-width: 550px;"> <h2>Crea il tuo Account</h2>
            <p class="subtitle">Unisciti a Sændwave per elaborare le tue tracce.</p>

            <% if (errore != null) { %>
                <div class="auth-error"><%= errore %></div>
            <% } %>

            <form id="registrationForm" action="RegistrazioneServlet" method="POST" class="auth-form">
                
                <div style="display: flex; gap: 15px;">
                    <div style="flex: 1; display: flex; flex-direction: column; gap: 15px;">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" class="auth-input" placeholder="Mario" required>
                    </div>
                    <div style="flex: 1; display: flex; flex-direction: column; gap: 15px;">
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" class="auth-input" placeholder="Rossi" required>
                    </div>
                </div>

                <label id="emailLabel" for="email">Indirizzo Email</label>
                <input type="email" id="email" name="email" class="auth-input" placeholder="mario.rossi@email.it" required>
                <div id="email-error" style="color: #c62828; font-size: 0.85em; font-weight: bold; margin-top: -10px; display: none;"></div>

                <label id="passwordLabel" for="password">Password</label>
                <input type="password" id="password" name="password" class="auth-input" placeholder="Scegli una password sicura" minlength="8" required>
                <div id="password-error" style="color: #c62828; font-size: 0.85em; font-weight: bold; margin-top: -10px; display: none;"></div>

                <label for="data_nascita">Data di Nascita</label>
                <input type="date" id="data_nascita" name="data_nascita" class="auth-input" required>

                <button type="submit" class="auth-btn">Registrati</button>
            </form>

            <div class="auth-switch">
                Hai già un account? <a href="LoginServlet">Accedi qui</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>