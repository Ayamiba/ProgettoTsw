package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/OrdineCompletatoServlet")
public class OrdineCompletatoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Sicurezza: Devi essere loggato
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // 2. Sicurezza: Deve esserci un ID ordine nell'URL
        String idOrdine = request.getParameter("id");
        if (idOrdine == null || idOrdine.trim().isEmpty()) {
            response.sendRedirect("HomeServlet");
            return;
        }

        // 3. Passiamo l'ID alla pagina e apriamo la vista nascosta in WEB-INF
        request.setAttribute("idOrdine", idOrdine);
        request.getRequestDispatcher("/WEB-INF/views/user/ordineCompletato.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}