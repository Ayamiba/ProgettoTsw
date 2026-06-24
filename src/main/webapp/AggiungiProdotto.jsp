<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pannello Admin - Aggiungi Prodotto</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        .form-container { max-width: 500px; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group textarea { width: 100%; padding: 8px; box-sizing: border-box; }
        .btn-submit { background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-submit:hover { background-color: #218838; }
        .messaggio { color: green; font-weight: bold; margin-bottom: 15px; }
    </style>
</head>
<body>

    <h2>Pannello Amministratore - Inserimento Prodotto</h2>
    
    <%-- Mostra un messaggio di conferma se la servlet ci reindirizza qui con un parametro --%>
    <% if(request.getParameter("messaggio") != null) { %>
        <div class="messaggio"><%= request.getParameter("messaggio") %></div>
    <% } %>

    <div class="form-container">
        <form action="AggiungiProdottoServlet" method="POST" enctype="multipart/form-data">
            
            <div class="form-group">
                <label for="nome">Nome Prodotto:</label>
                <input type="text" id="nome" name="nome" placeholder="Es. Distorsore Boss DS-1" required>
            </div>

            <div class="form-group">
                <label for="prezzo">Prezzo (€):</label>
                <input type="number" id="prezzo" name="prezzo" step="any" placeholder="Es. 79.90" required>
            </div>

            <div class="form-group">
                <label for="descrizione">Descrizione:</label>
                <textarea id="descrizione" name="descrizione" rows="4" placeholder="Inserisci i dettagli del prodotto..."></textarea>
            </div>

            <div class="form-group">
                <label for="foto">Immagine Prodotto:</label>
                <input type="file" id="foto" name="foto" accept="image/*" required>
            </div>

            <button type="submit" class="btn-submit">Salva nel Database</button>
        </form>
    </div>

</body>
</html>