<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.metodoPagamento.MetodoPagamentoBean" %>
<%@ page import="model.metodoPagamento.MetodoPagamentoDAO" %>
<%@ page import="model.tracciaAudio.TracciaAudioBean" %>
<%@ page import="model.tracciaAudio.TracciaAudioDAO" %>
<%@ page import="model.ordine.OrdineBean" %>
<%@ page import="model.ordine.OrdineDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="model.recensione.RecensioneBean" %>
<%@ page import="model.recensione.RecensioneDAO" %>
<%@ page import="java.util.ArrayList" %>
<% 
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
	//Recupero recensioni effettuate dall'utente
	RecensioneDAO recensioneDAO = new RecensioneDAO();
	List<RecensioneBean> tutteRecensioni = recensioneDAO.doRetrieveAll();
	List<RecensioneBean> recensioniUtente = new ArrayList<>();
	if (tutteRecensioni != null && utente != null) {
    	for (RecensioneBean r : tutteRecensioni) {
        	if (utente.getEmail() != null && utente.getEmail().equalsIgnoreCase(r.getFkUtente())) {
            	recensioniUtente.add(r);
        }
    }
}
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Il Mio Profilo</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
    <script src="<%= request.getContextPath() %>/js/AscoltaAudio.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1>Il Mio Account</h1>
        <p class="subtitle">Gestisci le tue informazioni personali, i metodi di pagamento e il tuo archivio audio.</p>
        
        <div class="dashboard-grid">
            
            <div class="dash-col">
                
                <div class="dash-card card-user">
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
                        <span style="text-transform: capitalize;"><%= utente.getTipo() %></span>
                    </div>
                    
                    <div class="dash-actions-list">
                        <a href="ModificaProfiloServlet" class="dash-link-action">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                            Modifica dati personali
                        </a>
                        <a href="CambiaPasswordServlet" class="dash-link-action">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
                            Aggiorna password account
                        </a>
                    </div>
                </div>

                <div class="dash-card card-user">
                    <h3>Metodi di Pagamento</h3>
                    <p class="card-desc">Configura le tue carte per accelerare le operazioni di acquisto licenze durante il checkout.</p>
                    
                    <%
                        MetodoPagamentoDAO pagDAO = new MetodoPagamentoDAO();
                        List<MetodoPagamentoBean> carteUtente = pagDAO.doRetrieveByUtente(utente.getEmail());
                        
                        if (carteUtente == null || carteUtente.isEmpty()) { 
                    %>
                        <div class="empty-data-box">
                            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect><line x1="1" y1="10" x2="23" y2="10"></line></svg>
                            <span>Nessun metodo di pagamento salvato</span>
                        </div>
                    <% } else { %>
                        <div class="payment-list">
                            <% for(MetodoPagamentoBean carta : carteUtente) { 
                                String numCarta = String.valueOf(carta.getNumeroCarta());
                                String ultimeQuattro = numCarta.substring(Math.max(0, numCarta.length() - 4));
                            %>
                                <div class="payment-item">
                                    <div class="payment-info">
                                        <div class="payment-icon-wrapper">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect><line x1="1" y1="10" x2="23" y2="10"></line></svg>
                                        </div>
                                        <div>
                                            <div class="payment-text-title">Carta terminante in <%= ultimeQuattro %></div>
                                            <div class="payment-text-sub">Scadenza: <%= carta.getScadenza() %> • <%= carta.getNome() %> <%= carta.getCognome() %></div>
                                        </div>
                                    </div>
                                    <a href="#" class="btn-text-danger">Rimuovi</a>
                                </div>
                            <% } %>
                        </div>
                    <% } %>
                    
                    <div class="dash-actions-list">
                        <a href="MetodiPagamentoServlet?azione=aggiungi" class="dash-link-action">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                            Aggiungi carta di credito/debito
                        </a>
                    </div>
                </div>

            </div>
            
            <div class="dash-col">
                
               <div class="dash-card card-user">
                    <h3>Sændwave Audio Cloud</h3>
                    <p class="card-desc">Carica in questo spazio i tuoi sample, stems o tracce grezze. I file rimarranno memorizzati nel tuo archivio privato personale per futuri utilizzi, indipendentemente dall'apertura di un ordine commerciale.</p>
                    
                    <form action="CaricaTracciaLiberaServlet" method="POST" enctype="multipart/form-data" id="audioUploadForm">
                        <div class="upload-box" onclick="document.getElementById('audioFile').click()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="17 8 12 3 7 8"></polyline><line x1="12" y1="3" x2="12" y2="15"></line></svg>
                            <p>Trascina qui il tuo file o clicca per esplorare</p>
                            <span class="upload-hint">WAV, AIF, MP3 (Max 50MB)</span>
                            <input type="file" id="audioFile" name="tracciaAudio" class="file-input-hidden" accept="audio/*" onchange="document.getElementById('audioUploadForm').submit()">
                        </div>
                    </form>
                    
                    <div class="cloud-tracks-list">
                        <h4 style="font-size: 0.95em; color: #1a1a2e; margin-bottom: 12px; font-weight: 600;">I tuoi file in Cloud</h4>
                        
                       <%
                            TracciaAudioDAO tracciaDAO = new TracciaAudioDAO();
                            List<TracciaAudioBean> tracceUtente = tracciaDAO.doRetrieveByUtente(utente.getEmail());
                            
                            if (tracceUtente == null || tracceUtente.isEmpty()) {
                        %>
                            <p class="card-desc" style="font-style: italic; margin: 0;">L'archivio cloud è vuoto. Carica la tua prima traccia audio.</p>
                        <% } else { %>
                            <div class="payment-list"> <% for(TracciaAudioBean traccia : tracceUtente) { %>
                                    <div class="cloud-track-item">
                                        <div class="cloud-track-info">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#6f42c1" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                                <path d="M9 18V5l12-2v13"></path>
                                                <circle cx="6" cy="18" r="3"></circle>
                                                <circle cx="18" cy="16" r="3"></circle>
                                            </svg>
                                            <span class="cloud-track-name" title="<%= traccia.getNomeFile() %>">
                                                <%= traccia.getNomeFile() %>
                                            </span>
                                        </div>
                                        <div class="cloud-track-actions">
                                            <button type="button" class="btn-play-audio" data-file="<%= traccia.getPercorsoFile() %>">
                                                <span class="play-icon">▶</span>
                                            </button>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                        <% } %>
                        
                        <audio id="globalAudioPlayer" style="display: none;"></audio>
                        
                    </div>
                </div>

				<div class="dash-card card-user">
                    <h3>Storico Ordini</h3>
                    <div class="dash-table-wrapper">
                    	<table class="dash-table">
                        	<thead>
                            	<tr>
                                	<th>Codice</th>
                                	<th>Data</th>
                                	<th>Stato</th>
                                	<th>Totale</th>
                                	<th style="text-align: center;">Recensione</th>
                                	<th style="text-align: center;">Download</th>
                                	<th style="text-align: center;">PDF</th>
                            	</tr>
                        	</thead>
                        	<tbody>
                            <%
                                OrdineDAO ordineDAO = new OrdineDAO();
                                List<OrdineBean> ordiniUtente = ordineDAO.doRetrieveByUtente(utente.getEmail());
                                
                                if (ordiniUtente == null || ordiniUtente.isEmpty()) {
                            %>
                                <tr>
                                    <td colspan="6" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                        Nessun ordine presente nello storico account.
                                    </td>
                                </tr>
                            <% } else { 
                                int limiteIniziale = 3; // Mostra i primi 3 ordini
                                for(int i = 0; i < ordiniUtente.size(); i++) { 
                                    OrdineBean ordine = ordiniUtente.get(i);
                                    String statoClasse = "pending";
                                    if ("Completato".equalsIgnoreCase(ordine.getStato())) statoClasse = "ready";
                                    else if ("In Lavorazione".equalsIgnoreCase(ordine.getStato())) statoClasse = "working";
                                    
                                    String rowClass = (i >= limiteIniziale) ? "order-row-hidden" : "";
                            %>
                                <tr class="<%= rowClass %>">
                                    <td style="font-weight: bold;">#<%= ordine.getIdOrdine() %></td>
                                    <td><%= ordine.getDataOrdine() %></td>
                                    <td><span class="status-badge <%= statoClasse %>"><%= ordine.getStato() %></span></td>
                                    <td style="font-weight: 600;">€ <%= String.format("%.2f", ordine.getTotale()) %></td>
                                    <td style="text-align: center;">
                                        <% if ("Completato".equalsIgnoreCase(ordine.getStato())) { %>
                                            <a href="DownloadLavoroServlet?idOrdine=<%= ordine.getIdOrdine() %>" class="btn-text-success" style="font-weight: 600; color: #28a745; text-decoration: none;">🎵 Scarica</a>
                                        <% } else { %>
                                            <span style="color: #888; font-style: italic; font-size: 0.85em;">In corso...</span>
                                        <% } %>
                                    </td>
                                    <td style="text-align: center;">
                                        <a href="GeneraFatturaServlet?id=<%= ordine.getIdOrdine() %>" class="dash-btn-download" title="Scarica Ricevuta">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
                                        </a>
                                    </td>
                                     <!-- COLONNA RECENSIONE -->
                    <td style="text-align: center;">
                        <% if ("Completato".equalsIgnoreCase(ordine.getStato())) { %>
                            <a href="InviaRecensioneServlet?idOrdine=<%= ordine.getIdOrdine() %>" 
                               style="background-color: #6f42c1; color: white; padding: 6px 12px; text-decoration: none; border-radius: 4px; font-size: 0.85em; font-weight: bold; white-space: nowrap;">
                                Lascia Recensione
                            </a>
                        <% } else { %>
                            <span style="color: #ccc;">–</span>
                        <% } %>
                    </td>
                                </tr>
                            <%  } 
                               } %>
                        </tbody>
                    </table>
                    </div>

                    <% if (ordiniUtente != null && ordiniUtente.size() > 3) { %>
                        <div style="text-align: center; margin-top: 10px;">
                            <button type="button" id="btnToggleOrders" class="btn-show-more" onclick="toggleOrders()">
                                Mostra altri ordini (<%= ordiniUtente.size() - 3 %>)
                            </button>
                        </div>
                    <% } %>

                    <!-- SEPARATORE TRA ORDINI E RECENSIONI -->
                    <hr class="card-divider">

                    <!-- SEZIONE RECENSIONI EFFETTUATE -->
                    <h3>Storico Recensioni</h3>
                    <% if (recensioniUtente == null || recensioniUtente.isEmpty()) { %>
                        <p class="card-desc" style="font-style: italic; margin-bottom: 0;">Non hai ancora rilasciato alcuna recensione.</p>
                    <% } else { %>
                        <div class="reviews-inline-list">
                            <% for(RecensioneBean rec : recensioniUtente) { %>
                                <div class="review-inline-item">
                                    <div class="review-inline-content">
                                        <div class="review-inline-header">
                                            <strong>
                                                <% if (rec.getFkOrdine() != null && rec.getFkOrdine() > 0) { %>
                                                    Ordine #<%= rec.getFkOrdine() %>
                                                <% } else { %>
                                                    Recensione
                                                <% } %>
                                            </strong>
                                            <span class="review-stars">
                                                <% for(int s=0; s<rec.getVoto(); s++) { %>★<% } %><% for(int s=rec.getVoto(); s<5; s++) { %>☆<% } %>
                                            </span>
                                            <span class="review-date-sm"><%= rec.getDataRecensione() %></span>
                                        </div>
                                        <% if (rec.getCommento() != null && !rec.getCommento().trim().isEmpty()) { %>
                                            <p class="review-comment-sm"><%= rec.getCommento() %></p>
                                        <% } %>
                                    </div>
                                    <a href="EliminaRecensioneServlet?id=<%= rec.getIdRecensione() %>" 
                                       class="btn-delete-review" 
                                       onclick="return confirm('Vuoi davvero eliminare questa recensione?');"
                                       title="Elimina recensione">&times;</a>
                                </div>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html> 