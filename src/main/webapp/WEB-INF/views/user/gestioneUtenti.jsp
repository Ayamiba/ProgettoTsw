<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.utente.UtenteBean" %>
<%
    // Recupero la lista passata dalla Servlet
    List<UtenteBean> utenti = (List<UtenteBean>) request.getAttribute("listaUtenti");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Sændwave - Gestione Utenti</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/dashboard.css">
  	<link rel="stylesheet" href="css/admin.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/navbar.jsp" />

    <main class="dashboard-container">
        
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;">
            <div>
                <h1 style="color: #d9534f; margin-bottom: 0;">Database Utenti</h1>
                <p class="subtitle" style="margin-bottom: 0;">Gestisci i permessi e gli accessi della piattaforma.</p>
            </div>
            
            <a href="ProfiloServlet" class="btn-back">&larr; Torna alla Dashboard</a>
        </div>
        
        <div class="dash-card card-admin" style="max-width: 100%;">
            <table class="dash-table">
                <thead>
                    <tr>
                        <th>Nome Completo</th>
                        <th>Email (ID)</th>
                        <th>Data Nascita</th>
                        <th>Livello Attuale</th>
                        <th style="text-align: right;">Azioni Amministrative</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(utenti != null && !utenti.isEmpty()) { 
                        for(UtenteBean u : utenti) { 
                    %>
                    <tr>
                        <td><strong><%= u.getNome() %> <%= u.getCognome() %></strong></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= (u.getDataNascita() != null) ? u.getDataNascita().toString() : "N/D" %></td>
                        
                        <td>
                            <span class="status-badge <%= u.getTipo().equals("admin") ? "working" : (u.getTipo().equals("professionista") ? "pending" : "ready") %>" 
                                  style="background-color: <%= u.getTipo().equals("professionista") ? "#fef3c7" : "" %>; color: <%= u.getTipo().equals("professionista") ? "#b45309" : "" %>;">
                                <%= u.getTipo() %>
                            </span>
                        </td>
                        
                        <td style="text-align: right; gap: 8px; display: flex; justify-content: flex-end;">
                            <% if(!u.getTipo().equals("admin")) { %>
                                <% if(u.getTipo().equals("utente registrato")) { %>
                                    <a href="GestioneUtentiServlet?azione=promuovi&email=<%= u.getEmail() %>" class="btn-action btn-promote" onclick="return confirm('Confermi di voler promuovere l\'utente a Professionista? Avrà accesso alla dashboard di mix/mastering.');">Promuovi a Pro</a>
                                <% } %>
									<a href="GestioneUtentiServlet?azione=elimina&email=<%= u.getEmail() %>" class="btn-action btn-ban" onclick="return confirm('Sei sicuro di voler eliminare questo utente? L\'azione è irreversibile.');">Elimina</a>                            <% } else { %>
                                <span style="color: #aaa; font-style: italic; font-size: 0.85em; padding: 6px;">Nessuna azione</span>
                            <% } %>
                        </td>
                    </tr>
                    <%  } 
                       } else { 
                    %>
                    <tr>
                        <td colspan="5" style="text-align: center; padding: 30px; color: #888;">
                            Nessun utente trovato nel database.
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </main>

    <jsp:include page="/WEB-INF/views/components/footer.jsp" />

</body>
</html>