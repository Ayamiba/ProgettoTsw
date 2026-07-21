<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="model.ordine.OrdineDAO" %>
<%@ page import="java.util.List" %>
<% 
    // Recuperiamo l'amministratore loggato in sessione
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave - Pannello di Amministrazione</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1 style="color: #d9534f;">Pannello di Amministrazione</h1>
        <p class="subtitle">Accesso di livello Root. Modifica catalogo, supervisiona gli utenti e gestisci tutti gli ordini globali.</p>
        
        <div class="dashboard-grid">
            
            <div style="display: flex; flex-direction: column; gap: 30px;">
                
                <div class="dash-card card-admin">
                    <h3>Informazioni Personali</h3>
                    
                    <div class="profile-info-block">
                        <strong>Nome e Cognome</strong>
                        <span><%= utente.getNome() %> <%= utente.getCognome() %></span>
                    </div>
                    
                    <div class="profile-info-block">
                        <strong>Indirizzo Email</strong>
                        <span><%= utente.getEmail() %></span>
                    </div>
                    
                    <div class="profile-info-block">
                        <strong>Data di Nascita</strong>
                        <span><%= (utente.getDataNascita() != null) ? utente.getDataNascita().toString() : "Non specificata" %></span>
                    </div>
                    
                    <div class="profile-info-block">
                        <strong>Livello Profilo</strong>
                        <span style="text-transform: capitalize; color: #d9534f; font-weight: bold;"><%= utente.getTipo() %></span>
                    </div>
                    
                    <div class="dash-actions-list">
                        <a href="ModificaProfiloServlet" class="dash-link-action" style="color: #d9534f;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                            Modifica dati personali
                        </a>
                        <a href="CambiaPasswordServlet" class="dash-link-action" style="color: #d9534f;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
                            Aggiorna password account
                        </a>
                    </div>
                </div>

                <div class="dash-card card-admin">
                    <h3>Gestione Utenti e Staff</h3>
                    <p style="font-size: 0.9em; color: #666; margin-bottom: 20px;">Promuovi gli utenti a "Professionisti", visualizza le anagrafiche o banna gli account non conformi.</p>
                    
                    <div style="background: #f8f9fa; border: 1px solid #eef0f2; padding: 14px; border-radius: 6px; display: flex; align-items: center; justify-content: space-between;">
                        <a href="GestioneUtentiServlet" class="dash-btn-save" style="text-decoration: none; display: inline-flex; width: auto; padding: 8px 15px; margin: 0; height: auto; background-color: #333;">Apri Lista</a>                    
                    </div>
                </div>

            </div>
            
            <div style="display: flex; flex-direction: column; gap: 30px;">
                
                <div class="dash-card card-admin">
                    <h3>Gestione Catalogo</h3>
                    <p style="font-size: 0.9em; color: #666; margin-bottom: 20px;">Aggiungi, modifica o rimuovi i prodotti (Plugin, Strumenti Virtuali) e le categorie dal catalogo e-commerce.</p>
                    
                    <div class="dash-actions-list" style="margin-top: 10px; border-top: none; padding-top: 0;">
                        <a href="#" class="dash-btn-save" style="text-decoration: none; text-align: center; margin-bottom: 10px;">+ Aggiungi Nuovo Prodotto</a>
                        <a href="#" class="dash-btn-cancel" style="text-decoration: none; text-align: center;">Modifica Prodotti Esistenti</a>
                    </div>
                </div>

                <div class="dash-card card-admin">
                    <h3>Registro Ordini Globale (Ultimi 5)</h3>
                    <p style="font-size: 0.9em; color: #666; margin-bottom: 15px;">Visualizza gli ordini più recenti effettuati sulla piattaforma.</p>
                    
                    <table class="dash-table">
                        <thead>
                            <tr>
                                <th>Cod.</th>
                                <th>Data</th>
                                <th>Stato</th>
                                <th>Totale</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                OrdineDAO ordineDAO = new OrdineDAO();
                                List<OrdineBean> tuttiOrdini = ordineDAO.doRetrieveAll(); // Recupera tutti gli ordini
                                
                                if (tuttiOrdini == null || tuttiOrdini.isEmpty()) {
                            %>
                                <tr>
                                    <td colspan="4" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                        Nessun ordine attualmente registrato nel sistema.
                                    </td>
                                </tr>
                            <%  } else { 
                                    int count = 0;
                                    for(OrdineBean ordine : tuttiOrdini) { 
                                        if(count >= 5) break; // Mostra solo gli ultimi 5 nella dashboard principale
                                        count++;
                                        
                                        // Assegna il colore del badge in base allo stato
                                        String statoClasse = "pending";
                                        if ("Completato".equalsIgnoreCase(ordine.getStato())) statoClasse = "ready";
                                        else if ("In Lavorazione".equalsIgnoreCase(ordine.getStato())) statoClasse = "working";
                            %>
                                <tr>
                                    <td style="font-weight: bold;">#<%= ordine.getIdOrdine() %></td>
                                    <td><%= ordine.getDataOrdine() %></td>
                                    <td>
                                        <span class="status-badge <%= statoClasse %>"><%= ordine.getStato() %></span>
                                    </td>
                                    <td style="font-weight: 600;">€ <%= String.format("%.2f", ordine.getTotale()) %></td>
                                </tr>
                            <%      } 
                                } %>
                        </tbody>
                    </table>
                    
                    <div style="margin-top: 15px; text-align: right;">
    					<a href="GestioneOrdiniServlet" class="dash-link-action" style="color: #d9534f; display: inline-flex; justify-content: flex-end;">
        					Vedi storico completo &rarr;
    					</a>
					</div>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>