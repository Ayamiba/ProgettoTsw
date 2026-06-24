//mettiamo nella doGet la fine della sessione pk il tasto di logout <a href.. usa la get>
package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")

public class LogoutServlet extends HttpServlet{
	//il metodo init non serve
	private static final long serialVersionUID=1L;
	
	//get e post sono uguali
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Recuperiamo la sessione attuale. 
        HttpSession session = request.getSession(false);
        if (session != null) {
            // cancelliamo l'utente loggato invalidando la sessione
            session.invalidate(); 
        }     
        // Una volta fatto il logout, rimandiamo l'utente al catalogo
        response.sendRedirect("CatalogoServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}