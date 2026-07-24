package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.utente.UtenteBean;

@WebServlet("/ProfiloProfessionistaServlet")
public class ProfiloProfessionistaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        // Controllo Sicurezza: Solo i Professionisti possono accedere
        if (utente == null || !utente.getTipo().equalsIgnoreCase("professionista")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            OrdineDAO ordineDAO = new OrdineDAO();
            
            // 1. Ordini liberi da prendere ("In attesa")
            List<OrdineBean> ordiniInAttesa = ordineDAO.doRetrieveOrdiniInAttesa();
            
            System.out.println("✅ DEBUG - Ordini in attesa trovati: " + ordiniInAttesa.size());
            
            // 2. Ordini attualmente in lavorazione da QUESTO professionista (USIAMO getEmail!)
            List<OrdineBean> ordiniInLavorazione = ordineDAO.doRetrieveByProfessionistaAndStato(utente.getEmail(), "In Lavorazione");
            
            // 3. Storico lavori completati da QUESTO professionista
            List<OrdineBean> ordiniCompletati = ordineDAO.doRetrieveByProfessionistaAndStato(utente.getEmail(), "Completato");

            // Passiamo tutto alla JSP
            request.setAttribute("ordiniInAttesa", ordiniInAttesa);
            request.setAttribute("ordiniInLavorazione", ordiniInLavorazione);
            request.setAttribute("ordiniCompletati", ordiniCompletati);

            request.getRequestDispatcher("/WEB-INF/views/user/profiloProfessionista.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}