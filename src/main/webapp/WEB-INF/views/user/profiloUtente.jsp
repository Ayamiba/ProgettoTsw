<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% 
    // Recuperiamo l'utente loggato in sessione per estrarre i dati anagrafici
    UtenteBean utente = (UtenteBean) session.getAttribute("user"); 
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Il Mio Profilo</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
    
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1>Il Mio Account</h1>
        <p class="subtitle">Gestisci le tue informazioni personali, i metodi di pagamento e il tuo archivio audio.</p>
        
        <div class="dashboard-grid">
            
            <div style="display: flex; flex-direction: column; gap: 30px;">
                
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
                    <p style="font-size: 0.9em; color: #666; margin-bottom: 15px;">Configura le tue carte per accelerare le operazioni di acquisto licenze durante il checkout.</p>
                    
                    <div style="background: #f8f9fa; border: 1px solid #eef0f2; padding: 14px; border-radius: 6px; display: flex; align-items: center; gap: 12px;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="color: #888;"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect><line x1="1" y1="10" x2="23" y2="10"></line></svg>
                        <span style="font-size: 0.9em; color: #555; font-weight: 500;">Nessun metodo di pagamento salvato</span>
                    </div>
                    
                    <div class="dash-actions-list">
                        <a href="MetodiPagamentoServlet?azione=aggiungi" class="dash-link-action">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                            Aggiungi carta di credito/debito
                        </a>
                    </div>
                </div>

            </div>
            
            <div style="display: flex; flex-direction: column; gap: 30px;">
                
                <div class="dash-card card-user">
                    <h3>Sændwave Audio Cloud</h3>
                    <p style="font-size: 0.9em; color: #666; margin-bottom: 20px;">Carica in questo spazio i tuoi sample, stems o tracce grezze. I file rimarranno memorizzati nel tuo archivio privato personali per futuri utilizzi, indipendentemente dall'apertura di un ordine commerciale.</p>
                    
                    <form action="CaricaTracciaLiberaServlet" method="POST" enctype="multipart/form-data" id="audioUploadForm">
                        <div class="upload-box" onclick="document.getElementById('audioFile').click()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="17 8 12 3 7 8"></polyline><line x1="12" y1="3" x2="12" y2="15"></line></svg>
                            <p>Trascina qui il tuo file o clicca per esplorare le cartelle</p>
                            <span style="font-size: 0.8em; color: #888;">Formati supportati: WAV, AIF, MP3 (Max 50MB)</span>
                            <input type="file" id="audioFile" name="tracciaAudio" class="file-input-hidden" accept="audio/*" onchange="document.getElementById('audioUploadForm').submit()">
                        </div>
                    </form>
                    
                    <div class="cloud-tracks-list">
                        <h4 style="font-size: 0.95em; color: #1a1a2e; margin-bottom: 12px; font-weight: 600;">I tuoi file in Cloud</h4>
                        
                        <%-- Struttura condizionale pronta per quando collegherai il CloudAudioDAO --%>
                        <p style="font-size: 0.9em; color: #aaa; font-style: italic; margin: 0;">L'archivio cloud è vuoto. Carica la tua prima traccia audio.</p>
                    </div>
                </div>

                <div class="dash-card card-user">
                    <h3>Storico Ordini & Licenze</h3>
                    <table class="dash-table">
                        <thead>
                            <tr>
                                <th>Codice</th>
                                <th>Data</th>
                                <th>Stato</th>
                                <th>Totale</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%-- Logica di fallback iniziale in attesa dei dati reali dal Database --%>
                            <tr>
                                <td colspan="4" style="text-align: center; color: #aaa; font-style: italic; padding: 25px 0;">
                                    Nessun ordine presente nello storico account.
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