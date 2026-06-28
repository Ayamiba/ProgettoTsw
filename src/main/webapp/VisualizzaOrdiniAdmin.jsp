<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Visualizza Ordini - Admin</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f4f9; }
        h2 { color: #333; }
        .sezione-filtri { background: #eef2f7; padding: 15px; margin-bottom: 20px; border-radius: 5px; display: flex; gap: 20px; flex-wrap: wrap; align-items: flex-end; }
        .filtro-box { display: flex; flex-direction: column; gap: 5px; }
        .btn-azione { background-color: #007bff; color: white; padding: 8px 12px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-azione:hover { background-color: #0056b3; }
        .btn-reset { background-color: #6c757d; }
        .btn-reset:hover { background-color: #5a6268; }
        .ordine-box { background: white; padding: 15px; margin-bottom: 15px; border-radius: 5px; border-left: 5px solid #007bff; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 600px; }
        .ordine-id { font-weight: bold; color: #007bff; }
        .no-ordini { color: #666; font-style: italic; }
    </style>
</head>
<body>

    <h2>Pannello Controllo Ordini (Admin)</h2>

    <div class="sezione-filtri">
        <form action="VisualizzaOrdiniAdminServlet" method="POST">
            <input type="hidden" name="azione" value="tutti">
            <button type="submit" class="btn-azione btn-reset">Mostra Tutti gli Ordini</button>
        </form>

        <form action="VisualizzaOrdiniAdminServlet" method="POST" class="sezione-filtri" style="padding:0; margin:0; background:none;">
            <input type="hidden" name="azione" value="filtraDate">
            <div class="filtro-box">
                <label>Da data:</label>
                <input type="date" name="dataInizio" required>
            </div>
            <div class="filtro-box">
                <label>A data:</label>
                <input type="date" name="dataFine" required>
            </div>
            <button type="submit" class="btn-azione">Filtra per Data</button>
        </form>

        <form action="VisualizzaOrdiniAdminServlet" method="POST" class="sezione-filtri" style="padding:0; margin:0; background:none;">
            <input type="hidden" name="azione" value="filtraCliente">
            <div class="filtro-box">
                <label>ID/Email Cliente:</label>
                <input type="text" name="idCliente" placeholder="Inserisci identificativo" required>
            </div>
            <button type="submit" class="btn-azione">Filtra per Cliente</button>
        </form>
    </div>

    <hr>

    <h3>Risultati Ricerca</h3>

    <% 
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
        <p class="no-ordini">Nessun ordine corrisponde ai filtri impostati.</p>
<% 
    } else {
%>
        <p class="no-ordini">Utilizza i filtri in alto per caricare la lista degli ordini desiderata.</p>
<% 
    } 
%>

</body>
</html>