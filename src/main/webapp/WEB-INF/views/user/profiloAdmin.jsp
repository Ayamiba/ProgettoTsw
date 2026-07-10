<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% UtenteBean utente = (UtenteBean) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Sændwave - Pannello di Amministrazione</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />
    <main class="dashboard-container">
        <h1 style="color: #d9534f;">Pannello di Amministrazione</h1>
        
        <div class="dashboard-grid">
            <div class="dash-card card-admin">
                <h3>Gestione Catalogo</h3>
                <button class="btn">Aggiungi Nuovo Plugin</button>
            </div>
            <div class="dash-card card-admin">
                <h3>Gestione Utenti e Staff</h3>
                <p>Elenco utenti registrati...</p>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>