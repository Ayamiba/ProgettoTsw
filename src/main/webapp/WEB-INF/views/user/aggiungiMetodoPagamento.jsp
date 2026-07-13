<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
    String errore = (String) request.getAttribute("errore");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Aggiungi Metodo di Pagamento</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
    <script src="<%= request.getContextPath() %>/js/MetodoPagamento.js" defer></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dash-settings-container">
        <div class="dash-settings-card" style="max-width: 480px;">
            <h2>Metodo di Pagamento</h2>
            <p class="subtitle">Inserisci i dati di una carta di credito o debito valida.</p>

            <% if (errore != null) { %>
                <div class="auth-error" style="margin-bottom: 20px;"><%= errore %></div>
            <% } %>

            <form action="MetodiPagamentoServlet" method="POST" id="paymentForm">
                
                <div class="dash-form-group">
                    <label for="intestatario">Titolare della Carta</label>
                    <input type="text" id="intestatario" name="intestatario" class="dash-input" placeholder="Mario Rossi" required>
                </div>

                <div class="dash-form-group">
                    <label for="numeroCarta">Numero della Carta</label>
                    <input type="text" id="numeroCarta" name="numeroCarta" class="dash-input" 
                           placeholder="1234-5678-1234-5678" pattern="\d{4}-\d{4}-\d{4}-\d{4}" maxlength="19" 
                           title="Inserisci le 16 cifre del numero di carta." required>
                </div>

                <div class="dash-form-row">
                    <div>
                        <label for="scadenza">Scadenza (MM/AA)</label>
                        <input type="text" id="scadenza" name="scadenza" class="dash-input" 
                               placeholder="12/29" pattern="(0[1-9]|1[0-2])\/[0-9]{2}" maxlength="5" 
                               title="Inserisci la data nel formato MM/AA (es. 05/28)" required>
                    </div>
                    <div>
                        <label for="cvv">CVV / CVC</label>
                        <input type="password" id="cvv" name="cvv" class="dash-input" 
                               placeholder="***" pattern="\d{3}" maxlength="3" 
                               title="Il codice di 3 cifre sul retro della carta." required>
                    </div>
                </div>

                <div class="dash-buttons-row">
                    <a href="ProfiloServlet" class="dash-btn-cancel">Annulla</a>
                    <button type="submit" class="dash-btn-save">Salva Carta</button>
                </div>
                
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>