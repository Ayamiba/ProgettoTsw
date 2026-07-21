package control;

import java.io.IOException;
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

@WebServlet("/GestioneOrdiniServlet")
public class GestioneOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        // 1. Sicurezza: solo gli Admin possono entrare qui
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // 2. Recupero TUTTI gli ordini
            OrdineDAO ordineDAO = new OrdineDAO();
            List<OrdineBean> tuttiOrdini = ordineDAO.doRetrieveAll();
            
            // 3. Invio i dati alla pagina JSP dedicata
            request.setAttribute("listaOrdini", tuttiOrdini);
            request.getRequestDispatcher("/WEB-INF/views/admin/gestioneOrdini.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloAdminServlet?errore=caricamento_ordini");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}