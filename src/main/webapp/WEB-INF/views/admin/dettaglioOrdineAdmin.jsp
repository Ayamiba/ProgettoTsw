<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<% 
    UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); 
    OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");
    String emailCliente = (String) request.getAttribute("emailCliente");
    
    if(ordine == null) {
        response.sendRedirect(request.getContextPath() + "/GestioneOrdiniServlet");
        return;
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
    
    <style>
        .form-group label { font-weight: bold; margin-bottom: 5px; display: block; color: #333; }
        .form-input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 15px; font-family: inherit; box-sizing: border-box; }
        .status-badge-large { padding: 5px 12px; border-radius: 20px; font-weight: bold; font-size: 0.9em; display: inline-block; }
    </style>
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
            
            <!-- COLONNA SINISTRA: Riepilogo Dati -->
            <div class="dash-col" style="flex: 2;">
                <div class="dash-card card-admin">
                    <h3 style="border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; margin-bottom: 15px;">Dettagli Cliente e Transazione</h3>
                    
                    <p><strong>Stato Attuale:</strong> 
                        <span class="status-badge-large" style="background-color: #e2e3e5; color: #383d41;"><%= ordine.getStato() %></span>
                    </p>
                    <p><strong>Totale Pagato:</strong> € <%= String.format("%.2f", ordine.getTotale()) %></p>
						<p><strong>Cliente:</strong> <%= emailCliente %></p>                    
                    <!-- Eventuale file caricato dal cliente per il Mix/Master -->
                    <%-- Sostituisci getFileCaricatoUtente() col metodo reale se gestito nell'ordine --%>
                    <div style="margin-top: 25px; padding: 15px; background: #f8f9fa; border-radius: 5px; border-left: 4px solid #4134E7;">
                        <h4>File sorgenti del cliente</h4>
                        <p style="font-size: 0.9em; color: #666;">Se l'ordine include un servizio Mix/Master, scarica qui le tracce.</p>
                        <a href="#" class="dash-btn" style="background: #6c757d;">⬇️ Scarica Zip Tracce (Demo)</a>
                    </div>
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