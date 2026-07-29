<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
    String errore = (String) request.getAttribute("errore");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Modifica Password</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dash-settings-container">
        <div class="dash-settings-card" style="max-width: 450px;">
            <h2>Sicurezza Account</h2>
            <p class="subtitle">Scegli una password forte e sicura.</p>

            <% if (errore != null) { %>
                <div class="auth-error" style="margin-bottom: 20px;"><%= errore %></div>
            <% } %>

            <form action="CambiaPasswordServlet" method="POST">
                
                <div class="dash-form-group">
                    <label for="vecchiaPassword">Password Attuale</label>
                    <input type="password" id="vecchiaPassword" name="vecchiaPassword" class="dash-input" placeholder="Inserisci la password corrente" required>
                </div>

                <hr style="border: 0; border-top: 1px solid #f0f0f0; margin: 25px 0;">

                <div class="dash-form-group">
                    <label for="nuovaPassword">Nuova Password</label>
                    <input type="password" id="nuovaPassword" name="nuovaPassword" class="dash-input" placeholder="Minimo 8 caratteri" minlength="8" required>
                </div>

                <div class="dash-form-group">
                    <label for="confermaPassword">Conferma Nuova Password</label>
                    <input type="password" id="confermaPassword" name="confermaPassword" class="dash-input" placeholder="Ripeti la nuova password" minlength="8" required>
                </div>

                <div class="dash-buttons-row">
                    <a href="ProfiloServlet" class="dash-btn-cancel">Annulla</a>
                    <button type="submit" class="dash-btn-save">Aggiorna Password</button>
                </div>
                
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('form');
            const nuova = document.getElementById('nuovaPassword');
            const conferma = document.getElementById('confermaPassword');

            form.addEventListener('submit', function(event) {
                if (nuova.value !== conferma.value) {
                    event.preventDefault();
                    alert("Attenzione: La nuova password e la conferma non coincidono!");
                }
            });
        });
    </script>
</body>
</html>