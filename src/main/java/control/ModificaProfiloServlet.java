package control;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.utente.UtenteBean;
import model.utente.UtenteDAO;

@WebServlet("/ModificaProfiloServlet")
public class ModificaProfiloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        utenteDAO = new UtenteDAO();
    }

    // Mostra la pagina del form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/user/modificaProfilo.jsp").forward(request, response);
    }

    // Riceve i dati modificati e aggiorna DB e Sessione
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");

        if (utenteLoggato == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Recupero i nuovi dati dal form
        String nuovoNome = request.getParameter("nome");
        String nuovoCognome = request.getParameter("cognome");
        String dataNascitaStr = request.getParameter("data_nascita");

        // Aggiorno l'oggetto in memoria (Mantenendo intatti Email, Password e Tipo)
        utenteLoggato.setNome(nuovoNome);
        utenteLoggato.setCognome(nuovoCognome);
        
        if (dataNascitaStr != null && !dataNascitaStr.isEmpty()) {
            utenteLoggato.setDataNascita(Date.valueOf(dataNascitaStr));
        }

        try {
            // Salvo le modifiche nel Database
            utenteDAO.doUpdate(utenteLoggato);
            
            // Sovrascrivo la sessione con l'oggetto aggiornato (fondamentale per la Navbar!)
            session.setAttribute("user", utenteLoggato);
            
            // Ritorno al profilo
            response.sendRedirect("ProfiloServlet");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore durante l'aggiornamento dei dati. Riprova.");
            request.getRequestDispatcher("/WEB-INF/views/user/modificaProfilo.jsp").forward(request, response);
        }
    }
}