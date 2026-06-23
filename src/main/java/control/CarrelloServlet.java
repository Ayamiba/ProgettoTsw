package control;

import model.carrello.*;
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
import javax.servlet.http.Cookie;

@WebServlet ("/CarrelloServlet")

public class CarrelloServlet extends HttpServlet {
	private static final long serialVersionUID= 1L;
	
	private CarrelloDAO carrelloDAO;
	private static final int itemPerPage=6; //Elementi caricati in una pagina (caricati != mostrati)
	
	@Override
	public void init() throws ServletException {
		super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
		carrelloDAO=new CarrelloDAO();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    // al login viene salvato l'utente che c'è
	    model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user");
	    
	    List<ProdottoBean> prodottiCarrello = new ArrayList<>();
	    String email = request.getParameter("email"); 
	    
	    try {
	        if (utenteLoggato != null) {
	            // CASO 1: Utente registrato loggato e legge dal DB
	            prodottiCarrello = carrelloDAO.doRetrieveByCarrello(utenteLoggato.getEmail()); 
	        } else if (email != null && !email.trim().isEmpty()) {
	            // TEST: Se inserisci la mail nel form temporaneo di test Legge dal DB
	            prodottiCarrello = carrelloDAO.doRetrieveByCarrello(email);
	        } else {
	            // CASO 2: Utente OSPITE e legge dai COOKIE
	            Cookie[] cookies = request.getCookies();
	            String contenutoCookie = null;
	            
	            if (cookies != null) {
	                for (Cookie c : cookies) {
	                    if (c.getName().equals("carrello_ospite")) {
	                        contenutoCookie = c.getValue(); // Ci sarà una stringa con gli id dei prodotti del tipo 6-4-3
	                        break;
	                    }
	                }
	            }
	            
	            // Se il cookie esiste ed è pieno, recuperiamo i prodotti
	            if (contenutoCookie != null && !contenutoCookie.trim().isEmpty()) {
	                String[] idProdottiArray = contenutoCookie.split("-"); //togliendo i trattini
	                model.prodotto.ProdottoDAO prodottoDAO = new model.prodotto.ProdottoDAO();
	                
	                for (String idStr : idProdottiArray) {
	                    if (!idStr.isEmpty()) {
	                        int idProdotto = Integer.parseInt(idStr);
	                        ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);
	                        if (prodotto != null) {
	                            prodottiCarrello.add(prodotto);
	                        }
	                    }
	                }
	            }
	        }
	        
	        // Passa la lista finale alla JSP
	        request.setAttribute("prodottiCarrello", prodottiCarrello); 
	        
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/carrello.jsp");     
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

