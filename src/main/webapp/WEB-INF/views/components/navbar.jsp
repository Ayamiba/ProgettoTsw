<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.carrello.CarrelloBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="java.util.List" %>
<%
    // Recuperiamo l'utente in sessione per mostrare il nome o il tasto login
    UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");
	
//Recuperiamo la lista dei prodotti effettivi inseriti nel carrello (creata dalle tue Servlet)
List<ProdottoBean> prodottiNelCarrello = (List<ProdottoBean>) session.getAttribute("carrelloProdotti");

// Contiamo quanti elementi ci sono per mostrare il numerino rosso
int oggettiNelCarrello = (prodottiNelCarrello != null) ? prodottiNelCarrello.size() : 0;
%>
<script src="<%= request.getContextPath() %>/js/Suggerimenti.js"></script>
<script src="<%= request.getContextPath() %>/js/Navbar.js"></script>

<header class="navbar">
    <div class="nav-left">
        <a href="HomeServlet" class="nav-logo">
            <img src="img/SWsoloLogo.png" alt="Logo Sændwave" onerror="this.onerror=null; this.src='img/placeholder.png';">
        </a>
        
        <div class="nav-catalog">
            <a href="CatalogoServlet">Catalogo &#9662;</a>
            <div class="dropdown">
                <a href="CatalogoServlet?tipo=effetto">Effetti</a>
                <a href="CatalogoServlet?tipo=studio_tool">Studio Tools</a>
                <a href="CatalogoServlet?tipo=bundle">Bundle Completi</a>
            </div>
        </div>
    </div>

    <div class="nav-center">
        <form action="CatalogoServlet" method="GET" class="search-form" id="search-form">
            <input id="search-input" type="text" name="search" class="search-bar" placeholder="Cerca plugin, equalizzatori, compressori...">
            <ul id="suggerimenti" class="dropdown-suggerimenti"></ul>
        </form>
    </div>

    <div class="nav-right">
        <div class="cart-wrapper">
            <a href="CarrelloServlet" class="cart-icon-btn">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-shopping-cart" width="22" height="22">
                    <circle cx="9" cy="21" r="1"></circle>
                    <circle cx="20" cy="21" r="1"></circle>
                    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                </svg>
                
                <% if (oggettiNelCarrello > 0) { %>
                    <span class="cart-badge"><%= oggettiNelCarrello %></span>
                <% } %>
            </a>
            <div class="mini-cart-dropdown">
                <h4>Il tuo Carrello</h4>
                <div class="mini-cart-items">
                    <% 
                        if (oggettiNelCarrello > 0) {
                            for (ProdottoBean p : prodottiNelCarrello) {
                    %>
                                <div class="mini-cart-item">
                                    <img src="<%= p.getImmagine() %>" onerror="this.src='img/placeholder.png'">
                                    <div class="mini-item-details">
                                        <span class="mini-item-name"><%= p.getNome() %></span>
                                        <span class="mini-item-price">€ <%= String.format("%.2f", p.getPrezzo()) %></span>
                                    </div>
                                </div>
                    <% 
                            }
                        } else { 
                    %>
                            <p class="empty-cart-msg">Il carrello è vuoto</p>
                    <% } %>
                </div>
                <a href="CarrelloServlet" class="btn-goToCart">Vai al Carrello</a>
            </div>
            </div>
        
        <% if (utenteLoggato != null) { %>
            <span class="user-greeting"><%= utenteLoggato.getNome() %></span>
            <a href="LogoutServlet">Logout</a>
        <% } else { %>
            <a href="login.jsp">Accedi / Registrati</a>
        <% } %>
    </div>
    <div class="hamburger-menu" id="hamburger-menu">
        <span></span>
        <span></span>
        <span></span>
    </div>

    <div class="mobile-sidebar" id="mobile-sidebar">
        <div class="nav-catalog-mobile">
            <a href="CatalogoServlet" style="color: var(--colore-primario);">Catalogo</a>
            <a href="CatalogoServlet?tipo=effetto" class="sub-link">- Effetti</a>
            <a href="CatalogoServlet?tipo=studio_tool" class="sub-link">- Studio Tools</a>
            <a href="CatalogoServlet?tipo=bundle" class="sub-link">- Bundle Completi</a>
        </div>
        
        <hr class="mobile-divider">
        
        <a href="CarrelloServlet">Carrello</a>
        <% if (utenteLoggato != null) { %>
            <a href="profilo.jsp">Profilo</a>
            <a href="LogoutServlet" style="color: #d9534f;">Logout</a>
        <% } else { %>
            <a href="login.jsp">Accedi / Registrati</a>
        <% } %>
    </div>
</header>

