<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.utente.UtenteBean" %>
<%@ page import="model.prodotto.ProdottoBean" %>
<%@ page import="model.tracciaAudio.TracciaAudioBean" %>
<%@ page import="model.tracciaAudio.TracciaAudioDAO" %>
<%@ page import="model.metodoPagamento.MetodoPagamentoBean" %>
<%@ page import="model.metodoPagamento.MetodoPagamentoDAO" %>
<%
    UtenteBean utente = (UtenteBean) session.getAttribute("user");
    List<ProdottoBean> carrello = (List<ProdottoBean>) session.getAttribute("carrelloProdotti");
    
    float totale = 0.0f;
    if (carrello != null) {
        for (ProdottoBean p : carrello) { totale += p.getPrezzo(); }
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave - Completa il tuo Ordine</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <script src="<%= request.getContextPath() %>/js/Checkout.js"></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        <h1>Configurazione Ordine</h1>
        <p class="subtitle">Imposta la tua catena effetti, carica la traccia e completa il pagamento in modo sicuro.</p>
        
        <% 
            // 1. Controllo Carrello Vuoto
            if (carrello == null || carrello.isEmpty()) { 
        %>
            <div class="empty-data-box mt-30">
                <span>Il tuo carrello è vuoto. <a href="CatalogoServlet">Torna al catalogo</a> per aggiungere dei plugin.</span>
            </div>
            
        <% 
            // 2. Controllo Utente Non Registrato
            } else if (utente == null) { 
        %>
            <div class="dash-card" style="text-align: center; padding: 60px 20px; margin-top: 30px;">
                <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="color: var(--colore-primario); margin-bottom: 20px;"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                <h2 style="margin-bottom: 15px;">Quasi fatto!</h2>
                <p style="color: #666; max-width: 500px; margin: 0 auto 30px auto;">
                    Per completare l'ordine, inviare la traccia ai nostri professionisti e ricevere il file elaborato nel tuo cloud privato, devi avere un account Sændwave.
                </p>
                <div style="display: flex; justify-content: center; gap: 20px;">
                    <a href="LoginServlet" class="dash-btn-save" style="text-decoration: none; padding: 12px 35px; width: auto; margin: 0;">Accedi</a>
                    <a href="RegistrazioneServlet" class="dash-btn-cancel" style="text-decoration: none; padding: 12px 35px; width: auto; margin: 0;">Crea un Account</a>
                </div>
            </div>

        <% 
            // 3. Utente Loggato e Carrello Pieno -> Mostriamo il form!
            } else { 
        %>
        
        <form action="CheckoutServlet" method="POST" enctype="multipart/form-data">
            <div class="checkout-grid">
                
                <!-- COLONNA SINISTRA: CONFIGURAZIONE TECNICA -->
                <div class="dash-col">
                    
                    <!-- 1. CATENA AUDIO -->
                    <div class="dash-card">
                        <h3>1. Routing e Catena Audio</h3>
                        <p class="card-desc">Assegna un numero (1, 2, 3...) per indicare al fonico l'ordine esatto in cui applicare i plugin alla tua traccia.</p>
                        
                        <% 
                            int counter = 1;
                            for (ProdottoBean p : carrello) { 
                        %>
                            <div class="plugin-chain-item">
                                <div class="plugin-chain-info">
                                    <img src="<%= p.getImmagine() %>" alt="plugin" onerror="this.src='img/placeholder.png'">
                                    <div>
                                        <span class="plugin-title"><%= p.getNome() %></span>
                                        
                                    </div>
                                </div>
                                <div>
                                    <label class="pos-label">Posizione:</label>
                                    <input type="number" name="posizione_<%= p.getIdProdotto() %>" class="pos-input" value="<%= counter %>" min="1" max="99" required>
                                </div>
                            </div>
                        <% 
                            counter++;
                            } 
                        %>
                    </div>

                    <!-- 2. TRACCIA E ISTRUZIONI -->
                    <div class="dash-card">
                        <h3>2. Sorgente Audio & Istruzioni</h3>
                        
                        <div class="dash-form-group mb-25">
                            <label class="checkout-label">Istruzioni per il Mixing/Mastering (Opzionale)</label>
                            <textarea name="descrizioneOrdine" class="dash-input input-full" rows="4" placeholder="Es: 'Vorrei un riverbero molto profondo sulla voce'"></textarea>
                        </div>
                        
                        <label class="checkout-label">Traccia da elaborare</label>
                        
                        <% 
                            boolean hasCloudTracks = false;
                            TracciaAudioDAO tracciaDAO = new TracciaAudioDAO();
                            List<TracciaAudioBean> tracceUtente = tracciaDAO.doRetrieveByUtente(utente.getEmail());
                            
                            if (tracceUtente != null && !tracceUtente.isEmpty()) {
                                hasCloudTracks = true;
                        %>
                                <div class="checkout-option-box highlight">
                                    <label class="radio-label">
                                        <input type="radio" name="sorgenteTraccia" value="cloud" checked onchange="toggleTracciaSource(this.value)">
                                        <strong>Scegli dal tuo Sændwave Cloud</strong>
                                    </label>
                                    <select name="idTracciaCloud" id="selectCloud" class="dash-input input-full">
                                        <% for(TracciaAudioBean t : tracceUtente) { %>
                                            <option value="<%= t.getIdTraccia() %>"><%= t.getNomeFile() %></option>
                                        <% } %>
                                    </select>
                                </div>
                        <% 
                            } 
                        %>
                        
                        <div class="checkout-option-box">
                            <% if (hasCloudTracks) { %>
                                <label class="radio-label mb-25">
                                    <input type="radio" name="sorgenteTraccia" value="nuova" onchange="toggleTracciaSource(this.value)">
                                    <strong>Carica una nuova traccia ora</strong>
                                </label>
                            <% } else { %>
                                <input type="hidden" name="sorgenteTraccia" value="nuova">
                                <p class="checkout-hint">Nessuna traccia in archivio. Seleziona il file da inviare al fonico.</p>
                            <% } %>
                            
                            <!-- Box nascosto di default se l'utente ha il Cloud attivo -->
                            <div id="uploadBoxNuova" class="checkout-upload-area <%= hasCloudTracks ? "d-none" : "" %>">
                                <p class="checkout-hint">Seleziona file audio (WAV, MP3)</p>
                                <input type="file" name="nuovaTracciaFile" accept="audio/*" class="input-full">
                            </div>
                        </div>

                    </div>
                </div>
                
                <!-- COLONNA DESTRA: RIEPILOGO E PAGAMENTO -->
                <div class="dash-col">
                    <div class="dash-card">
                        <h3>3. Pagamento</h3>
                        
                        <div class="totals-box">
                            <div class="totals-row">
                                <span>Totale Servizi:</span>
                                <strong>€ <%= String.format("%.2f", totale) %></strong>
                            </div>
                            <div class="totals-row final-total">
                                <span>Da Pagare:</span>
                                <span>€ <%= String.format("%.2f", totale) %></span>
                            </div>
                        </div>

                        <label class="checkout-label">Metodo di Pagamento</label>
                        
                        <% 
                            MetodoPagamentoDAO pagDAO = new MetodoPagamentoDAO();
                            List<MetodoPagamentoBean> carte = pagDAO.doRetrieveByUtente(utente.getEmail());
                            
                            if (carte != null && !carte.isEmpty()) {
                        %>
                                <select name="numeroCarta" class="dash-input input-full mb-25" required>
                                    <% for(MetodoPagamentoBean c : carte) { 
                                        String num = String.valueOf(c.getNumeroCarta());
                                        String ultime = num.substring(Math.max(0, num.length() - 4));
                                    %>
                                        <option value="<%= num %>">Carta term. in <%= ultime %> (<%= c.getNome() %>)</option>
                                    <% } %>
                                </select>
                                
                                <button type="submit" class="dash-btn-save btn-checkout">
                                    Conferma Ordine e Paga
                                </button>
                        <% 
                            } else {
                        %>
                                <p class="checkout-hint" style="color: #d9534f; margin-bottom: 20px;">Non hai metodi di pagamento salvati. Devi aggiungerne uno prima di procedere.</p>
                                <a href="MetodiPagamentoServlet?azione=aggiungi" class="dash-btn-save btn-checkout" style="text-decoration: none;">Aggiungi Carta</a>
                        <% 
                            }
                        %>
                        
                    </div>
                </div>

            </div>
        </form>
        <% } %>
    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>