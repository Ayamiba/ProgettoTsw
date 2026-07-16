<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% 
    // Recuperiamo il professionista loggato in sessione
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Area Professionista</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1 style="color: #c79a00;">Area Lavoro Professionisti</h1>
        <p class="subtitle">Bentornato in studio, <%= utente.getNome() %>. Gestisci la tua coda di mix e mastering e consegna i file finali.</p>
        
        <div class="dashboard-grid">
            
            <div class="dash-col">
                
                <div class="dash-card card-pro">
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
                        <span style="text-transform: capitalize; color: #c79a00; font-weight: bold;"><%= utente.getTipo() %></span>
                    </div>
                    
                    <div class="dash-actions-list">
                        <a href="ModificaProfiloServlet" class="dash-link-action" style="color: #c79a00;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                            Modifica dati personali
                        </a>
                        <a href="CambiaPasswordServlet" class="dash-link-action" style="color: #c79a00;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
                            Aggiorna password account
                        </a>
                    </div>
                </div>

                <div class="dash-card card-pro">
                    <h3>Consegna Lavoro Finito</h3>
                    <p class="card-desc">Carica la traccia audio definitiva elaborata. Assicurati di selezionare l'ordine corrispondente prima di effettuare l'upload.</p>
                    
                    <form action="ConsegnaLavoroServlet" method="POST" enctype="multipart/form-data" id="consegnaUploadForm">
                        
                        <div class="dash-form-group" style="margin-bottom: 15px;">
                            <label for="idOrdine">Seleziona Ordine in carico:</label>
                            <select id="idOrdine" name="idOrdine" class="dash-input" style="width: 100%;">
                                <option value="" disabled selected>Nessun ordine attualmente in lavorazione</option>
                            </select>
                        </div>

                        <div class="upload-box" onclick="document.getElementById('fileFinito').click()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom: 8px; color: #c79a00;"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="17 8 12 3 7 8"></polyline><line x1="12" y1="3" x2="12" y2="15"></line></svg>
                            <p>Trascina qui il file Master/Mix o esplora</p>
                            <span class="upload-hint">WAV (24-bit/44.1kHz consigliato)</span>
                            <input type="file" id="fileFinito" name="tracciaFinita" class="file-input-hidden" accept=".wav,.aif,.mp3" onchange="/*document.getElementById('consegnaUploadForm').submit()*/">
                        </div>
                    </form>
                </div>

            </div>
            
            <div class="dash-col">
                
                <div class="dash-card card-pro">
                    <h3>Ordini in Sospeso (Da elaborare)</h3>
                    <p class="card-desc">Tracce inviate dai clienti che richiedono servizi di Mix o Mastering. Accetta un lavoro per iniziare.</p>
                    
                    <table class="dash-table">
                        <thead>
                            <tr>
                                <th>Cod. Ordine</th>
                                <th>Servizio</th>
                                <th>Data</th>
                                <th>Azione</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%-- Logica di fallback --%>
                            <tr>
                                <td colspan="4" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                    Nessuna traccia in attesa di elaborazione al momento.
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="dash-card card-pro">
                    <h3>Storico Lavori Completati</h3>
                    <p class="card-desc">Elenco delle tracce elaborate e consegnate ai clienti con successo.</p>
                    
                    <table class="dash-table">
                        <thead>
                            <tr>
                                <th>Cod. Ordine</th>
                                <th>Cliente</th>
                                <th>Data Consegna</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="4" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                    Nessun lavoro completato finora.
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>