<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%
    // Recuperiamo l'utente in sessione per mostrare il nome o il tasto login
    UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");
%>
<script src="<%= request.getContextPath() %>/js/Suggerimenti.js"></script>

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
        <a href="CarrelloServlet">Carrello</a>
        
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
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const hamburger = document.getElementById('hamburger-menu');
        const sidebar = document.getElementById('mobile-sidebar');

        if(hamburger && sidebar) {
            hamburger.addEventListener('click', () => {
                hamburger.classList.toggle('active');
                sidebar.classList.toggle('active');
            });
        }
    });
</script>

