<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%
    // Recuperiamo il codice di stato HTTP (es. 403, 404, 500)
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
        statusCode = 500; // Default di sicurezza
    }

    String titolo = "Ops! Qualcosa è andato storto";
    String descrizione = "Si è verificato un errore inatteso nei nostri banchi di missaggio.";

    if (statusCode == 404) {
        titolo = "Pagina non trovata (404)";
        descrizione = "La traccia o la risorsa che stai cercando non esiste o è stata rimossa.";
    } else if (statusCode == 403) {
        titolo = "Accesso negato (403)";
        descrizione = "Non hai i permessi necessari per accedere a questa sezione.";
    } else if (statusCode == 500) {
        titolo = "Errore del server (500)";
        descrizione = "C'è stato un problema tecnico interno. Riprova più tardi.";
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= titolo %> – Sændwave</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="error-page-wrapper">
        <!-- Prima immagine in alto -->
        <img src="<%= request.getContextPath() %>/img/placeholder.png" alt="Logo Sændwave" class="error-img error-img-top" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/img/SWSoloScritta.png';">

        <h1 style="font-size: 4em; color: var(--colore-primario); margin-bottom: 15px;"><%= statusCode %></h1>
        <h2 style="margin-bottom: 15px;"><%= titolo %></h2>
        <p style="font-size: 1.1em; color: #555; margin-bottom: 30px; max-width: 500px;"><%= descrizione %></p>
        
        <a href="<%= request.getContextPath() %>/HomeServlet" class="btn-goToCart" style="display: inline-block; width: auto; padding: 12px 30px;">
            Torna alla Home
        </a>
       
        <!-- Seconda immagine in basso -->
        <img src="<%= request.getContextPath() %>/img/SWSoloScritta.png" alt="Scritta Sændwave" class="error-img error-img-bottom" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/img/SWSoloScritta.png';">
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>