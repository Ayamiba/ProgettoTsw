<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%
    // 1. RECUPERO I DATI DALLA SERVLET (Puro codice Java)
    List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
    String categoriaAttiva = (String) request.getAttribute("categoriaAttiva");
    String prezzoAttivo = (String) request.getAttribute("prezzoAttivo");
    
    // 2. GESTISCO I VALORI DI DEFAULT
    if (prezzoAttivo == null || prezzoAttivo.isEmpty()) {
        prezzoAttivo = "500";
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Test Architettura MVC (Senza JSTL)</title>
    <style>
        body { font-family: monospace; padding: 20px; background-color: #1e1e1e; color: #d4d4d4; }
        .box-filtri { background-color: #2d2d2d; border: 1px solid #007acc; padding: 15px; margin-bottom: 20px; }
        .box-prodotto { border: 1px dashed #4CAF50; padding: 10px; margin-bottom: 10px; background-color: #252526; }
        a { color: #569cd6; text-decoration: none; margin-right: 15px; }
        button { cursor: pointer; }
    </style>
</head>
<body>

    <h1>🛠️ SCHERMATA DI TEST - SENZA LIBRERIE</h1>

    <div class="box-filtri">
        <h3>1. Link per Filtro Categoria:</h3>
        <a href="CatalogoServlet">[Tutti i Prodotti]</a>
        <a href="CatalogoServlet?categoria=Effetti">[Filtra: Effetti]</a>
        <a href="CatalogoServlet?categoria=StudioTool">[Filtra: Studio Tools]</a>
        
        <br><br>
        
        <h3>2. Form per Filtro Prezzo:</h3>
        <form action="CatalogoServlet" method="GET">
            <%-- Se c'è una categoria attiva, stampo l'input hidden usando un IF in Java --%>
            <% if (categoriaAttiva != null && !categoriaAttiva.isEmpty()) { %>
                <span style="color: #ce9178;">Categoria attiva: <%= categoriaAttiva %></span><br>
                <input type="hidden" name="categoria" value="<%= categoriaAttiva %>">
            <% } %>

            <label>Prezzo Massimo (€):</label>
            <%-- Uso <%= variabile %> per stampare un valore Java dentro l'HTML --%>
            <input type="number" name="prezzoMax" value="<%= prezzoAttivo %>">
            <button type="submit">Filtra Prezzo</button>
        </form>
    </div>

    <h2>Risultati dal Database:</h2>

    <%-- Controllo se la lista è vuota in puro Java --%>
    <% if (prodotti == null || prodotti.isEmpty()) { %>
        <h3 style="color: #f44336;">Nessun prodotto trovato.</h3>
        <p>Il database è vuoto o i filtri sono troppo stringenti.</p>
        
    <% } else { 
        // Altrimenti, faccio un normale ciclo FOR-EACH in Java
        for (ProdottoBean plugin : prodotti) {
    %>
            <div class="box-prodotto">
                <strong>ID DB:</strong> <%= plugin.getIdProdotto() %> <br>
                <strong>Nome:</strong> <span style="color: #4CAF50; font-size: 1.2em;"><%= plugin.getNome() %></span> <br>
                <strong>Prezzo:</strong> <%= plugin.getPrezzo() %> € <br>
                <strong>Descrizione:</strong> <%= plugin.getDescrizione() %> <br>
            </div>
    <% 
        } // Fine del ciclo for
    } // Fine dell'if-else
    %>

</body>
</html>