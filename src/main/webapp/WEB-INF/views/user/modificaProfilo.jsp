<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% 
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
    String errore = (String) request.getAttribute("errore");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Sændwave – Modifica Dati Personali</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dash-settings-container">
        <div class="dash-settings-card">
            <h2>Modifica Anagrafica</h2>
            <p class="subtitle">Aggiorna le tue informazioni personali.</p>

            <% if (errore != null) { %>
                <div class="auth-error" style="margin-bottom: 20px;"><%= errore %></div>
            <% } %>

            <form action="ModificaProfiloServlet" method="POST">
                
                <div class="dash-form-row">
                    <div>
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" class="dash-input" value="<%= utente.getNome() %>" required>
                    </div>
                    <div>
                        <label for="cognome">Cognome</label>
                        <input type="text" id="cognome" name="cognome" class="dash-input" value="<%= utente.getCognome() %>" required>
                    </div>
                </div>

                <div class="dash-form-group">
                    <label for="email">Indirizzo Email (Non modificabile)</label>
                    <input type="email" id="email" class="dash-input readonly" value="<%= utente.getEmail() %>" readonly title="L'email non può essere modificata in quanto identificativo univoco dell'account.">
                </div>

                <div class="dash-form-group">
                    <label for="data_nascita">Data di Nascita</label>
                    <input type="date" id="data_nascita" name="data_nascita" class="dash-input" value="<%= (utente.getDataNascita() != null) ? utente.getDataNascita().toString() : "" %>" required>
                </div>

                <div class="dash-buttons-row">
                    <a href="ProfiloServlet" class="dash-btn-cancel">Annulla</a>
                    <button type="submit" class="dash-btn-save">Salva Modifiche</button>
                </div>
                
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>