<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.recensione.RecensioneBean" %>
<%
    Map<UtenteBean, List<RecensioneBean>> mappaPro = (Map<UtenteBean, List<RecensioneBean>>) request.getAttribute("mappaProfessionisti");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I Nostri Professionisti – Sændwave</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1 style="color: var(--colore-primario); text-align: center; margin-bottom: 10px;">I Nostri Professionisti</h1>
        <p class="subtitle" style="text-align: center; margin-bottom: 40px;">
            Scopri cosa dicono i nostri clienti degli ingegneri del suono che lavorano su Sændwave.
        </p>

        <div class="dashboard-grid">
            <% 
                if (mappaPro != null && !mappaPro.isEmpty()) {
                    for (Map.Entry<UtenteBean, List<RecensioneBean>> entry : mappaPro.entrySet()) {
                        UtenteBean prof = entry.getKey();
                        List<RecensioneBean> recensioni = entry.getValue();
            %>
                <div class="dash-card card-user" style="margin-bottom: 20px;">
                    <!-- Intestazione Professionista -->
                    <div style="border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 15px;">
                        <h2 style="color: var(--colore-primario); margin: 0;">
                            🎧 <%= prof.getNome() %> <%= prof.getCognome() %>
                        </h2>
                        <span style="color: #666; font-size: 0.9em;">Ingegnere del Suono (Mix & Master)</span>
                    </div>

                    <!-- Lista Recensioni -->
                    <h4 style="margin-bottom: 15px; color: #333;">Recensioni dei Clienti (<%= recensioni.size() %>)</h4>
                    
                    <% if (recensioni.isEmpty()) { %>
                        <p style="color: #888; font-style: italic;">Non ci sono ancora recensioni per questo professionista.</p>
                    <% } else { %>
                        <div style="max-height: 400px; overflow-y: auto; padding-right: 10px;">
                            <% for (RecensioneBean rec : recensioni) { %>
                                <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 15px; border-left: 4px solid var(--colore-secondario);">
                                    <div style="display: flex; justify-content: space-between; margin-bottom: 5px;">
                                        <strong><%= rec.getFkUtente() %></strong>
                                        <span style="color: #f39c12; font-size: 1.1em;">
                                            <% for(int i=0; i<rec.getVoto(); i++) { %>★<% } %><% for(int i=rec.getVoto(); i<5; i++) { %>☆<% } %>
                                        </span>
                                    </div>
                                    <p style="margin: 0; color: #444; font-size: 0.95em;"><%= rec.getCommento() %></p>
                                    <small style="color: #999; display: block; margin-top: 8px;"><%= rec.getDataRecensione() %></small>
                                </div>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            <% 
                    }
                } else { 
            %>
                <p style="text-align: center; width: 100%;">Nessun professionista disponibile al momento.</p>
            <% } %>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>