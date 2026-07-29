<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Contattaci - Sændwave</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/contatti.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="elementi-principali" style="max-width: 1000px; margin: 40px auto; padding: 0 20px;">
        
        <!-- Hero Box in stile About Us -->
        <section class="about-hero">
            <h1>Contattaci</h1>
            <p>Hai domande sui nostri servizi o bisogno di assistenza tecnica? Siamo qui per aiutarti.</p>
        </section>

        <!-- Se la Servlet invia un messaggio di successo, lo mostriamo qui -->
        <% if (request.getAttribute("successo") != null) { %>
            <div class="alert-success">
                <%= request.getAttribute("successo") %>
            </div>
        <% } %>

        <!-- Griglia delle Card Email in stile About Us -->
        <div class="contatti-grid">
            
            <!-- Card Supporto Generale -->
            <div class="contatti-card">
                <div class="card-badge">Generale</div>
                <h3>Supporto Generale</h3>
                <p>Per informazioni relative alla piattaforma, assistenza sugli ordini e richieste generiche.</p>
                <div class="email-box">
                    <a href="mailto:info@saendwave.com">info@saendwave.com</a>
                </div>
            </div>

            <!-- Card Email Admin -->
            <div class="contatti-card">
                <div class="card-badge admin-badge">Team Admin</div>
                <h3>Email degli Admin</h3>
                <p>Contatta direttamente uno degli amministratori per questioni tecniche o specifiche:</p>
                
                <div class="admin-list">
                    <div class="admin-item">
                        <span>Admin 1:</span>
                        <a href="mailto:m.ambrosio56@studenti.unisa.it">m.ambrosio56@studenti.unisa.it</a>
                    </div>
                    <div class="admin-item">
                        <span>Admin 2:</span>
                        <a href="mailto:g.varricchio1@studenti.unisa.it">g.varricchio1@studenti.unisa.it</a>
                    </div>
                    <div class="admin-item">
                        <span>Admin 3:</span>
                        <a href="mailto:m.dellasala12@studenti.unisa.it">m.dellasala12@studenti.unisa.it</a>
                    </div>
                </div>
            </div>

        </div>

    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>