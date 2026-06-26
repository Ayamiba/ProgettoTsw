<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Eliminazione Prodotto</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f9f9f9; }
        .box { max-width: 400px; padding: 20px; border: 1px solid #ccc; border-radius: 5px; background-color: #fff; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 8px; font-weight: bold; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn-delete { background-color: #dc3545; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; width: 100%; }
        .btn-delete:hover { background-color: #bd2130; }
        .messaggio { padding: 10px; margin-bottom: 15px; border-radius: 4px; font-weight: bold; background-color: #e2f0d9; color: #385723; border: 1px solid #bcd6a5; text-align: center; }
    </style>
</head>
<body>

    <h2>Elimina Prodotto</h2>

    <%-- Mostra il messaggio di esito (successo o errore) rimandato dalla Servlet --%>
    <% if (request.getParameter("messaggio") != null) { %>
        <div class="messaggio"><%= request.getParameter("messaggio") %></div>
    <% } %>

    <div class="box">
        <form action="EliminaProdottoServlet" method="POST" onsubmit="return confirm('Sei sicuro di voler eliminare definitivamente questo prodotto?');">
            <div class="form-group">
                <label for="idProdotto">ID Prodotto da cancellare:</label>
                <input type="number" id="idProdotto" name="idProdotto" placeholder="Inserisci l'ID (es. 5)" required>
            </div>
            <button type="submit" class="btn-delete">Elimina dal Database</button>
        </form>
    </div>

</body>
</html>