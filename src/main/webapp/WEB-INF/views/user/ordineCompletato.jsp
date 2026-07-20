<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Recuperiamo l'ID passato dalla Servlet
    String idOrdine = (String) request.getAttribute("idOrdine");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ordine Completato - Sændwave</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/checkout.css">
    
</head>
<body>
    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main>
        <div class="success-container">
            <!-- Icona spunta verde animata -->
            <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="success-icon">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
            </svg>
            
            <h1 class="success-title">Ordine Completato!</h1>
            <p class="success-desc">
                Grazie per aver scelto Sændwave. Abbiamo preso in carico la tua traccia e i nostri professionisti si metteranno presto all'opera.
            </p>
            
            <div class="order-number">
                Codice Ordine: #<%= idOrdine %>
            </div>
            
            <div class="action-buttons">
                <!-- Questo punterà alla Servlet iText per generare il PDF -->
                <a href="GeneraFatturaServlet?id=<%= idOrdine %>" class="dash-btn-save" style="text-decoration: none; display: inline-flex; align-items: center; gap: 8px;">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
                    Scarica Ricevuta PDF
                </a>
                <a href="ProfiloServlet" class="dash-btn-cancel" style="text-decoration: none; display: inline-flex; align-items: center; justify-content: center;">
                    Vai al Profilo
                </a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />
</body>
</html>