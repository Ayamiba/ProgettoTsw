<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Modifica Prodotto</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css">
</head>

<body>
    <%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <section class="contenuti-admin">
        <div class="admin-header">
            <h1>Pannello Gestione Admin – Modifica Prodotto</h1>
        </div>

        <%-- Gestione dinamica dei messaggi di successo o errore --%>
       <%-- Gestione dinamica dei messaggi di successo o errore (Sincronizzata con la Servlet) --%>
        <% 
            String msg = request.getParameter("messaggio");
            if (msg != null) { 
                // Se il messaggio inizia con "errore", mostriamo il banner rosso di allerta
                if (msg.startsWith("errore")) {
        %>
                <div class="alert-success" style="background-color: #fde8e8; color: #e53e3e; border-left: 4px solid #e53e3e; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-family: 'Inter', sans-serif; font-weight: 600;">
                    ⚠️ <%= msg %>
                </div>
        <% 
                } else {
                // nel caso "Prodotto aggiornato con successo!"), mostriamo il banner verde
        %>
                <div class="alert-success" style="background-color: #e2f0d9; color: #385723; border-left: 4px solid #70ad47; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-family: 'Inter', sans-serif; font-weight: 600;">
                    ✨ <%= msg %>
                </div>
        <% 
                }
            } 
        %>
        
        <section class="corpo-admin">
            <div class="suggerimenti-immagine" style="border-left-color: #007bff;">
                <h3 style="font-size: 1.1em; margin-bottom: 8px; color: #007bff; font-weight: 500;">Aggiornamento Dati Catalogo</h3>
                <p>• Inserisci l'ID del prodotto esistente per sovrascrivere le sue informazioni nel database.</p>
                <p>• Se non selezioni una nuova immagine di copertina, il sistema manterrà automaticamente quella già presente.</p>
            </div>
            
            <form action="ModificaProdottoServlet" method="POST" enctype="multipart/form-data">
                
                <div class="form-group">
                    <label for="idProdotto">ID del Prodotto da Modificare (Esistente nel DB)</label>
                    <input type="number" id="idProdotto" name="idProdotto" class="form-input" placeholder="Es. 1" required>
                </div>

                <div class="form-group">
                    <label for="nome">Nuovo Nome Prodotto</label>
                    <input type="text" id="nome" name="nome" class="form-input" placeholder="Inserisci il nuovo nome commerciale" required>
                </div>

                <div class="form-group">
                    <label for="prezzo">Nuovo Prezzo di Vendita (€)</label>
                    <input type="number" id="prezzo" name="prezzo" class="form-input" step="0.01" min="0" placeholder="Es. 49.99" required>
                </div>

                <div class="form-group">
                    <label for="descrizione">Nuova Descrizione</label>
                    <textarea id="descrizione" name="descrizione" class="form-input" placeholder="Aggiorna le specifiche tecniche o i dettagli del plugin..."></textarea>
                </div>

                <div class="form-group">
                    <label for="foto">Nuova Copertina Prodotto (Opzionale)</label>
                    <div class="file-upload-wrapper">
                        <input type="file" id="foto" name="foto" class="form-input" accept="image/*">
                    </div>
                </div>

                <button type="submit" class="btn-admin-submit" style="background: #007bff; box-shadow: 0 4px 12px rgba(0, 123, 255, 0.2);">
                    Salva Modifiche nel DB
                </button>
            </form>
        </section>
    </section>

    <%@ include file="/WEB-INF/views/components/footer.jsp" %>
</body>

</html>