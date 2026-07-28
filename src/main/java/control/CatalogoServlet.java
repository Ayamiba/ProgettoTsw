package control;

import model.utente.*;
import model.ConnectionPool;

import model.prodotto.*;
import model.categoria.*;
import model.tipologia.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;

@WebServlet ("/CatalogoServlet")
public class CatalogoServlet extends HttpServlet {
    private static long serialVersionUID= 1L;
    
    private ProdottoDAO prodottoDAO;
    private static final int itemPerPage=6; //Elementi caricati in una pagina (caricati != mostrati)
    
    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
            e.printStackTrace();
        }
        prodottoDAO=new ProdottoDAO(); 
    } 
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        List<ProdottoBean> listaProdotti = null;
        
        // il parametro "search" serve per la barra di ricerca
        String testoRicerca = request.getParameter("search"); 
        String categoriaScelta = request.getParameter("categoria");
        String sottoCategoriaScelta = request.getParameter("sottoCategoria");
        String prezzoMaxStr = request.getParameter("prezzoMax");
        
        try {
            float prezzoMax = -1; // -1 indica che non c'è nessun filtro prezzo attivo
            
            if (prezzoMaxStr != null && !prezzoMaxStr.trim().isEmpty()) {
                try {
                    prezzoMax = Float.parseFloat(prezzoMaxStr);
                } catch (NumberFormatException e) {
                    prezzoMax = -1; 
                }
            }
            
            // Se l'utente ha usato la barra di ricerca, cerchiamo per nome.
            // Altrimenti applichiamo i soliti filtri della barra laterale.
            if (testoRicerca != null && !testoRicerca.trim().isEmpty()) {
                listaProdotti = prodottoDAO.doRetrieveByNomeLike(testoRicerca);
            } else {
                listaProdotti = prodottoDAO.doRetrieveByAllFilters(categoriaScelta, sottoCategoriaScelta, prezzoMax);
            }
            
            // 3. Impostiamo gli attributi per la JSP
            request.setAttribute("prodotti", listaProdotti);
            request.setAttribute("ricercaAttiva", testoRicerca != null ? testoRicerca : ""); // Utile se vuoi mostrare "Risultati per: ..."
            request.setAttribute("categoriaAttiva", categoriaScelta);
            request.setAttribute("sottoCategoriaAttiva", sottoCategoriaScelta);
            request.setAttribute("prezzoAttivo", prezzoMax >= 0 ? String.valueOf(prezzoMax) : "");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/catalog/catalog.jsp");     
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il caricamento del catalogo.");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}