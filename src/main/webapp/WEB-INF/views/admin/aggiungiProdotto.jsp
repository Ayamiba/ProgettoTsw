<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.utente.UtenteBean" %>
<% 
    UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); 
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuovo Prodotto - Sændwave Admin</title>
    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <!-- Aggiunto admin.css per la classe btn-back coerente con il resto -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    
    <style>
        .form-group label { font-weight: bold; margin-bottom: 5px; display: block; color: #333; }
        /* Aggiunto box-sizing: border-box per evitare che l'input sbordi dal contenitore su mobile */
        .form-input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 15px; font-family: inherit; box-sizing: border-box; }
        textarea.form-input { resize: vertical; min-height: 120px; }
    </style>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
        <!-- Aggiunto flex-wrap e gap per gestire il layout su schermi stretti (il bottone andrà a capo) -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px;">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 5px;">Inserisci Nuovo Prodotto</h1>
                <p class="subtitle" style="margin: 0;">Aggiungi un plugin o tool alla vetrina del catalogo.</p>
            </div>
            <!-- Cambiata la classe in btn-back -->
            <a href="ProfiloServlet" class="btn-back">&larr; Torna alla Dashboard</a>
        </div>

        <% if(request.getParameter("messaggio") != null) { %>
            <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                ✨ <strong>Successo!</strong> <%= request.getParameter("messaggio") %>
            </div>
        <% } %>

        <div class="dashboard-grid">
            <div class="dash-col" style="flex: 2;">
                <div class="dash-card card-admin">
                    <form action="AggiungiProdottoServlet" method="POST" enctype="multipart/form-data">
                        
                        <div class="form-group">
                            <label for="nome">Nome Prodotto</label>
                            <input type="text" id="nome" name="nome" class="form-input" placeholder="Es. Equalizzatore Valvolare" required>
                        </div>

                        <div class="form-group">
                            <label for="prezzo">Prezzo di Vendita (€)</label>
                            <input type="number" id="prezzo" name="prezzo" class="form-input" step="0.01" min="0" placeholder="Es. 49.99" required>
                        </div>

                        <div class="form-group">
                            <label for="descrizione">Descrizione Dettagliata</label>
                            <textarea id="descrizione" name="descrizione" class="form-input" placeholder="Inserisci le specifiche tecniche o le caratteristiche principali del plugin..."></textarea>
                        </div>

                        <div class="form-group">
                            <label for="foto">Copertina Prodotto (JPG/PNG)</label>
                            <input type="file" id="foto" name="foto" class="form-input" accept="image/*" required style="padding: 7px;">
                        </div>

                        <!-- AGGIUNTO: Traccia Audio Senza Effetto (Dry) -->
                        <div class="form-group">
                            <label for="demoDry">Traccia Audio Senza Effetto (Dry) - WAV</label>
                            <input type="file" id="demoDry" name="demoDry" class="form-input" accept=".wav, audio/wav" style="padding: 7px;">
                        </div>

                        <!-- AGGIUNTO: Traccia Audio Con Effetto (Wet) -->
                        <div class="form-group">
                            <label for="demoWet">Traccia Audio Con Effetto (Wet) - WAV</label>
                            <input type="file" id="demoWet" name="demoWet" class="form-input" accept=".wav, audio/wav" style="padding: 7px;">
                        </div>

                        <div style="text-align: right; margin-top: 20px;">
                            <button type="submit" class="dash-btn-save" style="font-size: 1.1em; padding: 12px 30px; width: 100%;">Pubblica nel Catalogo</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="dash-col" style="flex: 1;">
                <div class="dash-card card-admin" style="background: #f8f9fa;">
                    <h3 style="color: #4134E7;">Linee Guida Caricamento</h3>
                    <ul style="padding-left: 20px; color: #555; line-height: 1.6; margin-top: 15px;">
                        <li><strong>Copertina:</strong> PNG o JPG, risoluzione consigliata 800x800px (max 2 MB).</li>
                        <li><strong>Demo Audio (Dry/Wet):</strong> Carica file in formato <strong>.wav</strong> per consentire l'ascolto delle anteprime direttamente nella pagina del prodotto.</li>
                        <li>I campi delle demo audio possono essere lasciati vuoti se il plugin non prevede tracce dimostrative immediate.</li>
                    </ul>
                </div>
            </div>
        </div>

    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>