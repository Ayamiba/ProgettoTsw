<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Chi Siamo - Sændwave</title>
    <!-- CSS Generale del sito -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <!-- CSS specifico per la pagina About -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/aboutUs.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="about-container">
    	<!--  sfondo preso da style.css -->
        <section class="about-hero">
            <h1>Chi Siamo</h1>
            <p>La tua musica senza ostacoli tecnici né costi proibitivi.</p>
        </section>

        <section class="about-section">
            <h2 class="section-title">La Nostra Missione</h2>
            <p class="mission-text">
                In <strong>Sændwave</strong> crediamo che ogni musicista debba avere la possibilità di far risuonare le proprie idee. Spesso la produzione audio richiede software costosi, plugin complessi e anni di formazione tecnica. Sændwave nasce per abbattere queste barriere: offriamo un canale diretto tra te e professionisti del suono specializzati, permettendoti di ottenere tracce lavorate con i migliori VST senza dover acquistare le licenze o imparare a usarli.
            </p>
        </section>

        <section class="about-section">
            <h2 class="section-title">Come Funziona</h2>
            <div class="steps-grid">
                <div class="step-card">
                    <div class="step-number">1</div>
                    <h3>Scegli gli Effetti</h3>
                    <p>Seleziona i VST e le lavorazioni desiderate dal nostro catalogo.</p>
                </div>
                <div class="step-card">
                    <div class="step-number">2</div>
                    <h3>Carica il Brano</h3>
                    <p>Invia la tua traccia audio direttamente sulla nostra piattaforma.</p>
                </div>
                <div class="step-card">
                    <div class="step-number">3</div>
                    <h3>Lavorazione</h3>
                    <p>I nostri professionisti applicano gli effetti con cura e precisione.</p>
                </div>
                <div class="step-card">
                    <div class="step-number">4</div>
                    <h3>Ricevi il File</h3>
                    <p>Scarica la tua traccia ottimizzata e pronta per l'ascolto.</p>
                </div>
            </div>
        </section>

        <section class="about-section">
            <h2 class="section-title">Perché Scegliere Sændwave</h2>
            <div class="values-grid">
                <div class="value-card">
                    <h3>Costi Abbattuti</h3>
                    <p>Paga solo per la lavorazione di cui hai bisogno, senza acquistare software costosi.</p>
                </div>
                <div class="value-card">
                    <h3>Zero Competenze Tecniche</h3>
                    <p>Concentrati solo sul comporre e suonare: alla parte tecnica pensiamo noi.</p>
                </div>
                <div class="value-card">
                    <h3>Professionisti Dedicati</h3>
                    <p>Ogni lavorazione è eseguita da tecnici reali per garantire la massima resa sonora.</p>
                </div>
            </div>
        </section>
        <!-- Collegamento a NostriProfessionistiServlet -->
        <section class="cta-professionisti-box">
            <h2>Vuoi conoscere chi curerà il tuo suono?</h2>
            <p>Scopri la nostra squadra di sound engineer ed esplora le recensioni lasciate dagli altri musicisti.</p>
            <a href="NostriProfessionistiServlet" class="btn-professionisti">Scopri i Professionisti e le Recensioni</a>
        </section>
    </main>
    <%@ include file="/WEB-INF/views/components/footer.jsp" %>
</body>
</html>