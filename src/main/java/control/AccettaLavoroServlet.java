package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ordine.OrdineDAO;
import model.utente.UtenteBean;

@WebServlet("/AccettaLavoroServlet")
public class AccettaLavoroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Controllo Sicurezza: Solo i Professionisti possono accettare un lavoro
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        if (utente == null || !utente.getTipo().equalsIgnoreCase("professionista")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // 2. Recupero ID Ordine dal form
        String idOrdineStr = request.getParameter("idOrdine");

        if (idOrdineStr != null && !idOrdineStr.trim().isEmpty()) {
            try {
                int idOrdine = Integer.parseInt(idOrdineStr);
                OrdineDAO ordineDAO = new OrdineDAO();
                
                // 3. Eseguiamo l'aggiornamento nel DB
                boolean successo = ordineDAO.accettaOrdine(idOrdine, utente.getEmail());
                
                if (successo) {
                    // L'ordine è ora assegnato a lui
                    response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Ordine #" + idOrdine + " preso in carico con successo! Ora è in lavorazione.");
                } else {
                    // Se ritorna false, significa che qualcun altro lo ha preso un millisecondo prima o c'è un errore
                    response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore: Impossibile prendere in carico l'ordine. Potrebbe essere già stato assegnato.");
                }
                return;

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore: ID Ordine non valido.");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore di comunicazione col database.");
                return;
            }
        }
        
        response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore: Nessun ID specificato.");
    }

    // Blocchiamo le richieste GET dirette per impedire attivazioni accidentali tramite URL
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("ProfiloProfessionistaServlet");
    }
}