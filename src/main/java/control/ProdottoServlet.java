package control;

import model.prodotto.ProdottoBean;
import model.prodotto.ProdottoDAO;
import model.recensione.RecensioneBean;
import model.recensione.RecensioneDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProdottoServlet")
public class ProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        String nomeString = request.getParameter("nome"); // Catturiamo il parametro da Javascript!
        
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        ProdottoBean prodotto = null;

        try {
            // CASO 1: Ricerca classica per ID (es. cliccando dal catalogo)
            if (idString != null && !idString.trim().isEmpty()) {
                int id = Integer.parseInt(idString);
                prodotto = prodottoDAO.doRetrieveByKey(id);
            } 
            // CASO 2: Ricerca per Nome (inviato dalla barra di ricerca JS)
            else if (nomeString != null && !nomeString.trim().isEmpty()) {
                prodotto = prodottoDAO.doRetrieveByName(nomeString);
            }

            // Se abbiamo trovato il prodotto (in un modo o nell'altro), andiamo alla pagina di dettaglio
            if (prodotto != null) {
                request.setAttribute("prodottoSingolo", prodotto);
                
                // GESTIONE RECENSIONI ---
                RecensioneDAO recensioneDAO = new RecensioneDAO();
                
                // Usiamo getIdProdotto() sull'oggetto appena trovato. 
                // Così funziona perfettamente sia che l'utente abbia cercato per ID o per Nome!
                List<RecensioneBean> recensioniProdotto = recensioneDAO.doRetrieveByIdProdotto(prodotto.getIdProdotto());
                
                // Passiamo la lista alla JSP
                request.setAttribute("recensioni", recensioniProdotto);
            } else {
                // Se non è stato trovato, impostiamo l'attributo a null per far mostrare alla JSP "Nessun prodotto trovato"
                request.setAttribute("prodottoSingolo", null);
            }
                request.getRequestDispatcher("/WEB-INF/views/catalog/prodotto.jsp").forward(request, response);
                return;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Se l'ID o il Nome non ci sono, o il prodotto non esiste nel DB, rimandiamo al catalogo
        response.sendRedirect("CatalogoServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}