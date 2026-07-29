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
    <!-- Aggiunto admin.css per ereditare le funzioni mobile (es. btn-back e tabelle a card) -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
        <!-- Intestazione pulita e responsiva tramite admin.css -->
        <div class="admin-page-header">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 5px;">Archivio Ordini Globale</h1>
                <p class="subtitle" style="margin: 0;">Visualizza e gestisci l'intero storico degli acquisti.</p>
            </div>
            <a href="ProfiloServlet" class="btn-back">&larr; Torna alla Dashboard</a>
        </div>

        <div class="dash-card card-admin">
            
            <!-- Wrapper protettivo per lo scorrimento mobile in caso di emergenza -->
            <div class="dash-table-wrapper">
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
                                <td colspan="5" class="empty-table-msg">
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
                                <!-- I data-label attivano la trasformazione in card su mobile -->
                                <td data-label="ID Ordine" class="td-id">#<%= ordine.getIdOrdine() %></td>
                                <td data-label="Data Acquisto"><%= ordine.getDataOrdine() %></td>
                                <td data-label="Stato Lavorazione">
                                    <span class="status-badge <%= statoClasse %>"><%= ordine.getStato() %></span>
                                </td>
                                <td data-label="Totale Pagato" class="td-bold">€ <%= String.format("%.2f", ordine.getTotale()) %></td>
                                <td data-label="Azioni" style="text-align: center;">
                                    <!-- Questo bottone usa la classe testuale unificata -->
                                    <a href="DettaglioOrdineAdminServlet?id=<%= ordine.getIdOrdine() %>" class="btn-text-edit">Gestisci</a>
                                </td>
                            </tr>
                        <%  } 
                           } %>
                    </tbody>
                </table>
            </div> <!-- Fine dash-table-wrapper -->
            
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>