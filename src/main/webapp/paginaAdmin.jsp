<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%
    // Recuperiamo l'utente loggato dalla sessione
    UtenteBean utente = (UtenteBean) session.getAttribute("user");
    
    // Controllo di sicurezza: se l'utente non è loggato, reindirizza alla home o login
    if (utente == null || utente.getTipo() == null) {
        response.sendRedirect("login.jsp"); // Sostituisci con la tua pagina o Servlet di login/errore
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Profilo Admin</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css?v=2">
    
</head>
<body>

    <%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <div class="profile-layout">
        
        <aside class="profile-sidebar">
            <div class="user-avatar-container">
                <h2><%= utente.getNome() %> <%= utente.getCognome() %></h2>
                <span class="role-badge"><%= utente.getTipo().toUpperCase() %></span>
            </div>
            
            <div class="user-details-list">
                <div class="detail-item">
                    <label>Nome</label>
                    <p><%= utente.getNome() %></p>
                </div>
                <div class="detail-item">
                    <label>Cognome</label>
                    <p><%= utente.getCognome() %></p>
                </div>
                <div class="detail-item">
                    <label>Email</label>
                    <p><%= utente.getEmail() %></p>
                </div>
                <div class="detail-item">
                    <label>Data di Nascita</label>
                    <p><%= utente.getDataNascita() != null ? utente.getDataNascita() : "Non specificata" %></p>
                </div>
                <div class="detail-item">
                    <label>Tipo Account</label>
                    <p><%= utente.getTipo() %></p>
                </div>
            </div>
        </aside>

        <main class="profile-main-content">
            <div class="admin-welcome-header">
                <h1>Pannello di Amministrazione</h1>
                <p>Benvenuto nella pagina admin.</p>
            </div>

            <div class="admin-actions-grid">
                
                <a href="AggiungiProdottoServlet" class="action-card card-add">
                    <h3>Aggiungi Prodotto</h3>
                    <p>Inserisci un nuovo prodotto al catalogo compilando il form.</p>
                </a>

                <a href="ModificaProdottoServlet?azione=modifica" class="action-card card-edit">
                    <h3>Modifica Prodotto</h3>
                    <p>Aggiorna i prezzi, sostituisci i file delle immagini o edita le descrizioni dei prodotti esistenti.</p>
                </a>

                <a href="VisualizzaOrdiniAdminServlet?azione=elimina" class="action-card card-delete">
                    <h3>Elimina Prodotto</h3>
                    <p>Rimuovi in maniera permanente o disattiva un articolo per interromperne la vendita.</p>
                </a>
                
            </div>
        </main>
        
    </div>

    <%@ include file="/WEB-INF/views/components/footer.jsp" %>

</body>
</html>