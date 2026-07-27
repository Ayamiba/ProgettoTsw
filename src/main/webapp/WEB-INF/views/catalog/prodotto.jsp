<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.recensione.RecensioneBean" %>
<%@ page import="java.util.List" %>

<%
    // Recuperiamo il prodotto e le recensioni inviati dalla Servlet
    ProdottoBean prodotto = (ProdottoBean) request.getAttribute("prodottoSingolo");
    List<RecensioneBean> recensioni = (List<RecensioneBean>) request.getAttribute("recensioni");
    
    // Sicurezza: se per qualche motivo il prodotto è nullo, fermiamo il rendering
    if(prodotto == null) {
        response.sendRedirect("CatalogoServlet");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – <%= prodotto.getNome() %></title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/prodotto.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main>
        <!-- BOX 1: DETTAGLIO PRODOTTO -->
        <div class="product-detail-container">
            <div class="product-image-box">
                <img src="<%= prodotto.getImmagine() %>" alt="<%= prodotto.getNome() %>" onerror="this.src='img/placeholder.png'">
                
                <h4 style="margin-top: 20px; color: var(--colore-secondario);">Ascolta la Demo</h4>
                <audio controls class="audio-demo">
                    <source src="uploads/demo_<%= prodotto.getIdProdotto() %>.mp3" type="audio/mpeg">
                    Il tuo browser non supporta l'elemento audio.
                </audio>
            </div>

            <div class="product-info-box">
                <h1><%= prodotto.getNome() %></h1>
                <hr style="margin: 15px 0; border: 0; border-top: 1px solid #eee;">
                
                <p style="font-size: 1.1em; line-height: 1.6; color: #555;">
                    <%= prodotto.getDescrizione() %>
                </p>
                
                <div class="product-price">
                    € <%= String.format("%.2f", prodotto.getPrezzo()) %> 
                    <span class="iva-info">IVA incl.</span>
                </div>

                <form action="AggiungiAlCarrelloServlet" method="POST">
                    <input type="hidden" name="idProdotto" value="<%= prodotto.getIdProdotto() %>">
                    <button type="submit" class="btn" style="width: 100%; font-size: 1.1em; padding: 15px;">
                        Aggiungi al Carrello
                    </button>
                </form>
            </div>
        </div>

        <!-- BOX 2: RECENSIONI (Stesso identico stile del box prodotto) -->
        <div class="product-detail-container" style="display: block; margin-top: 30px; padding: 30px;">
            
            <h2 style="color: #1a1a2e; font-size: 1.6em;">Recensioni dei Clienti</h2>
            <hr style="margin: 15px 0 25px 0; border: 0; border-top: 1px solid #eee;">
            
            <% if (recensioni == null || recensioni.isEmpty()) { %>
                <p style="color: #777; font-size: 1.1em; font-style: italic;">
                    Nessuno ha ancora recensito questo plugin. Sii il primo a provarlo!
                </p>
            <% } else { %>
                <div class="reviews-list">
                    <% for(RecensioneBean r : recensioni) { %>
                        <div style="padding: 15px 0; border-bottom: 1px dashed #eee;">
                            
                            <!-- Intestazione Recensione: Stelle e Data -->
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <div style="color: #ffc107; font-size: 1.2em; letter-spacing: 2px;">
                                    <% 
                                        for(int i = 1; i <= 5; i++) { 
                                            if(i <= r.getVoto()) {
                                                out.print("★");
                                            } else {
                                                out.print("<span style='color: #e0e0e0;'>★</span>"); // Stella vuota grigia
                                            }
                                        } 
                                    %>
                                </div>
                                <span style="font-size: 0.9em; color: #999;"><%= r.getDataRecensione() %></span>
                            </div>
                            
                            <!-- Testo della recensione (stile coerente con la descrizione del prodotto) -->
                            <p style="font-size: 1.05em; color: #555; line-height: 1.6; margin: 0; font-style: italic;">
                                "<%= (r.getCommento() != null && !r.getCommento().trim().isEmpty()) ? r.getCommento() : "Nessun commento testuale lasciato dall'utente." %>"
                            </p>
                            
                        </div>
                    <% } %>
                </div>
            <% } %>
            
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>