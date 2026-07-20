<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.utente.UtenteBean" %>

<%
    // Recupero dei dati passati da CatalogoServlet [cite: 1]
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    String categoriaAttiva = (String) request.getAttribute("categoriaAttiva");
    String prezzoAttivo = (String) request.getAttribute("prezzoAttivo");
    
    if (categoriaAttiva == null) categoriaAttiva = "";
    if (prezzoAttivo == null) prezzoAttivo = "";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Catalogo</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/catalogo.css">
</head>
<body>

    <%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <main class="catalog-container">
        
        <aside class="filters-sidebar">
            <h2 class="filters-title">Filtra Prodotti</h2>
            <form action="CatalogoServlet" method="GET">
                
                <div class="filter-group">
                    <label for="categoria">Categoria</label>
                    <select name="categoria" id="categoria" class="filter-input">
                        <option value="">Tutte le categorie</option>
                        <option value="Effetto" <%= categoriaAttiva.equals("Effetto") ? "selected" : "" %>>Effetti VST</option>
                        <option value="Studio Tool" <%= categoriaAttiva.equals("Studio Tool") ? "selected" : "" %>>Studio Tools</option>
                        <option value="bundle" <%= categoriaAttiva.equals("bundle") ? "selected" : "" %>>Bundle Completi</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label for="prezzoMax">Prezzo Massimo (€)</label>
                    <input type="number" name="prezzoMax" id="prezzoMax" class="filter-input" 
                           step="0.01" min="0" placeholder="Es. 50" value="<%= prezzoAttivo %>">
                </div>

                <button type="submit" class="btn" style="width: 100%;">Applica Filtri</button>
                
                <% if (!categoriaAttiva.isEmpty() || !prezzoAttivo.isEmpty()) { %>
                    <a href="CatalogoServlet" class="reset-filters-btn">Azzera Filtri</a>
                <% } %>
            </form>
        </aside>

        <section class="catalog-content">
            <div class="catalog-header">
                <h1>Shop Catalogo</h1>
                <span class="results-count">
                    <%= (prodotti != null) ? prodotti.size() : 0 %> prodotti trovati
                </span>
            </div>

            <div class="catalog-grid">
    <% 
       if (prodotti != null && !prodotti.isEmpty()) { 
           for (ProdottoBean prodotto : prodotti) { 
    %>
               <article class="catalog-card">
                   <% 
                       String imgPath = prodotto.getImmagine(); //la path corrisponde all'url che sta nel database 
                       if (imgPath == null || imgPath.trim().isEmpty()) {
                           imgPath = "img/placeholder.png";
                       }
                   %>
                   <img src="<%= imgPath %>" alt="<%= prodotto.getNome() %>">
                   
                   <div class="catalog-card-details">
                       <h3 class="catalog-card-title">
    						<a href="ProdottoServlet?id=<%= prodotto.getIdProdotto() %>" style="text-decoration: none; color: inherit;">
       							 <%= prodotto.getNome() %>
    						</a>
						</h3>
                       
                       <p class="catalog-card-desc">
                           <% 
                              String desc = prodotto.getDescrizione();
                              if(desc != null && desc.length() > 60) {
                                  out.print(desc.substring(0, 60) + "...");
                              } else if(desc != null) {
                                  out.print(desc);
                              }
                           %>
                       </p>
                       
                       <span class="catalog-card-price">
                           € <%= String.format("%.2f", prodotto.getPrezzo()) %>
                       </span>
                       
						<form action="AggiungiAlCarrelloServlet" method="POST" class="ajax-cart-form">
                           <input type="hidden" name="idProdotto" value="<%= prodotto.getIdProdotto() %>">
                           <button type="submit" class="btn">Aggiungi al Carrello</button>
                       </form>
                   </div>
               </article>
    <% 
           } 
       } else { 
    %>
       <div class="no-results">
           <p>Nessun prodotto corrisponde ai criteri di ricerca.</p>
       </div>
    <% 
       } 
    %>
</div>
        </section>
    </main>

    <%@ include file="/WEB-INF/views/components/footer.jsp" %>

</body>
</html>