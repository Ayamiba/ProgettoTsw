<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.recensione.RecensioneDAO" %> <!-- NUOVO IMPORT -->
<%@ page import="java.util.List" %>
<%
    UtenteBean utente = (UtenteBean) session.getAttribute("user");
    OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    
    if (utente == null || ordine == null) {
        response.sendRedirect(request.getContextPath() + "/ProfiloServlet");
        return;
    }

    // Istanziamo il DAO per controllare le recensioni esistenti
    RecensioneDAO recensioneDAO = new RecensioneDAO();
%>
<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recensisci il tuo Ordine – Sændwave</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/recensioni.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1 style="color: var(--colore-primario);">La tua opinione è importante</h1>
        <p class="subtitle" style="color: var(--colore-secondario);">
            Stai scrivendo una recensione per l'Ordine #<%= ordine.getIdOrdine() %> del <%= ordine.getDataOrdine() %>
        </p>

        <%-- Notifica di Sistema --%>
        <% 
            String messaggio = request.getParameter("messaggio");
            if (messaggio != null && !messaggio.trim().isEmpty()) { 
        %>
            <div id="toast-notifica" style="background-color: #e8f5e9; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border-left: 4px solid #28a745; font-weight: bold;">
                ✔️ <%= messaggio %>
            </div>
        <% } %>
        
        <div class="dashboard-grid" style="display: block; max-width: 800px; margin: 0 auto;">
            
            <!-- RECENSIONE DELL'ORDINE -->
            <div class="dash-card card-user" style="margin-bottom: 30px;">
                <h3 style="color: var(--colore-primario);">Servizio Generale</h3>
                
                <% 
                    // CONTROLLO: L'utente ha già recensito questo ordine?
                    boolean haRecensitoOrdine = recensioneDAO.esisteRecensioneOrdine(utente.getEmail(), ordine.getIdOrdine());
                    if (haRecensitoOrdine) { 
                %>
                    <div style="padding: 20px; background-color: #f8f9fa; border: 1px dashed #ccc; border-radius: 5px; text-align: center;">
                        <p style="color: #28a745; font-weight: bold; margin: 0;">Hai già lasciato una recensione per il servizio di questo ordine. Grazie!</p>
                    </div>
                <% } else { %>
                    <p class="card-desc">Valuta la tua esperienza per questo ordine.</p>
                    
                    <form action="<%= request.getContextPath() %>/InviaRecensioneServlet" method="POST" class="review-form">
                        <input type="hidden" name="tipo" value="ordine">
                        <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
                        <input type="hidden" name="ritorno" value="paginaRecensioni">
                        
                        <div>
                            <label for="votoOrdine" style="font-weight: 600; display: block; margin-bottom: 5px;">Voto:</label>
                            <select name="voto" id="votoOrdine" required>
                                <option value="" disabled selected>Scegli una valutazione...</option>
                                <option value="5">★★★★★ (5/5 - Eccellente)</option>
                                <option value="4">★★★★☆ (4/5 - Molto buono)</option>
                                <option value="3">★★★☆☆ (3/5 - Sufficiente)</option>
                                <option value="2">★★☆☆☆ (2/5 - Scarso)</option>
                                <option value="1">★☆☆☆☆ (1/5 - Pessimo)</option>
                            </select>
                        </div>
                        
                        <div>
                            <label for="commentoOrdine" style="font-weight: 600; display: block; margin-bottom: 5px;">Commento:</label>
                            <textarea name="commento" id="commentoOrdine" placeholder="Come ti sei trovato con i tempi di consegna e la qualità del servizio?" required></textarea>
                        </div>
                        
                        <button type="submit" style="align-self: flex-start;">Invia Recensione Servizio</button>
                    </form>
                <% } %>
            </div>

            <!-- RECENSIONI DEI SINGOLI PRODOTTI -->
            <% if (prodotti != null && !prodotti.isEmpty()) { %>
                <h2 style="color: var(--colore-primario); margin: 40px 0 20px 0; font-size: 1.5em;">Prodotti Acquistati</h2>
                
                <% for (ProdottoBean prod : prodotti) { %>
                    <div class="dash-card card-user" style="margin-bottom: 20px;">
                        
                        <div class="product-review-header">
                            <img src="<%= request.getContextPath() %>/<%= prod.getImmagine() %>" alt="Immagine <%= prod.getNome() %>">
                            <h3 style="margin: 0; font-size: 1.2em;"><%= prod.getNome() %></h3>
                        </div>

                        <% 
                            // CONTROLLO: L'utente ha già recensito questo specifico prodotto?
                            boolean haRecensitoProdotto = recensioneDAO.esisteRecensioneProdotto(utente.getEmail(), prod.getIdProdotto());
                            if (haRecensitoProdotto) { 
                        %>
                            <div style="padding: 15px; margin-top: 15px; background-color: #f8f9fa; border: 1px dashed #ccc; border-radius: 5px; text-align: center;">
                                <p style="color: #28a745; font-weight: bold; margin: 0;">Hai già recensito questo plugin. Vai alla pagina del prodotto per vederla!</p>
                            </div>
                        <% } else { %>
                            <form action="<%= request.getContextPath() %>/InviaRecensioneServlet" method="POST" class="review-form">
                                <input type="hidden" name="tipo" value="prodotto">
                                <input type="hidden" name="idProdotto" value="<%= prod.getIdProdotto() %>">
                                <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
                                <input type="hidden" name="ritorno" value="paginaRecensioni">
                                
                                <div>
                                    <label style="font-weight: 600; display: block; margin-bottom: 5px;">Voto Prodotto:</label>
                                    <select name="voto" required>
                                        <option value="" disabled selected>Scegli una valutazione...</option>
                                        <option value="5">★★★★★ (Eccellente)</option>
                                        <option value="4">★★★★☆ (Molto buono)</option>
                                        <option value="3">★★★☆☆ (Sufficiente)</option>
                                        <option value="2">★★☆☆☆ (Scarso)</option>
                                        <option value="1">★☆☆☆☆ (Pessimo)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <label style="font-weight: 600; display: block; margin-bottom: 5px;">Recensione:</label>
                                    <textarea name="commento" placeholder="Cosa ne pensi di questo plug-in o sample?" required></textarea>
                                </div>
                                
                                <button type="submit" style="align-self: flex-start; background-color: var(--colore-secondario);">
                                    Invia Recensione <%= prod.getNome() %>
                                </button>
                            </form>
                        <% } %>
                    </div>
                <% } %>
            <% } %>

            <div style="text-align: center; margin-top: 40px;">
                <a href="<%= request.getContextPath() %>/ProfiloServlet" style="color: var(--colore-secondario); font-weight: bold; text-decoration: none;">
                    &larr; Torna allo Storico Ordini
                </a>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>