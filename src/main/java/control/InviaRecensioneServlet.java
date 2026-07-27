//Questa Servlet controlla che l'utente sia loggato, prende tramite la request i dati dell ordine e li invia alla jsp
package control;

import model.ConnectionPool;
import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.prodotto.ProdottoBean;
import model.prodotto.ProdottoDAO;
import model.recensione.RecensioneDAO;
import model.recensione.RecensioneBean;
import model.utente.UtenteBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@WebServlet("/InviaRecensioneServlet")
public class InviaRecensioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
		ordineDAO=new OrdineDAO();
	}
	
	//la doGet mostra la pagina
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("user") : null;

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
            OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);

            ProdottoDAO prodottoDAO = new ProdottoDAO();
            List<ProdottoBean> prodottiAcquistati = prodottoDAO.doRetrieveByOrdine(idOrdine); 
            
            request.setAttribute("ordine", ordine);
            request.setAttribute("prodotti", prodottiAcquistati);
            
            request.getRequestDispatcher("/WEB-INF/views/user/recensioni.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloServlet");
        }
    }
    
	//la doPost salva i dati quando viene cliccato sul pulsante di inviare la recensione
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        //verifichiamo che l'utente sia loggato
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("user") : null;

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // Lettura dei parametri inviati da recensioni.jsp
            String tipo = request.getParameter("tipo"); // Sarà "ordine" oppure "prodotto"
            int voto = Integer.parseInt(request.getParameter("voto"));
            String commento = request.getParameter("commento");
            String idOrdineStr = request.getParameter("idOrdine");
            String ritorno = request.getParameter("ritorno");
            
            // Creazione e popolamento di recensione
            RecensioneBean recensione = new RecensioneBean();
            
            recensione.setFkUtente(utente.getEmail()); 
            recensione.setVoto(voto);
            recensione.setCommento(commento);
            // Impostiamo la data attuale per la recensione
            recensione.setDataRecensione(new Date(System.currentTimeMillis())); 
            recensione.setTipo(tipo);
            
            // Gestione specifica in base al tipo di recensione (Ordine vs Prodotto)
            if ("ordine".equals(tipo)) {
                if (idOrdineStr != null && !idOrdineStr.isEmpty()) {
                    recensione.setFkOrdine(Integer.parseInt(idOrdineStr));
                }
            } 
            else if ("prodotto".equals(tipo)) {
                String idProdottoStr = request.getParameter("idProdotto");
                if (idProdottoStr != null && !idProdottoStr.isEmpty()) {
                    recensione.setFkProdotto(Integer.parseInt(idProdottoStr));
                }
            }
            
            //Salvataggio  nel Database tramite RecensioneDAO
            RecensioneDAO recensioneDAO = new RecensioneDAO();
            recensioneDAO.doSave(recensione);
            
            //Gestione del Reindirizzamento dopo il salvataggio
            if ("paginaRecensioni".equals(ritorno) && idOrdineStr != null) {
                // Ricarichiamo la pagina delle recensioni per permettere all'utente di recensire gli altri prodotti dell'ordine
                response.sendRedirect("InviaRecensioneServlet?idOrdine=" + idOrdineStr);
            } else {
                // Ritorno di default alla pagina del profilo se non è specificato altro
                response.sendRedirect("ProfiloServlet");
            }
            
        } catch (Exception e) {
            System.err.println("Errore in InviaRecensioneServlet (POST): " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ProfiloServlet");
        }
    }

}