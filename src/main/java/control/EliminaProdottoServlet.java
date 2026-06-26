package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ConnectionPool;
import model.prodotto.ProdottoDAO;

@WebServlet("/EliminaProdottoServlet")
public class EliminaProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
		super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
		prodottoDAO=new ProdottoDAO(); 
	} 

    // l'eliminazione la faccio tramite post per sicurezza anche se abbiamo un solo parametro
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idProdotto");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int idProdotto = Integer.parseInt(idParam);
                //Uso  prodottoDAO per cancellare il record dal DB
                prodottoDAO.doDelete(idProdotto);
                
                // Reindirizziamo l'admin se funziona con un messaggio di successo
                response.sendRedirect("paginaAdmin.jsp?messaggio=Prodotto eliminato con successo!");
                return;
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("paginaAdmin.jsp?messaggio=Errore: ID non valido.");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("errore500.jsp");
                return;
            }
        }

        response.sendRedirect("paginaAdmin.jsp?messaggio=Errore: Nessun ID specificato.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}