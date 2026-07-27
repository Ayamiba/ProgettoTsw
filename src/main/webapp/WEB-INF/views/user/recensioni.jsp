<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%
    // Recupero dati dalla sessione per vedere se l'utente esiste dato che solo gli utenti registrati e con l'ordine possono recensire
    UtenteBean utente = (UtenteBean) session.getAttribute("user");
    OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    
    // controllo per sicurezza
    if (utente == null || ordine == null) {
        response.sendRedirect(request.getContextPath() + "/ProfiloServlet");
        return;
    }
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

    <!-- Riutilizzo della navbar -->
    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <!-- Utilizzo il tuo container della dashboard per avere stile coerente -->
    <main class="dashboard-container">
        <h1 style="color: var(--colore-primario);">La tua opinione è importante</h1>
        <p class="subtitle" style="color: var(--colore-secondario);">
            Stai scrivendo una recensione per l'Ordine #<%= ordine.getIdOrdine() %> del <%= ordine.getDataOrdine() %>
        </p>
        
        <div class="dashboard-grid" style="display: block; max-width: 800px; margin: 0 auto;">
            
            <!-- RECENSIONE DELL ORDINE-->
            <div class="dash-card card-user" style="margin-bottom: 30px;">
                <h3 style="color: var(--colore-primario);">Servizio Generale</h3>
                <p class="card-desc">Valuta la tua esperienza per questo ordine.</p>
                
                <form action="<%= request.getContextPath() %>/InviaRecensioneServlet" method="POST" class="review-form">
                    <input type="hidden" name="tipo" value="ordine">
                    <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
                    <input type="hidden" name="ritorno" value="paginaRecensioni"> <!-- Per la gestione del redirect -->
                    
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
                    
                    <!-- Il bottone che prende sempre lo stile dal CSS base -->
                    <button type="submit" style="align-self: flex-start;">Invia Recensione Servizio</button>
                </form>
            </div>

            <!-- RECENSIONI DEI SINGOLI PRODOTTI -->
            <% if (prodotti != null && !prodotti.isEmpty()) { %>
                <h2 style="color: var(--colore-primario); margin: 40px 0 20px 0; font-size: 1.5em;">Prodotti Acquistati</h2>
                
                <% for (ProdottoBean prod : prodotti) { %>
                    <div class="dash-card card-user" style="margin-bottom: 20px;">
                        
                        <div class="product-review-header">
                            <!-- Assicurati che l'oggetto ProdottoBean abbia il metodo getImmagine() -->
                            <img src="<%= request.getContextPath() %>/<%= prod.getImmagine() %>" alt="Immagine <%= prod.getNome() %>">
                            <h3 style="margin: 0; font-size: 1.2em;"><%= prod.getNome() %></h3>
                        </div>
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
                            
                            <!-- Bottone colorato diversamente per staccare usando il tuo colore secondario -->
                            <button type="submit" style="align-self: flex-start; background-color: var(--colore-secondario);">
                                Invia Recensione <%= prod.getNome() %>
                            </button>
                        </form>
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

    <!-- Riutilizzo del footer -->
    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>