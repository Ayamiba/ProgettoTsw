<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="java.util.List" %>
<% 
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
    List<OrdineBean> listaOrdini = (List<OrdineBean>) request.getAttribute("listaOrdini");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Ordini - Sændwave Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 5px;">Archivio Ordini Globale</h1>
                <p class="subtitle" style="margin: 0;">Visualizza e gestisci l'intero storico degli acquisti.</p>
            </div>
            <a href="ProfiloAdminServlet" class="dash-btn-cancel" style="text-decoration: none;">&larr; Torna alla Dashboard</a>
        </div>

        <div class="dash-card card-admin">
            <table class="dash-table">
                <thead>
                    <tr>
                        <th>ID Ordine</th>
                        <th>Data Acquisto</th>
                        <th>Stato Lavorazione</th>
                        <th>Totale Pagato</th>
                        <th style="text-align: center;">Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (listaOrdini == null || listaOrdini.isEmpty()) { %>
                        <tr>
                            <td colspan="5" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                Nessun ordine nel database.
                            </td>
                        </tr>
                    <% } else { 
                        for(OrdineBean ordine : listaOrdini) { 
                            String statoClasse = "pending";
                            if ("Completato".equalsIgnoreCase(ordine.getStato())) statoClasse = "ready";
                            else if ("In Lavorazione".equalsIgnoreCase(ordine.getStato())) statoClasse = "working";
                    %>
                        <tr>
                            <td style="font-weight: bold;">#<%= ordine.getIdOrdine() %></td>
                            <td><%= ordine.getDataOrdine() %></td>
                            <td>
                                <span class="status-badge <%= statoClasse %>"><%= ordine.getStato() %></span>
                            </td>
                            <td style="font-weight: 600;">€ <%= String.format("%.2f", ordine.getTotale()) %></td>
                            <td style="text-align: center;">
                                <!-- Questo bottone servirà in futuro per aprire il dettaglio dell'ordine e cambiare lo stato -->
                                <a href="DettaglioOrdineAdminServlet?id=<%= ordine.getIdOrdine() %>" class="btn-text" style="color: #4134E7;">Gestisci</a>
                            </td>
                        </tr>
                    <%  } 
                       } %>
                </tbody>
            </table>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>