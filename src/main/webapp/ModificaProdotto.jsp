<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Modifica Prodotto</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f9f9f9; }
        .form-container { max-width: 500px; padding: 20px; border: 1px solid #ccc; border-radius: 5px; background-color: #fff; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group textarea { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn-submit { background-color: #007bff; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; width: 100%; font-size: 16px; }
        .btn-submit:hover { background-color: #0056b3; }
        .messaggio { padding: 10px; margin-bottom: 15px; border-radius: 4px; font-weight: bold; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    </style>
</head>
<body>

    <h2>Test Modifica Prodotto (Inserimento Manuale ID)</h2>
    
    <%-- Gestione dinamica dei messaggi di successo o errore inviati dalla Servlet --%>
    <% 
        String msg = request.getParameter("messaggio");
        if (msg != null) { 
            if (msg.contains("Errore")) {
    %>
                <div class="messaggio error"><%= msg %></div>
    <% 
            } else {
    %>
                <div class="messaggio success"><%= msg %></div>
    <% 
            }
        } 
    %>

    <div class="form-container">
        <form action="ModificaProdottoServlet" method="POST" enctype="multipart/form-data">
            
            <div class="form-group">
                <label for="idProdotto">ID del Prodotto da Modificare (Esistente nel DB):</label>
                <input type="number" id="idProdotto" name="idProdotto" placeholder="Es. 1" required>
            </div>

            <div class="form-group">
                <label for="nome">Nuovo Nome:</label>
                <input type="text" id="nome" name="nome" placeholder="Inserisci il nuovo nome" required>
            </div>

            <div class="form-group">
                <label for="prezzo">Nuovo Prezzo (€):</label>
                <input type="number" id="prezzo" name="prezzo" step="any" placeholder="Es. 49.99" required>
            </div>

            <div class="form-group">
                <label for="descrizione">Nuova Descrizione:</label>
                <textarea id="descrizione" name="descrizione" rows="4" placeholder="Aggiorna la descrizione qui..."></textarea>
            </div>

            <div class="form-group">
                <label for="foto">Nuova Immagine (Lascia vuoto per mantenere la vecchia):</label>
                <input type="file" id="foto" name="foto" accept="image/*">
            </div>

            <button type="submit" class="btn-submit">Salva Modifiche nel DB</button>
        </form>
    </div>

</body>
</html>