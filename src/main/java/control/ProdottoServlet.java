package control;

import model.prodotto.ProdottoBean;
import model.prodotto.ProdottoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ProdottoServlet")
public class ProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Recuperiamo l'ID dall'URL
        String idString = request.getParameter("id");

        if (idString != null && !idString.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idString);
                ProdottoDAO prodottoDAO = new ProdottoDAO();
                
                // 2. Cerchiamo il prodotto nel database
                ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(id);

                if (prodotto != null) {
                    // 3. Se esiste, lo mettiamo nella request e andiamo alla pagina di dettaglio
                    request.setAttribute("prodottoSingolo", prodotto);
                    
                    // (Opzionale: qui in futuro potresti recuperare anche le RecensioniDAO e TracceAudioDAO)
                    
                    request.getRequestDispatcher("/WEB-INF/views/catalog/prodotto.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 4. Se l'ID non c'è, o non è un numero, o il prodotto non esiste, rimandiamo al catalogo o a un 404
        response.sendRedirect("CatalogoServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}