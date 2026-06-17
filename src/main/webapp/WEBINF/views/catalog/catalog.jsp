<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SÆNDWAVE | Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="../components/header.jsp" />

    <main>
        <section class="hero-banner" style="background-color: var(--sfondo-alternativo); padding: 50px; text-align: center; border-radius: 8px;">
            <h1 style="color: white;">Benvenuto su SÆNDWAVE</h1>
            <p style="color: white; margin-top: 10px;">La tua traccia, il tocco dei professionisti.</p>
            
            <button style="background-color: var(--colore-primario); color: white; border: none; padding: 12px 24px; margin-top: 20px; cursor: pointer; border-radius: 4px;">
                Esplora il Catalogo
            </button>
        </section>

        <section class="plugin-in-evidenza" style="margin-top: 40px;">
            <h2>Plugin in Evidenza</h2>
            <div class="griglia-prodotti">
               </div>
        </section>
    </main>

    <jsp:include page="../components/footer.jsp" />

</body>
</html>