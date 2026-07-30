<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.prodotto.ProdottoDAO" %>
<%@ page import="java.util.List" %>
<% 
    UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); 
    OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");
    String emailCliente = (String) request.getAttribute("emailCliente");
    
    if(ordine == null) {
        response.sendRedirect(request.getContextPath() + "/GestioneOrdiniServlet");
        return;
    }
    
    // Recupero i prodotti associati a questo ordine specifico usando il DAO
    ProdottoDAO prodottoDAO = new ProdottoDAO();
    List<ProdottoBean> prodottiAcquistati = null;
    try {
        prodottiAcquistati = prodottoDAO.doRetrieveByOrdine(ordine.getIdOrdine());
    } catch (Exception e) {
        // Gestione base errore DB
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestisci Ordine #<%= ordine.getIdOrdine() %> - Admin</title>
    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px;">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 5px;">Gestione Ordine #<%= ordine.getIdOrdine() %></h1>
                <p class="subtitle" style="margin: 0;">Acquistato il: <%= ordine.getDataOrdine() %></p>
            </div>
            <!-- Torna alla lista ordini -->
            <a href="GestioneOrdiniServlet" class="btn-back">&larr; Torna agli Ordini</a>
        </div>

        <% if(request.getParameter("messaggio") != null) { %>
            <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                ✨ <strong>Successo!</strong> <%= request.getParameter("messaggio") %>
            </div>
        <% } %>
        <% if(request.getParameter("errore") != null) { %>
            <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
                ⚠️ <strong>Errore:</strong> <%= request.getParameter("errore") %>
            </div>
        <% } %>

        <div class="dashboard-grid">
            
            <!-- COLONNA SINISTRA: Riepilogo Dati e Prodotti -->
            <div class="dash-col" style="flex: 2;">
                
                <!-- Box Dettagli Cliente -->
                <div class="dash-card card-admin">
                    <h3 style="border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; margin-bottom: 15px;">Dettagli Cliente e Transazione</h3>
                    
                    <p><strong>Stato Attuale:</strong> 
                        <span class="status-badge-large" style="background-color: #e2e3e5; color: #383d41;"><%= ordine.getStato() %></span>
                    </p>
                    <p><strong>Totale Pagato:</strong> € <%= String.format("%.2f", ordine.getTotale()) %></p>
                    <p style="margin-bottom: 0;"><strong>Cliente:</strong> <%= emailCliente %></p>                    
                </div>
                
                <!-- Box Prodotti Acquistati -->
                <div class="dash-card card-admin" style="margin-top: 20px;">
                    <h3 style="border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; margin-bottom: 5px;">Prodotti nell'Ordine</h3>
                    
                    <% if (prodottiAcquistati == null || prodottiAcquistati.isEmpty()) { %>
                        <p style="color: #888; font-style: italic; margin-top: 15px;">Nessun prodotto trovato per questo ordine (o è stato acquistato un servizio senza prodotti).</p>
                    <% } else { %>
                        <ul class="order-product-list">
                            <% for (ProdottoBean prod : prodottiAcquistati) { 
                                String imgSrc = prod.getImmagine();
                                if (imgSrc == null || imgSrc.isEmpty()) imgSrc = "img/placeholder.png";
                            %>
                                <li class="order-product-item">
                                    <img src="<%= request.getContextPath() %>/<%= imgSrc %>" class="order-product-img" alt="Immagine Prodotto">
                                    <div class="order-product-details">
                                        <div class="order-product-title">
                                            <%= prod.getNome() %> 
                                            <% if (prod.isEliminato()) { %>
                                                <span style="color: #dc3545; font-size: 0.8em; font-weight: normal; margin-left: 5px;">(Ritirato dal mercato)</span>
                                            <% } %>
                                        </div>
                                        <div class="order-product-price">ID Prodotto: #<%= prod.getIdProdotto() %> | Prezzo: € <%= String.format("%.2f", prod.getPrezzo()) %></div>
                                    </div>
                                </li>
                            <% } %>
                        </ul>
                    <% } %>
                </div>
                
            </div>
            
            <!-- COLONNA DESTRA: Azioni di Lavorazione -->
            <div class="dash-col" style="flex: 1; display: flex; flex-direction: column; gap: 20px;">
                
                <!-- Azione 1: Aggiorna Stato -->
                <div class="dash-card card-admin">
                    <h3 style="color: #4134E7; margin-bottom: 15px;">Aggiorna Stato</h3>
                    <form action="DettaglioOrdineAdminServlet" method="POST">
                        <input type="hidden" name="action" value="aggiornaStato">
                        <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
                        
                        <div class="form-group">
                            <select name="statoOrdine" class="form-input" required>
                                <option value="In attesa" <%= "In attesa".equals(ordine.getStato()) ? "selected" : "" %>>⏳ In Attesa</option>
                                <option value="In lavorazione" <%= "In lavorazione".equals(ordine.getStato()) ? "selected" : "" %>>🛠️ In Lavorazione</option>
                                <option value="Completato" <%= "Completato".equals(ordine.getStato()) ? "selected" : "" %>>✅ Completato</option>
                            </select>
                        </div>
                        <button type="submit" class="dash-btn" style="width: 100%;">Salva Stato</button>
                    </form>
                </div>

                <!-- Azione 2: Upload Consegna -->
                <div class="dash-card card-admin" style="border: 2px dashed #4134E7;">
                    <h3 style="color: #4134E7; margin-bottom: 15px;">Consegna Lavoro</h3>
                    <p style="font-size: 0.85em; color: #555; margin-bottom: 15px;">Carica il Mix/Master finale. L'ordine verrà impostato automaticamente su Completato.</p>
                    
                    <form action="DettaglioOrdineAdminServlet" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="consegnaFile">
                        <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
                        
                        <div class="form-group">
                            <input type="file" name="fileConsegna" class="form-input" accept=".wav, .mp3, .zip" required style="padding: 7px; background: #fff;">
                        </div>
                        <button type="submit" class="dash-btn-save" style="width: 100%;">Consegna al Cliente</button>
                    </form>
                </div>

            </div>
        </div>

    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>