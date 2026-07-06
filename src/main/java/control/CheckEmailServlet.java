package control;
import model.ConnectionPool;
import model.prodotto.ProdottoDAO;
import model.utente.UtenteBean;
import model.utente.UtenteDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/CheckEmailServlet") /* Servlet per verifica E-mail */
public class CheckEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

    @Override
    public void init() throws ServletException {
  		super.init();
  		try {
  	        ConnectionPool.init(5);
  	    } catch (SQLException e) {
  	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
  	        e.printStackTrace();
  	    }
  	}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (email == null || email.trim().isEmpty()) { /* L'email è vuota */

                out.print("{\"error\": \"Parametro email mancante o vuoto\"}");
                return;
            }

            
            UtenteDAO dao = new UtenteDAO();
            UtenteBean user= dao.doRetrieveByKey(email);
            boolean emailDisponibile = (user == null);

            out.print("{\"available\": " + emailDisponibile + ", \"error\": null}"); /* Costruzione del JSON */
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Errore interno del server\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}