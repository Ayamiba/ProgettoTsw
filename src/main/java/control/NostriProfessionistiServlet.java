package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.recensione.RecensioneBean;
import model.recensione.RecensioneDAO;
import model.utente.UtenteBean;
import model.utente.UtenteDAO;

@WebServlet("/NostriProfessionistiServlet")
public class NostriProfessionistiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtenteDAO utenteDAO;
    private RecensioneDAO recensioneDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Inizializzazione dei DAO all'avvio della servlet
        utenteDAO = new UtenteDAO();
        recensioneDAO = new RecensioneDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtenteDAO utenteDAO = new UtenteDAO();
        RecensioneDAO recensioneDAO = new RecensioneDAO();
        
        try {
            // 1. Prendo tutti i professionisti
            List<UtenteBean> professionisti = utenteDAO.doRetrieveProfessionisti();
            
            // 2. Associo ogni professionista alla lista delle sue recensioni
            Map<UtenteBean, List<RecensioneBean>> mappaProfessionisti = new LinkedHashMap<>();
            
            for (UtenteBean prof : professionisti) {
                List<RecensioneBean> recensioniDelProf = recensioneDAO.doRetrieveRecensioniByProfessionista(prof.getEmail());
                mappaProfessionisti.put(prof, recensioniDelProf);
            }
            
            // 3. Passo la mappa alla pagina JSP
            request.setAttribute("mappaProfessionisti", mappaProfessionisti);
            request.getRequestDispatcher("/WEB-INF/catalog/professionisti.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Reindirizzamento di sicurezza in caso di errore col database
            response.sendRedirect("HomeServlet?messaggio=Errore di sistema durante il caricamento dei professionisti.");
        }
    }
}