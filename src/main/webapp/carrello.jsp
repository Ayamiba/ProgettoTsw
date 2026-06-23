<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%
    // 1. RECUPERO I DATI DALLA SERVLET (Puro codice Java)
   List<ProdottoBean> prodottiCarrello = (List<ProdottoBean>) request.getAttribute("prodottiCarrello");
	// Inizializzazione di sicurezza per evitare che sia null se si accede direttamente alla pagina
    if (prodottiCarrello == null) {
        prodottiCarrello = new ArrayList<ProdottoBean>();
    }
    
    // Recuperiamo l'ultima email cercata per mantenerla visibile nel campo di testo
    String emailCercata = request.getParameter("email");
    if (emailCercata == null) {
        emailCercata = "";
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

    <h1>🛠️ SCHERMATA DI TEST CARRELLO</h1>
	//box di prova per inserire la mail
    <div class="box-form">
        <h3>Simula Utente (Inserisci Email del Database)</h3>
        <form action="CarrelloServlet" method="get">
            <label for="email">Email Utente: </label>
            <input type="email" id="email" name="email" value="<%= emailCercata %>" placeholder="es. mario.rossi@email.it" required>
            <button type="submit">Carica Carrello</button>
        </form>
    </div>

    <h2>Risultati dal Database:</h2>

    <% if (prodottiCarrello.isEmpty()) { %>
        <% if (!emailCercata.isEmpty()) { %>
            <h3 style="color: #f44336;">Nessun prodotto trovato nel carrello per: <%= emailCercata %></h3>
        <% } else { %>
            <h3 style="color: #ecc94b;">Inserisci un'email in alto e premi "Carica Carrello" per interrogare il DB.</h3>
        <% } %>
    <% } else { 
        // Ciclo FOR-EACH sui prodotti recuperati dalla JOIN
        for (ProdottoBean plugin : prodottiCarrello) {
    %>
            <div class="box-prodotto">
                <strong>ID Prodotto:</strong> <%= plugin.getIdProdotto() %> <br>
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