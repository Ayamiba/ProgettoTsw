<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "model.utente.UtenteBean" %>
<% UtenteBean utenteloggato = (UtenteBean) session.getAttribute("user"); %>

<!DOCTYPE html>
<html lang="it">

	<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sændwave – Pannello Admin</title>
    
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/admin.css">
</head>

<body>
	<%@ include file="/WEB-INF/views/components/navbar.jsp" %> <!-- fa riferimento al file navbar per non riscriverla -->

	<section class="contenuti-admin">
		<div class="admin-header">
                <h1>Pannello Gestione Admin– Inserisci Nuovo Prodotto</h1>
            </div>

            <%-- Notifica per l'inserimento riuscito --%>
            <% if(request.getParameter("messaggio") != null) { %>
                <div class="alert-success">
                    ✨ <%= request.getParameter("messaggio") %>
                </div>
            <% } %>
            
            <section class="corpo-admin">
            <div class="suggerimenti-immagine">
                    <h3 style="font-size: 1.1em; margin-bottom: 8px; color: #6A32E8; font-weight: 500;">Linee Guida Immagine</h3>
                    <p>• Usa formati standard come <strong>PNG</strong> o <strong>JPG</strong>.</p>
                    <p>• Risoluzione quadrata raccomandata: <strong>800x800 pixel</strong>.</p>
                    <p>• Uno sfondo trasparente o bianco garantisce la resa ottimale all'interno del catalogo.</p>
                </div>
                
                <!-- form per inserire tutto basato sulla Servlet -->
                <form action="AggiungiProdottoServlet" method="POST" enctype="multipart/form-data">
                    
                    <div class="form-group">
                        <label for="nome">Nome Prodotto</label>
                        <input type="text" id="nome" name="nome" class="form-input" placeholder="Es. Equalizzatore" required>
                    </div>

                    <div class="form-group">
                        <label for="prezzo">Prezzo di Vendita (€)</label>
                        <input type="number" id="prezzo" name="prezzo" class="form-input" step="0.01" min="0" placeholder="Es. 49.99" required>
                    </div>

                    <div class="form-group">
                        <label for="descrizione">Descrizione</label>
                        <textarea id="descrizione" name="descrizione" class="form-input" placeholder="Inserisci le specifiche tecniche o le caratteristiche principali del plugin..."></textarea>
                    </div>

                    <div class="form-group">
                        <label for="foto">Copertina Prodotto </label>
                        <div class="file-upload-wrapper">
                            <input type="file" id="foto" name="foto" class="form-input" accept="image/*" required>
                        </div>
                    </div>

                    <button type="submit" class="btn-admin-submit">Pubblica nel Catalogo</button>
                    </form>
                    </section>
	</section>
	<%@ include file="/WEB-INF/views/components/footer.jsp" %>
</body>

</html>