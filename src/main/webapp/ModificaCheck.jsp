<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Area Professionista: Gestione Tracce</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css"> <!-- uso lo stesso css di admin -->
</head>

<body>
    <%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <section class="contenuti-admin">
        <div class="admin-header">
            <h1>Pannello di Controllo Professionista</h1>
        </div>

        <%-- Notifica per l'esito dell'operazione (successo o errore) --%>
        <% 
            String messaggio = request.getParameter("messaggio");
            if (messaggio != null && !messaggio.isEmpty()) { 
        %>
            <div class="alert-success" style="background-color: #e2f0d9; color: #385723; border-left: 4px solid #70ad47; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-family: 'Inter', sans-serif; font-weight: 600;">
                ✨ <%= messaggio %>
            </div>
        <% } %>
        
        <section class="corpo-admin">
            <div class="suggerimenti-immagine" style="border-left-color: #6A32E8;">
                <h3 style="font-size: 1.1em; margin-bottom: 8px; color: #6A32E8; font-weight: 500;">Istruzioni</h3>
                <p>• Inserisci l'ID della traccia di cui desideri approvare la presa in carico.</p>
                <p>• L'utente saprà che quest'ultima è stata presa in carico</p>
            </div>
            
            <form action="CheckServlet" method="POST">
                
                <div class="form-group">
                    <label for="idTracciaInput">ID Traccia Audio</label>
                    <input type="number" id="idTracciaInput" name="idTraccia" class="form-input" placeholder="Inserisci l'ID della traccia (Es. 12)" required>
                </div>

                <div style="display: flex; gap: 15px; margin-top: 25px;">
                    
                    <button type="submit" name="nuovoCheck" value="true" class="btn-admin-submit" style="margin-top: 0; background: #28a745; box-shadow: 0 4px 12px rgba(40, 167, 69, 0.2); flex: 1;">
                        Approva
                    </button>
                    
                    <button type="submit" name="nuovoCheck" value="false" class="btn-admin-submit" style="margin-top: 0; background: #dc3545; box-shadow: 0 4px 12px rgba(220, 53, 69, 0.2); flex: 1;">
                        Disapprova
                    </button>
                    
                </div>
            </form>
        </section>
    </section>

    <%@ include file="/WEB-INF/views/components/footer.jsp" %>
</body>

</html>