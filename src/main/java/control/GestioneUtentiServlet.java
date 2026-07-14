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

import model.ConnectionPool;
import model.carrello.CarrelloDAO;
import model.utente.UtenteBean;
import model.utente.UtenteDAO;

@WebServlet("/GestioneUtentiServlet")
public class GestioneUtentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtenteDAO utenteDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore Connection Pool in RegistrazioneServlet!");
            e.printStackTrace();
        }
        utenteDAO = new UtenteDAO();
        carrelloDAO = new CarrelloDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean admin = (UtenteBean) session.getAttribute("user");

        // Controllo di sicurezza vitale: solo gli ADMIN possono accedere a questa Servlet!
        if (admin == null || !admin.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        UtenteDAO utenteDAO = new UtenteDAO();
        try {
            // Assicurati di avere un metodo doRetrieveAll() nel tuo UtenteDAO!
            List<UtenteBean> listaUtenti = utenteDAO.doRetrieveAll();
            request.setAttribute("listaUtenti", listaUtenti);
            
            request.getRequestDispatcher("/WEB-INF/views/user/gestioneUtenti.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di errore, si torna al profilo
            response.sendRedirect("ProfiloServlet"); 
        }
    }
}