<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Visualizza Ordini - Admin</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css">
</head>
<body>
	<%@ include file="/WEB-INF/views/components/navbar.jsp" %>

   <div class="contenuti-admin">
        
        <div class="admin-header">
            <h1>Pannello Gestione Admin: Visualizza Ordini</h1>
        </div>

        <div class="corpo-admin">
            
           <div class="suggerimenti-immagine" style="border-left-color: #007bff;">
                <h3 style="font-size: 1.1em; margin-bottom: 8px; color: #007bff; font-weight: 500;">Aggiornamento Dati Catalogo</h3>
                <p>• Qui puoi visualizzare gli ordini</p>
                <p>• Applica il filtro in base alla data o all'id dell'utente </p>
            </div>

            <div class="filtri-grid">
                
                <div class="filtro-sezione" style="display: flex; align-items: center;">
                    <form action="VisualizzaOrdiniAdminServlet" method="POST" style="width: 100%;">
                        <input type="hidden" name="azione" value="tutti">
                        <p style="font-size: 0.9em; color: #666; margin-top: 0;">Visualizza l'intero catalogo degli ordini registrati nel sistema.</p>
                        <button type="submit" class="btn-brand btn-secondary">Mostra Tutti gli Ordini</button>
                    </form>
                </div>

                <div class="filtro-sezione">
                    <form action="VisualizzaOrdiniAdminServlet" method="POST">
                        <input type="hidden" name="azione" value="filtraDate">
                        <div class="form-group">
                            <label>Da data:</label>
                            <input type="date" name="dataInizio" required>
                        </div>
                        <div class="form-group">
                            <label>A data:</label>
                            <input type="date" name="dataFine" required>
                        </div>
                        <button type="submit" class="btn-brand">Filtra per Data</button>
                    </form>
                </div>

                <div class="filtro-sezione">
                    <form action="VisualizzaOrdiniAdminServlet" method="POST">
                        <input type="hidden" name="azione" value="filtraCliente">
                        <div class="form-group">
                            <label>ID/Email Cliente:</label>
                            <input type="text" name="idCliente" placeholder="Es. mario.rossi@email.it" required>
                        </div>
                        <button type="submit" class="btn-brand">Filtra per Cliente</button>
                    </form>
                </div>

            </div>

            <h2 style="font-family: 'Jost', sans-serif; color: #333; font-size: 1.5em; margin-bottom: 20px;">Risultati Ricerca</h2>

            <div class="ordini-container">
                <% 
                java.util.List<model.ordine.OrdineBean> listaOrdini = (java.util.List<model.ordine.OrdineBean>) request.getAttribute("listaOrdini");
                
                if (listaOrdini != null && !listaOrdini.isEmpty()) {
                    for (model.ordine.OrdineBean ordine : listaOrdini) {
                %>
                        <div class="ordine-card">
                            <div class="ordine-header">
                                Ordine ID: <%= ordine.getIdOrdine() %>
                            </div>
                            <div class="ordine-dettaglio">
                                <strong>Data:</strong> <%= ordine.getDataOrdine() %>
                            </div>
                            <div class="ordine-dettaglio">
                                <strong>Stato:</strong> <%= ordine.getStato() %>
                            </div>
                            <div class="ordine-dettaglio">
                                <strong>Prezzo Totale:</strong> <%= String.format("%.2f", ordine.getTotale()) %> &euro;
                            </div>
                            <div class="ordine-dettaglio">
                                <strong>Descrizione:</strong> <%= ordine.getDescrizione() %>
                            </div>
                            <hr>
                        </div>
                <% 
                    }
                } else if (request.getMethod().equalsIgnoreCase("POST")) {
                %>
                    <div class="no-ordini">Nessun ordine corrisponde ai filtri impostati. Modifica i parametri e riprova.</div>
                <% 
                } else {
                %>
                    <div class="no-ordini">Utilizza i filtri in alto per avviare una ricerca e caricare la lista degli ordini.</div>
                <% 
                } 
                %>
            </div>

        </div> </div>
</body>
</html>