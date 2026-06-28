<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Area Professionista - Gestione Tracce</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f4f9; color: #333; }
        h2 { color: #0056b3; }
        
        /* Stile per i messaggi di notifica di successo/errore */
        .alert { padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold; max-width: 500px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        
        /* Box Form Input Manuale */
        .form-manuale-box { background: white; padding: 20px; border-radius: 6px; max-width: 500px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .form-manuale { display: flex; flex-direction: column; gap: 15px; }
        .input-group { display: flex; flex-direction: column; gap: 5px; }
        .input-id { padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 16px; width: 100%; box-sizing: border-box; }
        .btn-container { display: flex; gap: 10px; }

        /* Bottoni d'azione */
        .btn-azione { border: none; padding: 10px 15px; cursor: pointer; border-radius: 4px; font-weight: bold; font-size: 14px; flex: 1; transition: background 0.2s; color: white; }
        .btn-approva { background-color: #28a745; }
        .btn-approva:hover { background-color: #218838; }
        .btn-disapprova { background-color: #dc3545; }
        .btn-disapprova:hover { background-color: #c82333; }
    </style>
</head>
<body>

    <h2>Pannello di Controllo Professionista</h2>
    
    <%-- Mostra il messaggio se la Servlet reindirizza indietro con esito positivo --%>
    <% 
        String messaggio = request.getParameter("messaggio");
        if (messaggio != null && !messaggio.isEmpty()) {
    %>
        <div class="alert">
            <%= messaggio %>
        </div>
    <% 
        } 
    %>

    <div class="form-manuale-box">
        <form action="CheckServlet" method="POST" class="form-manuale">
            
            <div class="input-group">
                <label for="idTracciaInput" style="font-weight: bold; font-size: 15px;">ID Traccia Audio:</label>
                <input type="number" id="idTracciaInput" name="idTraccia" placeholder="Inserisci l'ID della traccia (es. 12)" required class="input-id">
            </div>

            <div class="btn-container">
                <button type="submit" name="nuovoCheck" value="true" class="btn-azione btn-approva">
                    Approva
                </button>
                
                <button type="submit" name="nuovoCheck" value="false" class="btn-azione btn-disapprova">
                    Disapprova
                </button>
            </div>
            
        </form>
    </div>

</body>
</html>