<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.carrello.CarrelloBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="java.util.List" %>

<%
    // 1. RECUPERO I DATI DALLA SERVLET (Come testato nel tuo file carrellotest.jsp)
    List<ProdottoBean> prodottiCarrello = (List<ProdottoBean>) request.getAttribute("prodottiCarrello");

    // Variabili per il calcolo dei totali
    double totaleLordo = 0.0;
    double totaleImponibile = 0.0;
    double totaleIva = 0.0;
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Il tuo Carrello</title>
    <link rel="stylesheet" href="css/style.css">
     <link rel="stylesheet" href="css/carrello.css">
   
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main>
        <div class="cart-page-container">
            <h2>Il tuo Carrello Spesa</h2>
            
           <% if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) { %>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Servizio Audio</th>
                            <th>Dettagli</th>
                            <th>Prezzo Netto</th>
                            <th>IVA (22%)</th>
                            <th>Prezzo Lordo</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            // Ciclo FOR-EACH sui prodotti recuperati dalla Servlet
                            for (ProdottoBean p : prodottiCarrello) { 
                                // Calcoli matematici per lo scorporo IVA (22%)
                                double lordoRiga = p.getPrezzo();
                                double nettoRiga = lordoRiga / 1.22; 
                                double ivaRiga = lordoRiga - nettoRiga; 
                                
                                totaleLordo += lordoRiga;
                                totaleImponibile += nettoRiga;
                                totaleIva += ivaRiga;
                        %>
                                <tr>
                                    <td>
                                        <img src="<%= p.getImmagine() %>" class="cart-item-img" onerror="this.src='img/placeholder.png'">
                                    </td>
                                    <td>
                                        <strong style="color: #1a1a2e;"><%= p.getNome() %></strong>
                                        <p style="font-size: 0.85em; margin: 0; color: #777;">Licenza d'utilizzo traccia singola</p>
                                    </td>
                                    <td>€ <%= String.format("%.2f", nettoRiga) %></td>
                                    <td>€ <%= String.format("%.2f", ivaRiga) %></td>
                                    <td style="font-weight: 600;">€ <%= String.format("%.2f", lordoRiga) %></td>
                                    <td>
                                        <a href="RimuoviDalCarrelloServlet?idProdotto=<%= p.getIdProdotto() %>" class="btn-remove">Rimuovi</a>
                                    </td>
                                </tr>
                        <% } %>
                    </tbody>
                </table>

                <div class="summary-box">
                    <div class="summary-row">
                        <span>Totale Imponibile (Netto):</span>
                        <span>€ <%= String.format("%.2f", totaleImponibile) %></span>
                    </div>
                    <div class="summary-row">
                        <span>Totale IVA (22%):</span>
                        <span>€ <%= String.format("%.2f", totaleIva) %></span>
                    </div>
                    <div class="summary-row summary-total">
                        <span>Totale Lordo:</span>
                        <span>€ <%= String.format("%.2f", totaleLordo) %></span>
                    </div>
                    
                    <form action="CheckoutServlet" method="POST" style="margin-top: 15px;">
                        <button type="submit" style="width: 100%; margin-top: 10px; font-weight: bold; padding: 14px;">
                            Procedi al Pagamento
                        </button>
                    </form>
                </div>

            <% } else { %>
                <div style="text-align: center; padding: 50px 0;">
                    <p style="font-size: 1.3em; color: #999; margin-bottom: 20px;">Il tuo carrello è attualmente vuoto.</p>
                    <a href="CatalogoServlet" class="btn" style="text-decoration: none; inline-block: left;">Torna al Catalogo</a>
                </div>
            <% } %>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>