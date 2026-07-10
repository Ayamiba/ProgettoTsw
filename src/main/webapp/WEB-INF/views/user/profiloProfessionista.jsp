<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% UtenteBean utente = (UtenteBean) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Sændwave - Dashboard Professionista</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />
    <main class="dashboard-container">
        <h1 style="color: #ecc94b;">Area Lavoro Professionisti</h1>
        <p class="subtitle">Bentornato in studio, <%= utente.getNome() %>. Ecco il lavoro in coda.</p>
        
        <div class="dashboard-grid">
            <div class="dash-card card-pro">
                <h3>Ordini in Sospeso (Da elaborare)</h3>
                <p>Nessuna traccia in attesa di elaborazione.</p>
            </div>
            <div class="dash-card card-pro">
                <h3>Consegna Lavoro</h3>
                <button class="btn">Carica Traccia Finita</button>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>