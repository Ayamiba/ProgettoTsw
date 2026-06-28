<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Visualizza Ordini - Admin</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f4f9; }
        h2 { color: #333; }
        .btn-carica { background-color: #007bff; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-bottom: 20px; }
        .btn-carica:hover { background-color: #0056b3; }
        .ordine-box { background: white; padding: 15px; margin-bottom: 15px; border-radius: 5px; border-left: 5px solid #007bff; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 600px; }
        .ordine-id { font-weight: bold; color: #007bff; }
        .no-ordini { color: #666; font-style: italic; }
    </style>
</head>
<body>

    <h2>Pannello Controllo Ordini (Admin)</h2>

    <form action="VisualizzaOrdiniAdminServlet" method="POST">
        <button type="submit" class="btn-carica">Carica / Aggiorna Ordini</button>
    </form>

    <hr>

    <h3>Lista degli Ordini Ricevuti</h3>

    <% 
    // Recuperiamo la lista usando il percorso completo strutturato della classe
    java.util.List<model.ordine.OrdineBean> listaOrdini = (java.util.List<model.ordine.OrdineBean>) request.getAttribute("listaOrdini");
    
    if (listaOrdini != null && !listaOrdini.isEmpty()) {
        for (model.ordine.OrdineBean ordine : listaOrdini) {
%>
            <div class="ordine-box">
                <div class="ordine-id">Ordine ID: <%= ordine.getIdOrdine() %></div>
                <div><strong>Data:</strong> <%= ordine.getDataOrdine() %></div>
                <div><strong>Prezzo Totale:</strong> <%= ordine.getTotale() %> €</div>
                <div><strong>Stato:</strong> <%= ordine.getStato() %></div>
                <div><strong>Descrizione:</strong> <%= ordine.getDescrizione() %></div>
            </div>
<% 
        }
    } else if (request.getMethod().equalsIgnoreCase("POST")) {
%>
        <p class="no-ordini">Nessun ordine presente nel database.</p>
<% 
    } else {
%>
        <p class="no-ordini">Clicca sul bottone sopra per caricare gli ordini.</p>
<% 
    } 
%>

</body>
</html>