<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Recupera l'eventuale messaggio di errore dalla Servlet
    String errore = (String) request.getAttribute("errore");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Accedi</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/loginRegistrazione.css">
	<script src="<%= request.getContextPath() %>/js/Login.js"></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="auth-page-container">
        <div class="auth-card">
            <h2>Bentornato</h2>
            <p class="subtitle">Accedi </p>

            <% if (errore != null) { %>
                <div class="auth-error"><%= errore %></div>
            <% } %>

           <form id="loginForm" action="LoginServlet" method="POST" class="auth-form">
                
                <label id="labelEmail" for="email">Indirizzo Email</label>
                <input type="email" id="email" name="email" class="auth-input" placeholder="mario.rossi@email.it" required>

                <label id="labelPassword" for="password">Password</label>
                <input type="password" id="password" name="password" class="auth-input" placeholder="Inserisci la tua password" required>

                <div id="error-message" style="color: #c62828; font-size: 0.85em; font-weight: bold; margin-top: -10px; display: none;"></div>

                <button type="submit" class="auth-btn">Accedi</button>
            </form>

            <div class="auth-switch">
                Non hai ancora un account? <a href="RegistrazioneServlet">Registrati ora</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>