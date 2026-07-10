package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.utente.UtenteBean;

@WebServlet("/ProfiloServlet")
public class ProfiloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        // Se l'utente non è loggato, lo cacciamo alla pagina di login
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // Smistamento MVC in base al ruolo
        String tipo = utente.getTipo().toLowerCase();

        if (tipo.equals("admin")) {
            request.getRequestDispatcher("/WEB-INF/views/admin/profiloAdmin.jsp").forward(request, response);
            
        } else if (tipo.equals("professionista")) {
            request.getRequestDispatcher("/WEB-INF/views/pro/profiloProfessionista.jsp").forward(request, response);
            
        } else {
            // Caso di default: "utente registrato"
            request.getRequestDispatcher("/WEB-INF/views/user/profiloUtente.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}