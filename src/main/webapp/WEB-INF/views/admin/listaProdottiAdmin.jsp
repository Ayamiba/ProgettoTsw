<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="java.util.List" %>
<% 
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
    List<ProdottoBean> catalogo = (List<ProdottoBean>) request.getAttribute("catalogo");
    
    // Sicurezza visiva
    if(utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Catalogo - Sændwave Admin</title>
    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    
    <script src="<%= request.getContextPath() %>/js/RicercaProdottoAdmin.js"></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
        <!-- Header pulito con flex-wrap gestito in admin.css -->
        <div class="admin-page-header">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 5px;">Inventario Catalogo</h1>
                <p class="subtitle" style="margin: 0;">Seleziona un prodotto per aggiornarne le specifiche o rimuoverlo.</p>
            </div>
            <a href="ProfiloServlet" class="btn-back">&larr; Torna alla Dashboard</a>
        </div>
      
        <div class="dash-card card-admin">
            
            <!-- Barra di ricerca pulita -->
            <div class="admin-search-container">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#888" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="11" cy="11" r="8"></circle>
                    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                </svg>
                <input type="text" id="adminSearch" onkeyup="filtraTabellaLive()" class="dash-input" placeholder="Cerca per nome prodotto o ID..." style="max-width: 400px;">
            </div>

            <!-- Wrapper protettivo per lo scorrimento mobile -->
            <div class="dash-table-wrapper">
                <table class="dash-table" id="tabellaProdotti">
                    <thead>
                        <tr>
                            <th style="width: 60px;">Img</th>
                            <th>ID</th>
                            <th>Nome Prodotto</th>
                            <th>Prezzo</th>
                            <th style="text-align: center;">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (catalogo == null || catalogo.isEmpty()) { %>
                            <tr>
                                <td colspan="5" class="empty-table-msg">
                                    Nessun prodotto presente nel database.
                                </td>
                            </tr>
                        <% } else { 
                            for(ProdottoBean p : catalogo) { 
                                String imgPath = p.getImmagine();
                                if(imgPath == null || imgPath.isEmpty()) imgPath = "img/placeholder.png";
                        %>
                            <tr>
                                <!-- I data-label permettono alla tabella di trasformarsi in card su mobile -->
                                <td data-label="Img">
                                    <img src="<%= request.getContextPath() %>/<%= imgPath %>" class="thumb-admin" alt="thumb">
                                </td>
                                <td data-label="ID" class="td-id">#<%= p.getIdProdotto() %></td>
                                <td data-label="Nome Prodotto" class="td-bold"><%= p.getNome() %></td>
                                <td data-label="Prezzo">€ <%= String.format("%.2f", p.getPrezzo()) %></td>
                                <td data-label="Azioni">
                                    <div class="table-actions">
                                        <a href="ModificaProdottoServlet?id=<%= p.getIdProdotto() %>" class="btn-text-edit">Modifica</a>
                                        
                                        <form action="EliminaProdottoServlet" method="POST" onsubmit="return confermaEliminazione('<%= p.getNome().replace("'", "\\'") %>');">
                                            <input type="hidden" name="idProdotto" value="<%= p.getIdProdotto() %>">
                                            <button type="submit" class="btn-text-delete">Elimina</button>
                                        </form>
                                    </div>
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
    
    <script>
        function confermaEliminazione(nomeProdotto) {
            return confirm("⚠️ ATTENZIONE!\n\nSei sicuro di voler eliminare definitivamente il prodotto '" + nomeProdotto + "' dal catalogo?\n\nQuesta azione non può essere annullata.");
        }
    </script>

</body>
</html>