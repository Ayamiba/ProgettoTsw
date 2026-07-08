<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.prodotto.ProdottoBean" %>

<%
    // Recuperiamo il prodotto inviato dalla Servlet
    ProdottoBean prodotto = (ProdottoBean) request.getAttribute("prodottoSingolo");
    
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
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>