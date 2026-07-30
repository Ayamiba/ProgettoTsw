package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.recensione.RecensioneBean;
import model.recensione.RecensioneDAO;
import model.utente.UtenteBean;

@WebServlet("/EliminaRecensioneServlet")
public class EliminaRecensioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Controllo autenticazione utente
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // 2. Recupero dell'ID della recensione
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idRecensione = Integer.parseInt(idStr);
                
                RecensioneDAO recensioneDAO = new RecensioneDAO();
                RecensioneBean recensione = recensioneDAO.doRetrieveByKey(idRecensione);
                
                // 3. Verifica di sicurezza: la recensione esiste e appartiene all'utente loggato
                if (recensione != null && utente.getEmail() != null && utente.getEmail().equalsIgnoreCase(recensione.getFkUtente())) {
                    recensioneDAO.doDelete(idRecensione);
                }
                
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        }

        // 4. Ricarica la pagina di provenienza (Profilo)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("ProfiloServlet"); // Fallback se referer è vuoto
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}