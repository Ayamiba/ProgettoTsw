<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrazione - E-commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 50px; }
        .reg-container { max-width: 400px; margin: auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; }
        .btn { width: 100%; padding: 10px; background-color: #28a745; color: white; border: none; border-radius: 3px; cursor: pointer; }
        .btn:hover { background-color: #218838; }
        .error-message { color: red; font-weight: bold; margin-bottom: 15px; text-align: center; }
    </style>
</head>
<body>

    <div class="reg-container">
        <h2>Crea un nuovo account</h2>
        
        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
            <div class="error-message"><%= errore %></div>
        <%  } %>

        <form action="RegistrazioneServlet" method="POST">
            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required>
            </div>

            <div class="form-group">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome" required>
            </div>

            <div class="form-group">
                <label for="data_nascita">Data di Nascita:</label>
                <input type="date" id="data_nascita" name="data_nascita" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit" class="btn">Registrati</button>
        </form>
        
        <p style="text-align:center; margin-top:15px;">
            Hai già un account? <a href="LoginServlet">Accedi qui</a>
        </p>
    </div>

</body>
</html>