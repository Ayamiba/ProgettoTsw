package control;

import model.utente.UtenteBean;
import model.recensione.RecensioneBean;
import model.recensione.RecensioneDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/SalvaRecensioneServlet")
public class SalvaRecensioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
     // controllo della sessione perchè l'utente può scrivere una recensione solo se è registrato
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("user") : null;

        // Se l'utente NON è registrato/loggato, viene reindirizzato direttamente al Login
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        try {
            String tipo = request.getParameter("tipo"); // Arriverà "prodotto" o "ordine"
            int voto = Integer.parseInt(request.getParameter("voto"));
            String commento = request.getParameter("commento");
            
            RecensioneBean recensione = new RecensioneBean();
            recensione.setVoto(voto);
            recensione.setCommento(commento);
            recensione.setDataRecensione(new Date(System.currentTimeMillis())); // Data odierna
            recensione.setTipo(tipo);
            
            // Distinguiamo se la recensione è per un prodotto o per un ordine
            if ("prodotto".equalsIgnoreCase(tipo)) {
                String idProdottoStr = request.getParameter("idProdotto");
                if (idProdottoStr != null) {
                    recensione.setFkProdotto(Integer.parseInt(idProdottoStr));
                    recensione.setFkOrdine(null); // NULL per il DB
                }
            } else if ("ordine".equalsIgnoreCase(tipo)) {
                String idOrdineStr = request.getParameter("idOrdine");
                if (idOrdineStr != null) {
                    recensione.setFkOrdine(Integer.parseInt(idOrdineStr));
                    recensione.setFkProdotto(null); // NULL per il DB
                }
            }
            
            // Salvataggio nel Database
            RecensioneDAO dao = new RecensioneDAO();
            dao.doSave(recensione);
            
            // Reindirizzamento dell'utente
            if ("prodotto".equalsIgnoreCase(tipo)) {
                response.sendRedirect("ProdottoServlet?id=" + request.getParameter("idProdotto"));
            } else {
                response.sendRedirect("ProfiloServlet"); // Ritorna alla dashboard utente se era un ordine
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CatalogoServlet");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}