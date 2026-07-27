<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.prodotto.ProdottoBean" %>

<%
    // Recuperiamo solo i prodotti, la navbar penserà all'utente
    List<ProdottoBean> ultimiProdotti = (List<ProdottoBean>) request.getAttribute("ultimiProdotti");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Homepage</title>
    
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/home.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main>
        <section class="latest-products">
            <img src="img/SWSoloScritta.png" alt="SÆNDWAVE" class="brand-text-banner" onerror="this.onerror=null; this.style.display='none';">
            
            <h1 class="section-title">Ultimi Prodotti</h1>
            
            <div class="grid">
                <% 
                   if (ultimiProdotti != null && !ultimiProdotti.isEmpty()) { 
                       for (ProdottoBean prodotto : ultimiProdotti) { 
                %>
                           <article class="card">
                               <% 
                      	 String imgPath = prodotto.getImmagine(); //la path corrisponde all'url che sta nel database 
                       	 if (imgPath == null || imgPath.trim().isEmpty()) {
                         imgPath = "img/placeholder.png";
                      			 }
                  				 %>
                   			   <img src="<%= imgPath %>" alt="<%= prodotto.getNome() %>">
                               
                               <h3 class="card-title">
    								<a href="ProdottoServlet?id=<%= prodotto.getIdProdotto() %>" style="text-decoration: none; color: inherit;">
        								<%= prodotto.getNome() %>
   									</a>
								</h3>
                               
                               <p class="card-desc">
                                   <% 
                                      String desc = prodotto.getDescrizione();
                                      if(desc != null && desc.length() > 60) {
                                          out.print(desc.substring(0, 60) + "...");
                                      } else if(desc != null) {
                                          out.print(desc);
                                      }
                                   %>
                               </p>
                               
                               <span class="card-price">
                                   € <%= String.format("%.2f", prodotto.getPrezzo()) %>
                               </span>
                               
                               <form action="AggiungiAlCarrelloServlet" method="POST" style="margin-top: auto;">
                                   <input type="hidden" name="idProdotto" value="<%= prodotto.getIdProdotto() %>">
                                   <button type="submit" class="btn">Aggiungi al Carrello</button>
                               </form>
                           </article>
                <% 
                       } 
                   } else { 
                %>
                       <p style="font-size: 1.2em; color: #aaa;">Nessun prodotto disponibile al momento.</p>
                <% 
                   } 
                %>
            </div>
        </section>

        <section class="categories-section">
            <div class="cat-grid">
                <a href="CatalogoServlet?categoria=bundle" class="cat-box bg-bundle">BUNDLE</a>
                <a href="CatalogoServlet?categoria=Effetto" class="cat-box bg-effetti">EFFETTI</a>
                <a href="CatalogoServlet?categoria=Studio Tool" class="cat-box bg-studio">STUDIO TOOLS</a>
            </div>
        </section>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>