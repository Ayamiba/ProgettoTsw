<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Elimina Prodotto</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css">
</head>

<body>
    <%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <section class="contenuti-admin">
        <div class="admin-header">
            <h1>Pannello Gestione Admin – Rimuovi Prodotto</h1>
        </div>

        <%-- Notifica per l'esito dell'operazione (successo o errore) --%>
        <% if(request.getParameter("messaggio") != null) { %>
            <div class="alert-success" style="background-color: #fde8e8; color: #e53e3e; border-left: 4px solid #e53e3e; padding: 15px; border-radius: 6px; margin-bottom: 20px; font-family: 'Inter', sans-serif; font-weight: 600;">
                ⚠️ <%= request.getParameter("messaggio") %>
            </div>
        <% } %>
        
        <section class="corpo-admin">
            <div class="suggerimenti-immagine" style="border-left-color: #dc3545;">
                <h3 style="font-size: 1.1em; margin-bottom: 8px; color: #dc3545; font-weight: 500;">Avvertenze</h3>
                <p>• L'inserimento dell'ID comporterà la rimozione definitiva del prodotto dal catalogo.</p>
                <p>• Prima di procedere, verifica l'ID corretto all'interno della tabella dei prodotti per evitare errori.</p>
            </div>
            
            <form action="EliminaProdottoServlet" method="POST" onsubmit="return confirm('Sei sicuro di voler eliminare definitivamente questo prodotto?');">
                
                <div class="form-group">
                    <label for="idProdotto">ID Prodotto da cancellare</label>
                    <input type="number" id="idProdotto" name="idProdotto" class="form-input" placeholder="Inserisci l'ID numerico (Es. 5)" required>
                </div>

                <button type="submit" class="btn-admin-submit" style="background: #dc3545; box-shadow: 0 4px 12px rgba(220, 53, 69, 0.2);">
                    Elimina dal Database
                </button>
            </form>
        </section>
    </section>

    <%@ include file="/WEB-INF/views/components/footer.jsp" %>
</body>

</html>