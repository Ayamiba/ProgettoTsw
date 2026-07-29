<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="java.util.List" %>

<%
    List<ProdottoBean> prodottiCarrello = (List<ProdottoBean>) request.getAttribute("prodottiCarrello");

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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/carrello.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main>
        <div class="cart-page-container">
            <h2>Il tuo Carrello Spesa</h2>
            
           <% if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) { %>
                <div class="cart-table-wrapper">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Prodotto</th>
                                <th class="col-fiscal desktop-only-col">Prezzo Netto</th>
                                <th class="col-fiscal desktop-only-col">IVA (22%)</th>
                                <th>Prezzo</th>
                                <th style="text-align: center;"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                for (ProdottoBean p : prodottiCarrello) { 
                                    double lordoRiga = p.getPrezzo();
                                    double nettoRiga = lordoRiga / 1.22; 
                                    double ivaRiga = lordoRiga - nettoRiga; 
                                    
                                    totaleLordo += lordoRiga;
                                    totaleImponibile += nettoRiga;
                                    totaleIva += ivaRiga;
                            %>
                                    <tr>
                                        <td>
                                            <div class="cart-product-info">
                                                <img src="<%= p.getImmagine() %>" alt="<%= p.getNome() %>" class="cart-item-img" onerror="this.src='img/placeholder.png'">
                                                <span class="cart-product-name"><%= p.getNome() %></span>
                                            </div>
                                        </td>
                                        <td class="col-fiscal desktop-only-col">€ <%= String.format("%.2f", nettoRiga) %></td>
                                        <td class="col-fiscal desktop-only-col">€ <%= String.format("%.2f", ivaRiga) %></td>
                                        <td class="cart-price-cell">€ <%= String.format("%.2f", lordoRiga) %></td>
                                        <td style="text-align: center;">
                                            <a href="RimuoviDalCarrelloServlet?idProdotto=<%= p.getIdProdotto() %>" class="btn-remove" title="Rimuovi elemento">&times;</a>
                                        </td>
                                    </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

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
                    
                    <form action="CheckoutServlet" method="GET" style="margin-top: 15px;">
                        <button type="submit" class="btn-checkout">
                            Procedi alla Configurazione
                        </button>
                    </form>
                </div>

            <% } else { %>
                <div style="text-align: center; padding: 50px 0;">
                    <p style="font-size: 1.3em; color: #999; margin-bottom: 20px;">Il tuo carrello è attualmente vuoto.</p>
                    <a href="CatalogoServlet" class="btn-checkout" style="text-decoration: none; display: inline-block; width: auto; padding: 12px 24px;">Torna al Catalogo</a>
                </div>
            <% } %>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>