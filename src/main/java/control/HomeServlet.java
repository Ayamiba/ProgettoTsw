package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ConnectionPool;
import model.prodotto.ProdottoBean;
import model.prodotto.ProdottoDAO;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        
        try {
            // Recuperiamo tutti i prodotti dal database
            List<ProdottoBean> tuttiProdotti = prodottoDAO.doRetrieveAll();
            
            // Per la home, prendiamo solo gli ultimi 4 inseriti per non intasarla
            List<ProdottoBean> ultimiProdotti = tuttiProdotti;
            if (tuttiProdotti != null && tuttiProdotti.size() > 4) {
                ultimiProdotti = tuttiProdotti.subList(tuttiProdotti.size() - 4, tuttiProdotti.size());
            }
            
            // Salviamo la lista nella request per farla leggere alla JSP (allo scriptlet)
            request.setAttribute("ultimiProdotti", ultimiProdotti);
            
            // Reindirizziamo alla home.jsp (che ora si trova direttamente in webapp)
            request.getRequestDispatcher("/home.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}