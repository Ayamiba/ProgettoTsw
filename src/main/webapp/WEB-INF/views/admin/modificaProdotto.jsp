<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<% 
    UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); 
    ProdottoBean prodotto = (ProdottoBean) request.getAttribute("prodotto");
    
    // Sicurezza visiva sulla pagina
    if(prodotto == null) {
        response.sendRedirect(request.getContextPath() + "/ProfiloAdminServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifica Prodotto - Sændwave Admin</title>
    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
   
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    
    
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
        <!-- Intestazione resa responsiva con flex-wrap e gap -->
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px; width: 100%; box-sizing: border-box;">
    <div style="flex: 1 1 auto; min-width: 0; word-break: break-word; overflow-wrap: break-word;">
        <h1 style="color: #d9534f; margin-bottom: 5px;">Modifica: <%= prodotto.getNome() %></h1>
        <p class="subtitle" style="margin: 0;">Aggiorna le specifiche, il prezzo o la grafica del prodotto.</p>
    </div>
    <a href="ProfiloServlet" class="btn-back" style="flex-shrink: 0;">&larr; Torna alla Dashboard</a>
</div>

        <% if(request.getParameter("messaggio") != null) { %>
            <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                ✨ <strong>Successo!</strong> <%= request.getParameter("messaggio") %>
            </div>
        <% } %>

        <div class="dashboard-grid">
            <div class="dash-col" style="flex: 2;">
                <div class="dash-card card-admin">
                    <!-- Importante: l'action punta alla Servlet di Modifica -->
                    <form action="ModificaProdottoServlet" method="POST" enctype="multipart/form-data">
                        
                        <!-- ID Nascosto Fondamentale! -->
                        <input type="hidden" name="idProdotto" value="<%= prodotto.getIdProdotto() %>">
                        
                        <div class="form-group">
                            <label for="nome">Nome Prodotto</label>
                            <input type="text" id="nome" name="nome" class="form-input" value="<%= prodotto.getNome() %>" required>
                        </div>

                        <div class="form-group">
                            <label for="prezzo">Prezzo di Vendita (€)</label>
                            <%-- Sostituiamo la virgola con il punto per evitare conflitti HTML --%>
                            <input type="number" id="prezzo" name="prezzo" class="form-input" step="0.01" min="0" value="<%= String.valueOf(prodotto.getPrezzo()).replace(",", ".") %>" required>
                        </div>

                        <div class="form-group">
                            <label for="descrizione">Descrizione Dettagliata</label>
                            <textarea id="descrizione" name="descrizione" class="form-input" required><%= prodotto.getDescrizione() %></textarea>
                        </div>

                        <div class="form-group">
                            <label for="foto">Aggiorna Copertina (Lascia vuoto per mantenere quella attuale)</label>
                            <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 10px;">
                                <% 
                                   String imgPath = prodotto.getImmagine();
                                   if(imgPath == null || imgPath.isEmpty()) imgPath = "img/placeholder.png";
                                %>
                                <img src="<%= request.getContextPath() %>/<%= imgPath %>" class="current-image-preview" alt="Attuale">
                                <span style="font-size: 0.85em; color: #666;">Immagine in uso</span>
                            </div>
                            <!-- Rimosso il tag required -->
                            <input type="file" id="foto" name="foto" class="form-input" accept="image/*" style="padding: 7px;">
                        </div>

                        <!-- Aggiunto width 100% al bottone -->
                        <div style="text-align: right; margin-top: 20px;">
                            <button type="submit" class="dash-btn-save" style="font-size: 1.1em; padding: 12px 30px; width: 100%;">Salva Modifiche</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="dash-col" style="flex: 1;">
                <div class="dash-card card-admin" style="background: #f8f9fa;">
                    <h3 style="color: #4134E7;">Info di Sistema</h3>
                    <ul style="padding-left: 20px; color: #555; line-height: 1.6; margin-top: 15px;">
                        <li><strong>ID Database:</strong> #<%= prodotto.getIdProdotto() %></li>
                        <li>Se non carichi una nuova immagine, il sistema manterrà intatta l'immagine precedente senza generare errori.</li>
                        <li>Le modifiche avranno effetto immediato su tutto il catalogo e sui carrelli degli utenti attivi.</li>
                    </ul>
                </div>
            </div>
        </div>

    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>