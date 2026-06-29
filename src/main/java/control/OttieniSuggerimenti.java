package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/OttieniSuggerimenti")
public class OttieniSuggerimenti extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String DB_URL = "jdbc:mysql://localhost:3306/saendwave";
    private final String DB_USER = "root";
    private final String DB_PASS = "Cicciogamer89!";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json"); // Settiamo il formato in JSON
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter(); // Utilizziamo out.print per inviare dati al JS richiedente
        
    String queryUtente = request.getParameter("q"); // Prende il valore inviato con "OttieniSuggerimenti?q="
    ArrayList<String> risultati = new ArrayList<>(); //Creiamo una lista per i risultati
    if (queryUtente != null && queryUtente.trim().length() >= 2) { //Inviamo la query
    	String sql = "SELECT prodotto.nome FROM prodotto WHERE prodotto.nome LIKE ? LIMIT 5"; 
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, queryUtente.trim() + "%"); 
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        risultati.add(rs.getString("nome_prodotto"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    StringBuilder json = new StringBuilder("["); //Costruzione della stringa JSON
    for (int i = 0; i < risultati.size(); i++) { // Per ogni risultato dal database
        String pulita = risultati.get(i).replace("\"", "\\\"");
        json.append("\"").append(pulita).append("\""); // Correggiamo eventuali errori del JSON sostituendo le virgolette con "virgolette protette"
        if (i < risultati.size() - 1) { //Inseriamo una virgola fino al penultimo elemento
            json.append(",");
        }
    }
    json.append("]"); // Parentesi quadrata finale

    out.print(json.toString()); // Inviamo il risultato 
    out.flush(); //svuota il buffer e forza l'invio dei dati
}
}
    