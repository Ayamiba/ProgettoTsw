<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.utente.UtenteBean" %>

<%
    // Recupero dei dati passati da CatalogoServlet [cite: 1]
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    String categoriaAttiva = (String) request.getAttribute("categoriaAttiva");
    String prezzoAttivo = (String) request.getAttribute("prezzoAttivo");
    
    // Recupero dell'utente in sessione per la navbar [cite: 2]
    UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");
    
    // Sanificazione delle stringhe nulle per i form
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

    <header class="navbar">
        <div class="nav-left">
            <a href="HomeServlet" class="nav-logo">
                <img src="img/SWsoloLogo.png" alt="Logo Sændwave" onerror="this.src='img/placeholder.png'">
            </a>
            <div class="nav-catalog">
                <a href="CatalogoServlet">Catalogo &#9662;</a>
                <div class="dropdown">
                    <a href="CatalogoServlet?categoria=effetto">Effetti VST</a>
                    <a href="CatalogoServlet?categoria=studio_tool">Studio Tools</a>
                    <a href="CatalogoServlet?categoria=bundle">Bundle Completi</a>
                </div>
            </div>
        </div>
        <div class="nav-center">
            <form action="CatalogoServlet" method="GET" class="search-form">
                <input type="text" name="search" class="search-bar" placeholder="Cerca plugin, equalizzatori, compressori...">
            </form>
        </div>
        <div class="nav-right">
            <a href="CarrelloServlet">Carrello</a>
            <% if (utenteLoggato != null) { %>
                <span class="user-greeting"><%= utenteLoggato.getNome() %></span>
                <a href="LogoutServlet">Logout</a>
            <% } else { %>
                <a href="login.jsp">Profilo / Accedi</a>
            <% } %>
        </div>
    </header>

    <main class="catalog-container">
        
        <aside class="filters-sidebar">
            <h2 class="filters-title">Filtra Prodotti</h2>
            <form action="CatalogoServlet" method="GET">
                
                <div class="filter-group">
                    <label for="categoria">Categoria</label>
                    <select name="categoria" id="categoria" class="filter-input">
                        <option value="">Tutte le categorie</option>
                        <option value="effetto" <%= categoriaAttiva.equals("effetto") ? "selected" : "" %>>Effetti VST</option>
                        <option value="studio_tool" <%= categoriaAttiva.equals("studio_tool") ? "selected" : "" %>>Studio Tools</option>
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
                       <h3 class="catalog-card-title"><%= prodotto.getNome() %></h3>
                       
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
                       
                       <form action="AggiungiAlCarrelloServlet" method="POST">
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

    <footer class="footer">
        <p>&copy; 2026 Sændwave - Audio Processing & Plugins. Tutti i diritti riservati.</p>
        <div style="margin-top: 15px;">
            <a href="#">About Us</a> |
            <a href="#">Termini di Servizio</a> | 
            <a href="#">Contatti</a>
        </div>
    </footer>

</body>
</html>