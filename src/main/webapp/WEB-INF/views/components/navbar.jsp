<%@ page import="model.utente.UtenteBean" %>
<%
    // Recuperiamo l'utente in sessione; questo serve per mostrare il nome o il tasto login
    UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");
%>

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