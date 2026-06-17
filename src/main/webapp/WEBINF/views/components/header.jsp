<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header style="background-color: white; border-bottom: 2px solid var(--elementi-principali); padding: 15px 30px; display: flex; justify-content: space-between; align-items: center;">
    
    <div class="logo">
        <a href="HomeServlet" style="text-decoration: none;">
            <h2 style="color: var(--colore-primario); letter-spacing: 2px;">SÆNDWAVE</h2>
        </a>
    </div>

    <nav>
        <ul style="list-style: none; display: flex; gap: 20px;">
            <li><a href="HomeServlet" style="text-decoration: none; color: var(--colore-secondario);">Home</a></li>
            <li><a href="CatalogoServlet" style="text-decoration: none; color: var(--colore-secondario);">Catalogo</a></li>
            <li><a href="LoginServlet" style="text-decoration: none; color: var(--colore-secondario);">Profilo / Login</a></li>
            <li><a href="CarrelloServlet" style="text-decoration: none; font-weight: bold; color: var(--colore-primario);">Carrello</a></li>
        </ul>
    </nav>
</header>