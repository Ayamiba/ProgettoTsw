package control;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.prodotto.ProdottoBean;
import model.prodotto.ProdottoDAO;
import model.utente.UtenteBean;

@WebServlet("/GestioneCatalogoAdminServlet")
public class GestioneCatalogoAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        // 1. Sicurezza: solo gli Admin possono vedere questa lista
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // 2. Recuperiamo tutto il catalogo
            ProdottoDAO prodottoDAO = new ProdottoDAO();
            List<ProdottoBean> catalogo = prodottoDAO.doRetrieveAll();
            
            // 3. Passiamo la lista alla pagina e la apriamo
            request.setAttribute("catalogo", catalogo);
            request.getRequestDispatcher("/WEB-INF/views/admin/listaProdottiAdmin.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloAdminServlet?errore=caricamento_catalogo");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}