package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.utente.UtenteBean;
import model.utente.UtenteDAO;
import control.Security; // Importa la tua classe di sicurezza

@WebServlet("/CambiaPasswordServlet")
public class CambiaPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        utenteDAO = new UtenteDAO();
    }

    // Mostra il form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/user/cambiaPassword.jsp").forward(request, response);
    }

    // Processa il cambio password
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utenteLoggato = (UtenteBean) session.getAttribute("user");

        if (utenteLoggato == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String vecchiaPassword = request.getParameter("vecchiaPassword");
        String nuovaPassword = request.getParameter("nuovaPassword");
        String confermaPassword = request.getParameter("confermaPassword");

        // 1. Controllo: La nuova password e la conferma coincidono?
        if (!nuovaPassword.equals(confermaPassword)) {
            request.setAttribute("errore", "Le nuove password non coincidono.");
            request.getRequestDispatcher("/WEB-INF/views/user/cambiaPassword.jsp").forward(request, response);
            return;
        }

        // 2. Controllo: La vecchia password inserita è corretta?
        String hashVecchiaInserita = Security.hashPassword(vecchiaPassword);
        if (!hashVecchiaInserita.equals(utenteLoggato.getPassword())) {
            request.setAttribute("errore", "La vecchia password non è corretta.");
            request.getRequestDispatcher("/WEB-INF/views/user/cambiaPassword.jsp").forward(request, response);
            return;
        }

        // Se tutti i controlli passano, salviamo la nuova password
        try {
            utenteLoggato.setPassword(Security.hashPassword(nuovaPassword));
            utenteDAO.doUpdate(utenteLoggato);
            
            // Aggiorniamo la sessione e rimandiamo al profilo con un parametro di successo
            session.setAttribute("user", utenteLoggato);
            response.sendRedirect("ProfiloServlet?success=password");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Si è verificato un errore durante l'aggiornamento. Riprova.");
            request.getRequestDispatcher("/WEB-INF/views/user/cambiaPassword.jsp").forward(request, response);
        }
    }
}