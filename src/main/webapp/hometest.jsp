<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.utente.UtenteBean" %>

<%
    // Recuperiamo le variabili dalla Request e dalla Sessione
    List<ProdottoBean> ultimiProdotti = (List<ProdottoBean>) request.getAttribute("ultimiProdotti");
    UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Homepage</title>
    <script src="js/Suggerimenti.js"></script>
    <!-- Collegamento al file CSS esterno -->
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/home.css">
</head>
<body>

    <!-- NAVBAR ESTESA -->
    <header class="navbar">
        <div class="nav-left">
            <!-- LOGO NAVBAR: Ora c'è solo l'onda (SWsoloLogo.png) -->
            <a href="HomeServlet" class="nav-logo">
                <img src="img/SWsoloLogo.png" alt="Logo Sændwave" onerror="this.src='img/placeholder.png'">
            </a>
            
            <!-- CATALOGO CON DROPDOWN -->
            <div class="nav-catalog">
                <a href="CatalogoServlet">Catalogo &#9662;</a>
                <div class="dropdown">
                    <a href="CatalogoServlet?tipo=effetto">Effetti VST</a>
                    <a href="CatalogoServlet?tipo=studio_tool">Studio Tools</a>
                    <a href="CatalogoServlet?tipo=bundle">Bundle Completi</a>
                </div>
            </div>
        </div>

        <!-- BARRA DI RICERCA CENTRALE -->
          <div class="nav-center">
            <form action="CatalogoServlet" method="GET" class="search-form" id="search-form">
                <input id="search-input" type="text" name="search" class="search-bar" placeholder="Cerca plugin, equalizzatori, compressori...">
            <ul id="suggerimenti" class="dropdown-suggerimenti"></ul>
            </form>
        </div>

        <!-- CARRELLO E PROFILO -->
        <div class="nav-right">
            <a href="CarrelloServlet">Carrello</a>
            
            <%-- LOGICA SCRIPTLET: Gestione Login/Profilo --%>
            <% if (utenteLoggato != null) { %>
                <span class="user-greeting"><%= utenteLoggato.getNome() %></span>
                <a href="LogoutServlet">Logout</a>
            <% } else { %>
                <a href="login.jsp">Profilo / Accedi</a>
            <% } %>
        </div>
    </header>

    <main>
        <!-- SEZIONE 1: BANNER CENTRALE E ULTIMI PRODOTTI -->
        <section class="latest-products">
            
            <!-- SCRITTA DEL BRAND (SWSoloScritta.png) POSIZIONATA NEL BANNER CENTRALE -->
            <img src="img/SWSoloScritta.png" alt="SÆNDWAVE" class="brand-text-banner" onerror="this.style.display='none'">
            
            <h1 class="section-title">Ultimi Prodotti</h1>
            
            <div class="grid">
                <%-- LOGICA SCRIPTLET: Rendering dinamico dei prodotti --%>
                <% 
                   if (ultimiProdotti != null && !ultimiProdotti.isEmpty()) { 
                       for (ProdottoBean prodotto : ultimiProdotti) { 
                %>
                           <article class="card">
                               <img src="img/prodotti/<%= prodotto.getImmagine() %>" 
                                    alt="<%= prodotto.getNome() %>" 
                                    onerror="this.src='img/placeholder.png'">
                               
                               <h3 class="card-title"><%= prodotto.getNome() %></h3>
                               
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

        <!-- SEZIONE 2: CATEGORIE (Box di navigazione) -->
        <section class="categories-section">
            <div class="cat-grid">
                <a href="CatalogoServlet?tipo=bundle" class="cat-box bg-bundle">
                    BUNDLE
                </a>
                <a href="CatalogoServlet?tipo=effetto" class="cat-box bg-effetti">
                    EFFETTI
                </a>
                <a href="CatalogoServlet?tipo=studio_tool" class="cat-box bg-studio">
                    STUDIO TOOLS
                </a>
            </div>
        </section>
    </main>

    <!-- FOOTER CON LINK ABOUT US -->
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