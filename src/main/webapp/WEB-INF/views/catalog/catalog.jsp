<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SÆNDWAVE | Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>

    <jsp:include page="../components/header.jsp" />

    <main>
        <section class="hero-banner">
            <h1>Benvenuto su SÆNDWAVE</h1>
            <p>La tua traccia, il tocco dei professionisti.</p>
            
            <button>
                Esplora il Catalogo
            </button>
        </section>

        <section class="plugin-in-evidenza">
            <h2>Plugin in Evidenza</h2>
            <div class="griglia-prodotti">
               </div>
        </section>
    </main>

    <jsp:include page="../components/footer.jsp" />

</body>
</html>