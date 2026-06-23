<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - E-commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 50px; }
        .login-container { max-width: 300px; margin: auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; }
        .btn { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 3px; cursor: pointer; }
        .btn:hover { background-color: #0056b3; }
        .error-message { color: red; font-weight: bold; margin-bottom: 15px; text-align: center; }
    </style>
</head>
<body>

    <div class="login-container">
        <h2>Accedi</h2>
        
        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
            <div class="error-message"><%= errore %></div>
        <%  } %>

        <form action="LoginServlet" method="POST">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit" class="btn">Login</button>
        </form>
    </div>

</body>
</html>