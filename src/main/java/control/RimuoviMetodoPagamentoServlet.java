package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.metodoPagamento.MetodoPagamentoDAO;
import model.utente.UtenteBean;

@WebServlet("/RimuoviMetodoPagamentoServlet")
public class RimuoviMetodoPagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Controllo sicurezza sessione utente
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        // 2. Recupero del parametro della carta e conversione in long
        String numeroCartaStr = request.getParameter("numeroCarta");

        if (numeroCartaStr != null && !numeroCartaStr.trim().isEmpty()) {
            try {
                long numeroCarta = Long.parseLong(numeroCartaStr);
                
                MetodoPagamentoDAO pagDAO = new MetodoPagamentoDAO();
                
                // Eliminazione dal database passando il tipo long
                pagDAO.doDelete(numeroCarta);
                
            } catch (NumberFormatException e) {
                System.err.println("Errore: numero carta non valido -> " + numeroCartaStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 3. Ricarica la pagina del profilo
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("ProfiloServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}