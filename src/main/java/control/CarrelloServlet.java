package control;

import model.carrello.*;
import model.ConnectionPool;
import model.prodotto.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;

@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CarrelloDAO carrelloDAO;
    private static final int itemPerPage = 6;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
            e.printStackTrace();
        }
        carrelloDAO = new CarrelloDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user");
        
        // Usiamo una LinkedHashMap per mantenere l'ordine e associare l'ID riga al prodotto
        Map<Integer, ProdottoBean> prodottiCarrelloMap = new LinkedHashMap<>();
        String email = request.getParameter("email"); 
        
        try {
            if (utenteLoggato != null) {
                // CASO 1: Utente registrato loggato -> legge la mappa dal DB (ID riga -> Prodotto)
                prodottiCarrelloMap = carrelloDAO.doRetrieveByCarrello(utenteLoggato.getEmail()); 
            } else if (email != null && !email.trim().isEmpty()) {
                // TEST: Se inserisci la mail nel form temporaneo
                prodottiCarrelloMap = carrelloDAO.doRetrieveByCarrello(email);
            } else {
                // CASO 2: Utente OSPITE -> legge dai COOKIE e mappa con chiavi fittizie progressive
                Cookie[] cookies = request.getCookies();
                String contenutoCookie = null;
                
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (c.getName().equals("carrello_ospite")) {
                            contenutoCookie = c.getValue();
                            break;
                        }
                    }
                }
                
                if (contenutoCookie != null && !contenutoCookie.trim().isEmpty()) {
                    String[] idProdottiArray = contenutoCookie.split("-");
                    ProdottoDAO prodottoDAO = new ProdottoDAO();
                    int fakeIdRiga = 1;
                    
                    for (String idStr : idProdottiArray) {
                        if (!idStr.isEmpty()) {
                            int idProdotto = Integer.parseInt(idStr);
                            ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);
                            if (prodotto != null) {
                                // Mappa ogni occorrenza con un ID progressivo
                                prodottiCarrelloMap.put(fakeIdRiga++, prodotto);
                            }
                        }
                    }
                }
            }
            
            // 1. Passa la mappa completa a carrello.jsp (per la tabella con gli ID riga corretti)
            request.setAttribute("prodottiCarrello", prodottiCarrelloMap); 
            
            // 2. Passa la lista dei valori alla sessione per popolare correttamente la Navbar
            session.setAttribute("carrelloProdotti", new ArrayList<>(prodottiCarrelloMap.values()));
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/user/carrello.jsp");     
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento del carrello.");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}